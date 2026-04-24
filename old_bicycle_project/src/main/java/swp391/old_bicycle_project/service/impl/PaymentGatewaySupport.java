package swp391.old_bicycle_project.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import swp391.old_bicycle_project.config.SepayProperties;
import swp391.old_bicycle_project.dto.response.PaymentRequestResponseDTO;
import swp391.old_bicycle_project.entity.Order;
import swp391.old_bicycle_project.entity.Payment;
import swp391.old_bicycle_project.exception.AppException;
import swp391.old_bicycle_project.exception.ErrorCode;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Component
final class PaymentGatewaySupport {

    private static final Logger log = LoggerFactory.getLogger(PaymentGatewaySupport.class);

    private final SepayProperties sepayProperties;
    private final RestTemplate restTemplate;

    PaymentGatewaySupport(SepayProperties sepayProperties, ObjectMapper objectMapper, RestTemplate restTemplate) {
        this.sepayProperties = sepayProperties;
        this.restTemplate = restTemplate;
    }

    public void validateSepayConfigurationForCurrentMode() {
        if (sepayProperties.isMockMode()) {
            log.info("Payment Gateway is running in MOCK MODE.");
            return;
        }
        log.info("Payment Gateway is running in LIVE MODE.");
    }

    public String generateGatewayOrderCode(Order order) {
        String shortId = order.getId() != null
                ? order.getId().toString().replace("-", "").substring(0, 8)
                : String.valueOf(System.currentTimeMillis() % 100000);
        return "OB-" + shortId.toUpperCase() + "-" + (System.currentTimeMillis() / 1000 % 10000);
    }

    public PaymentProvisionResult provisionPayment(Order order, Payment payment) {
        if (sepayProperties.isMockMode() || sepayProperties.getApiToken() == null) {
            log.info("[MOCK/STATIC] Building static QR provision for order: {}", order.getId());
            return buildStaticProvision(order, payment);
        }

        String baXid = sepayProperties.getBankAccountId();
        String apiToken = sepayProperties.getApiToken();
        String baseUrl = sepayProperties.getApiBaseUrl() != null
                ? sepayProperties.getApiBaseUrl().replaceAll("/$", "")
                : "https://my.sepay.vn/userapi";

        String url = baseUrl + "/orders/create?apikey=" + apiToken + "&bank_account_id=" + baXid;

        Map<String, Object> body = new HashMap<>();
        body.put("amount", payment.getAmount().longValue());
        body.put("description", payment.getGatewayOrderCode());
        body.put("order_id", payment.getGatewayOrderCode());

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

            if (!response.getStatusCode().is2xxSuccessful()) {
                log.warn("SePay returned {}, falling back to static QR.", response.getStatusCode());
                return buildStaticProvision(order, payment);
            }

            return buildStaticProvision(order, payment);
        } catch (Exception e) {
            log.error("SePay connection failed, falling back to static QR: {}", e.getMessage());
            return buildStaticProvision(order, payment);
        }
    }

    // PaymentProvisionResult có 9 tham số (theo PaymentSupportModels.java):
    // checkoutUrl, qrCodeUrl, bankBin, bankAccountNumber, bankAccountName,
    // transferContent, instructions, expiresAt, gatewayResponse
    private PaymentProvisionResult buildStaticProvision(Order order, Payment payment) {
        String qrUrl = "https://img.vietqr.io/image/"
                + sepayProperties.getBankBin()
                + "-"
                + sepayProperties.getAccountNumber()
                + "-compact2.png?amount="
                + payment.getAmount().longValue()
                + "&addInfo="
                + URLEncoder.encode(payment.getGatewayOrderCode(), StandardCharsets.UTF_8)
                + "&accountName="
                + URLEncoder.encode(sepayProperties.getAccountName() != null ? sepayProperties.getAccountName() : "", StandardCharsets.UTF_8);

        String instructions = "Chuyển khoản đúng số tiền "
                + payment.getAmount().toPlainString()
                + " VND với nội dung "
                + payment.getGatewayOrderCode()
                + ". Hệ thống sẽ tự động xác nhận.";

        return new PaymentProvisionResult(
                null,                                   // checkoutUrl
                qrUrl,                                  // qrCodeUrl
                sepayProperties.getBankBin(),           // bankBin
                sepayProperties.getAccountNumber(),     // bankAccountNumber
                sepayProperties.getAccountName(),       // bankAccountName
                payment.getGatewayOrderCode(),          // transferContent
                instructions,                           // instructions
                order.getPaymentDeadline(),             // expiresAt
                null                                    // gatewayResponse
        );
    }
}
