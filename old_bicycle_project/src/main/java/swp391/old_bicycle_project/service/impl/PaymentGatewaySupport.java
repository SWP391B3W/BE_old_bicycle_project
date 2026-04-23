package swp391.old_bicycle_project.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import swp391.old_bicycle_project.config.SepayProperties;
import swp391.old_bicycle_project.dto.PaymentRequestDTO;
import swp391.old_bicycle_project.exception.AppException;
import swp391.old_bicycle_project.exception.ErrorCode;
import swp391.old_bicycle_project.model.Order;
import swp391.old_bicycle_project.model.Payment;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
final class PaymentGatewaySupport {

    private final SepayProperties sepayProperties;
    private final RestTemplate restTemplate;

    PaymentGatewaySupport(SepayProperties sepayProperties, RestTemplate restTemplate) {
        this.sepayProperties = sepayProperties;
        this.restTemplate = restTemplate;
    }

    public PaymentRequestDTO createUpfrontPaymentRequest(Order order, Payment payment) {
        String baXid = sepayProperties.getBankAccountId();
        String apiToken = sepayProperties.getApiToken();
        
        // KIỂM TRA: Nếu baXid là số thuần túy (v1), dùng endpoint v1 như thời ngrok
        boolean isV1 = baXid != null && baXid.matches("\\d+");
        
        String url;
        if (isV1) {
            // Quay lại công thức v1 ổn định: apikey truyền vào URL
            url = String.format("https://my.sepay.vn/userapi/orders/create?apikey=%s&bank_account_id=%s", 
                                apiToken, baXid);
            log.info("Using SePay v1 compatibility mode (ngrok style) for ID: {}", baXid);
        } else {
            // Dùng v2 nếu là UUID
            url = "https://userapi.sepay.vn/v2/bank-accounts/" + baXid + "/orders";
            log.info("Using SePay v2 mode for UUID: {}", baXid);
        }

        Map<String, Object> body = new HashMap<>();
        body.put("amount", payment.getAmount());
        body.put("description", payment.getGatewayOrderCode());
        body.put("order_id", payment.getGatewayOrderCode());

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            if (!isV1) {
                headers.set("Authorization", "Bearer " + apiToken);
            }

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

            if (!response.getStatusCode().is2xxSuccessful()) {
                log.error("SePay API Error: Status={}, Body={}", response.getStatusCode(), response.getBody());
                throw new AppException(ErrorCode.PAYMENT_GATEWAY_ERROR);
            }

            // QR Code link static cực nhanh
            String qrUrl = String.format("https://qr.sepay.vn/img?bank=%s&acc=%s&template=compact&amount=%d&des=%s",
                    sepayProperties.getBankBin(),
                    sepayProperties.getAccountNumber(),
                    payment.getAmount().longValue(),
                    payment.getGatewayOrderCode());

            return new PaymentRequestDTO(
                null,
                qrUrl,
                sepayProperties.getBankBin(),
                sepayProperties.getAccountNumber(),
                sepayProperties.getAccountName(),
                payment.getGatewayOrderCode(),
                "Thanh toan don hang " + order.getId(),
                order.getPaymentDeadline(),
                null
            );
        } catch (Exception e) {
            log.error("Payment Gateway Fatal Error: ", e);
            throw new AppException(ErrorCode.PAYMENT_GATEWAY_ERROR);
        }
    }
}
