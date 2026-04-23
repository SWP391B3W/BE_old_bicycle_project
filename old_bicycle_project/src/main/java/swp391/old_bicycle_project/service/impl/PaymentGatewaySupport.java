package swp391.old_bicycle_project.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
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

    PaymentGatewaySupport(SepayProperties sepayProperties, ObjectMapper objectMapper, RestTemplate restTemplate) {
        this.sepayProperties = sepayProperties;
        this.restTemplate = restTemplate;
    }

    public PaymentRequestDTO createUpfrontPaymentRequest(Order order, Payment payment) {
        // Build request body for SePay v2 - Cực kỳ đơn giản
        Map<String, Object> body = new HashMap<>();
        body.put("amount", payment.getAmount());
        body.put("description", payment.getGatewayOrderCode());
        body.put("order_id", payment.getGatewayOrderCode());

        // Lấy ID tài khoản từ config (UUID)
        String baXid = sepayProperties.getBankAccountId();
        String url = "https://userapi.sepay.vn/v2/bank-accounts/" + baXid + "/orders";

        log.info("Creating SePay order via: {}", url);
        
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + sepayProperties.getApiToken());

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

            if (!response.getStatusCode().is2xxSuccessful()) {
                log.error("SePay API failed: {}", response.getBody());
                throw new AppException(ErrorCode.PAYMENT_GATEWAY_ERROR);
            }

            // QR Code gọn nhẹ
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
            log.error("Payment Gateway Error: ", e);
            throw new AppException(ErrorCode.PAYMENT_GATEWAY_ERROR);
        }
    }
}
