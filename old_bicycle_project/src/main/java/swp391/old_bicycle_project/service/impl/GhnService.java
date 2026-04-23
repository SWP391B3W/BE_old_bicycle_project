package swp391.old_bicycle_project.service.impl;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import swp391.old_bicycle_project.config.GhnConfig;
import swp391.old_bicycle_project.dto.ghn.*;

import java.util.Collections;
import java.util.List;

@Service
public class GhnService {
    private static final Logger log = LoggerFactory.getLogger(GhnService.class);

    private final GhnConfig ghnConfig;
    private final RestTemplate restTemplate;

    public GhnService(GhnConfig ghnConfig) {
        this.ghnConfig = ghnConfig;

        // Configure RestTemplate to ignore unknown JSON properties from GHN API
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter(mapper);

        this.restTemplate = new RestTemplate();
        this.restTemplate.getMessageConverters().removeIf(c -> c instanceof MappingJackson2HttpMessageConverter);
        this.restTemplate.getMessageConverters().add(converter);
    }

    private HttpHeaders getPublicHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Token", ghnConfig.getApiToken());
        headers.set("Content-Type", "application/json");
        return headers;
    }

    private HttpHeaders getShopHeaders() {
        HttpHeaders headers = getPublicHeaders();
        headers.set("ShopId", String.valueOf(ghnConfig.getShopId()));
        return headers;
    }

    public List<GhnProvince> getProvinces() {
        try {
            String url = ghnConfig.getApiUrl() + "/master-data/province";
            String token = ghnConfig.getApiToken();
            Integer shopId = ghnConfig.getShopId();

            System.out.println("DEBUG GHN: URL=" + url);
            System.out.println("DEBUG GHN: Token=" + token);
            System.out.println("DEBUG GHN: ShopId=" + shopId);

            if (token == null || token.isEmpty()) {
                log.error("GHN Token is MISSING!");
                return Collections.emptyList();
            }

            HttpEntity<String> entity = new HttpEntity<>(getPublicHeaders());

            log.info("Executing GHN Provinces request...");

            // Debug: Get raw response as string first
            try {
                ResponseEntity<String> raw = new RestTemplate().exchange(url, HttpMethod.GET, entity, String.class);
                System.out.println("DEBUG GHN RAW RESPONSE: " + raw.getBody());
            } catch (Exception re) {
                System.out.println("DEBUG GHN RAW ERROR: " + re.getMessage());
            }

            ResponseEntity<GhnResponse<List<GhnProvince>>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<GhnResponse<List<GhnProvince>>>() {
                    });

            GhnResponse<List<GhnProvince>> body = response.getBody();
            if (body == null) {
                log.error("GHN Response Body is NULL");
                return Collections.emptyList();
            }

            log.info("GHN Response Code: {}", body.getCode());
            log.info("GHN Response Message: {}", body.getMessage());

            if (body.getData() == null) {
                log.error("GHN Response Data is NULL");
                return Collections.emptyList();
            }

            log.info("GHN Provinces found: {}", body.getData().size());
            return body.getData();
        } catch (Exception e) {
            System.out.println("DEBUG GHN ERROR: " + e.getMessage());
            log.error("Failed to fetch GHN provinces", e);
            return Collections.emptyList();
        }
    }

    public List<GhnDistrict> getDistricts(Integer provinceId) {
        try {
            String url = ghnConfig.getApiUrl() + "/master-data/district?province_id=" + provinceId;
            HttpEntity<String> entity = new HttpEntity<>(getPublicHeaders());
            ResponseEntity<GhnResponse<List<GhnDistrict>>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<GhnResponse<List<GhnDistrict>>>() {
                    });
            return response.getBody() != null ? response.getBody().getData() : Collections.emptyList();
        } catch (Exception e) {
            log.error("Failed to fetch GHN districts for province " + provinceId, e);
            return Collections.emptyList();
        }
    }

    public List<GhnWard> getWards(Integer districtId) {
        try {
            String url = ghnConfig.getApiUrl() + "/master-data/ward?district_id=" + districtId;
            HttpEntity<String> entity = new HttpEntity<>(getPublicHeaders());
            ResponseEntity<GhnResponse<List<GhnWard>>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<GhnResponse<List<GhnWard>>>() {
                    });
            return response.getBody() != null ? response.getBody().getData() : Collections.emptyList();
        } catch (Exception e) {
            log.error("Failed to fetch GHN wards for district " + districtId, e);
            return Collections.emptyList();
        }
    }

    public Integer calculateFee(GhnFeeRequest request) {
        try {
            // Default District 1, HCM City origin if missing
            if (request.getFromDistrictId() == null) {
                request.setFromDistrictId(1454);
            }
            log.info("Calculating fee from district: {} to district: {}", request.getFromDistrictId(), request.getToDistrictId());
            
            // Dynamic Service Selection based on route
            List<GhnServiceItemResponse> availableServices = getAvailableServices(request.getFromDistrictId(), request.getToDistrictId());
            if (!availableServices.isEmpty()) {
                // Try to find Standard service (service_type_id = 2)
                GhnServiceItemResponse selected = availableServices.stream()
                        .filter(s -> Integer.valueOf(2).equals(s.getServiceTypeId()))
                        .findFirst()
                        .orElse(availableServices.get(0));
                
                request.setServiceId(selected.getServiceId());
                request.setServiceTypeId(selected.getServiceTypeId());
                log.info("Selected GHN Service: {} (ID: {}, Type: {})", 
                    selected.getShortName(), selected.getServiceId(), selected.getServiceTypeId());
            } else {
                // Fallback to defaults if lookup fails
                request.setServiceId(53320);
                request.setServiceTypeId(2);
            }
            request.setWeight(15000);

            String url = ghnConfig.getApiUrl() + "/v2/shipping-order/fee";
            HttpEntity<GhnFeeRequest> entity = new HttpEntity<>(request, getShopHeaders());
            ResponseEntity<GhnResponse<GhnFeeResponse>> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    new ParameterizedTypeReference<GhnResponse<GhnFeeResponse>>() {
                    });

            if (response.getBody() != null) {
                log.error("GHN Fee calculation response: code={}, message={}", 
                    response.getBody().getCode(), response.getBody().getMessage());
                if (response.getBody().getData() != null) {
                    return response.getBody().getData().getTotal();
                }
            }
            return 0;
        } catch (org.springframework.web.client.HttpStatusCodeException e) {
            log.error("GHN API Error: Status={}, ResponseBody={}", e.getStatusCode(), e.getResponseBodyAsString());
            return 0;
        } catch (Exception e) {
            log.error("Unexpected error calculating GHN fee", e);
            return 0;
        }
    }

    public List<GhnServiceItemResponse> getAvailableServices(Integer fromDistrictId, Integer toDistrictId) {
        try {
            String url = ghnConfig.getApiUrl() + "/v2/shipping-order/available-services";
            GhnServiceRequest request = GhnServiceRequest.builder()
                    .shopId(ghnConfig.getShopId())
                    .fromDistrict(fromDistrictId)
                    .toDistrict(toDistrictId)
                    .build();

            HttpEntity<GhnServiceRequest> entity = new HttpEntity<>(request, getPublicHeaders());

            ResponseEntity<GhnResponse<List<GhnServiceItemResponse>>> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    new ParameterizedTypeReference<GhnResponse<List<GhnServiceItemResponse>>>() {
                    });

            return response.getBody() != null ? response.getBody().getData() : Collections.emptyList();
        } catch (Exception e) {
            log.error("Failed to fetch GHN available services", e);
            return Collections.emptyList();
        }
    }
}
