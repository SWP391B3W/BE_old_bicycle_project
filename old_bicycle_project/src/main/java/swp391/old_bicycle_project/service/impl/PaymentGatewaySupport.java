package swp391.old_bicycle_project.service.impl;

import swp391.old_bicycle_project.config.SepayProperties;
import swp391.old_bicycle_project.entity.Order;
import swp391.old_bicycle_project.entity.Payment;
import swp391.old_bicycle_project.exception.AppException;
import swp391.old_bicycle_project.exception.ErrorCode;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static swp391.old_bicycle_project.service.impl.PaymentSupportUtils.firstNonBlank;
import static swp391.old_bicycle_project.service.impl.PaymentSupportUtils.hasText;
import static swp391.old_bicycle_project.service.impl.PaymentSupportUtils.parseDateTime;
import static swp391.old_bicycle_project.service.impl.PaymentSupportUtils.serializeJson;
import static swp391.old_bicycle_project.service.impl.PaymentSupportUtils.textOrNull;

final class PaymentGatewaySupport {

    private static final Logger log = LoggerFactory.getLogger(PaymentGatewaySupport.class);

    private static final DateTimeFormatter ORDER_CODE_TIMESTAMP_FORMAT =
            DateTimeFormatter.ofPattern("HHmmss");

    private final SepayProperties sepayProperties;
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;
    private ResolvedBankAccount cachedBankAccount;

    PaymentGatewaySupport(SepayProperties sepayProperties, ObjectMapper objectMapper, RestTemplate restTemplate) {
        this.sepayProperties = sepayProperties;
        this.objectMapper = objectMapper;
        this.restTemplate = restTemplate;
    }

    void validateSepayConfigurationForCurrentMode() {
        if (sepayProperties.isMockMode()) {
            return;
        }

        boolean missingWebhookKey = !hasText(sepayProperties.getWebhookApiKey());
        boolean hasLiveApiPath = hasText(sepayProperties.getApiToken())
                && (hasText(sepayProperties.getBankAccountId()) || hasText(sepayProperties.getAccountNumber()));
        boolean hasStaticTransferPath = hasText(sepayProperties.getBankBin())
                && hasText(sepayProperties.getAccountNumber());

        if (missingWebhookKey || (!hasLiveApiPath && !hasStaticTransferPath)) {
            throw new AppException(ErrorCode.PAYMENT_NOT_READY);
        }
    }

    PaymentProvisionResult provisionPayment(Order order, Payment payment) {
        if (!sepayProperties.isMockMode() && hasText(sepayProperties.getApiToken())) {
            ResolvedBankAccount resolvedBankAccount = resolveBankAccount();
            if (resolvedBankAccount != null && isBidvAccount(resolvedBankAccount)) {
                return createLiveSepayOrder(order, payment, resolvedBankAccount);
            }
            if (resolvedBankAccount != null) {
                return buildStaticTransferProvision(
                        order,
                        payment,
                        resolvedBankAccount.bankBin(),
                        resolvedBankAccount.accountNumber(),
                        resolvedBankAccount.accountName(),
                        buildStaticInstructions(payment)
                );
            }
        }
        return buildStaticTransferProvision(order, payment);
    }

    String generateGatewayOrderCode(Order order) {
        String shortOrderId = order.getId().toString().replace("-", "").substring(0, 12).toUpperCase();
        String timestamp = LocalDateTime.now().format(ORDER_CODE_TIMESTAMP_FORMAT);
        return "OB-" + shortOrderId + "-" + timestamp;
    }

    private PaymentProvisionResult createLiveSepayOrder(
            Order order,
            Payment payment,
            ResolvedBankAccount resolvedBankAccount
    ) {
        if (resolvedBankAccount == null || !hasText(resolvedBankAccount.bankAccountId())) {
            throw new AppException(ErrorCode.PAYMENT_NOT_READY);
        }

        String url = normalizeApiBaseUrl() + "/v2/bank-accounts/" + resolvedBankAccount.bankAccountId() + "/orders";
        Map<String, Object> requestBody = new LinkedHashMap<>();
        requestBody.put("amount", payment.getAmount());
        requestBody.put("order_code", payment.getGatewayOrderCode());
        requestBody.put("duration", computeDurationSeconds(order));
        requestBody.put("with_qrcode", true);

        JsonNode root = exchangeJson(url, HttpMethod.POST, requestBody, true);
        JsonNode data = root.path("data");
        if (data.isMissingNode() || data.isNull()) {
            throw new AppException(ErrorCode.PAYMENT_GATEWAY_ERROR);
        }

        String qrCodeUrl = textOrNull(data, "qr_code_url");
        String accountNumber = firstNonBlank(
                textOrNull(data, "va_number"),
                textOrNull(data, "account_number"),
                resolvedBankAccount.accountNumber(),
                sepayProperties.getAccountNumber()
        );
        String accountName = firstNonBlank(
                textOrNull(data, "account_holder_name"),
                textOrNull(data, "va_holder_name"),
                resolvedBankAccount.accountName(),
                sepayProperties.getAccountName()
        );
        LocalDateTime expiresAt = parseDateTime(firstNonBlank(
                textOrNull(data, "expired_at"),
                textOrNull(data, "expiredAt")
        ));

        String gatewayResponse = serializeJson(root, objectMapper);
        String instructions = buildLiveInstructions(payment, accountNumber, accountName);

        return new PaymentProvisionResult(
                null,
                qrCodeUrl,
                firstNonBlank(resolvedBankAccount.bankBin(), sepayProperties.getBankBin()),
                accountNumber,
                accountName,
                payment.getGatewayOrderCode(),
                instructions,
                expiresAt != null ? expiresAt : order.getPaymentDeadline(),
                gatewayResponse
        );
    }

    private PaymentProvisionResult buildStaticTransferProvision(Order order, Payment payment) {
        return buildStaticTransferProvision(
                order,
                payment,
                sepayProperties.getBankBin(),
                sepayProperties.getAccountNumber(),
                sepayProperties.getAccountName(),
                buildStaticInstructions(payment)
        );
    }

    private PaymentProvisionResult buildStaticTransferProvision(
            Order order,
            Payment payment,
            String bankBin,
            String accountNumber,
            String accountName,
            String instructions
    ) {
        return new PaymentProvisionResult(
                null,
                buildQrCodeUrl(payment, bankBin, accountNumber, accountName),
                bankBin,
                accountNumber,
                accountName,
                payment.getGatewayOrderCode(),
                instructions,
                order.getPaymentDeadline(),
                null
        );
    }

    private ResolvedBankAccount resolveBankAccount() {
        if (cachedBankAccount != null) {
            return cachedBankAccount;
        }
        
        // Nếu bạn đã cung cấp UUID và các thông tin khác, ta có thể tối ưu
        // Nhưng hiện tại để đảm bảo lấy đúng Bank BIN và Name từ SePay, ta vẫn gọi API
        // nhưng sẽ kiểm tra và log kỹ hơn.
        
        String url = normalizeApiBaseUrl() + "/v2/bank-accounts";
        JsonNode root = exchangeJson(url, HttpMethod.GET, null, true);
        JsonNode data = root.path("data");
        if (!data.isArray()) {
            data = root.path("bankaccounts");
            if (!data.isArray()) return null;
        }

        for (JsonNode node : data) {
            String id = firstNonBlank(textOrNull(node, "ba_xid"), textOrNull(node, "id"));
            String accountNumber = firstNonBlank(textOrNull(node, "bank_number"), textOrNull(node, "accountNumber"));
            String accountName = firstNonBlank(textOrNull(node, "account_name"), textOrNull(node, "accountName"));
            String bankBin = firstNonBlank(textOrNull(node, "bank_bin"), textOrNull(node, "bankBin"));
            String bankCode = firstNonBlank(textOrNull(node, "bank_id"), textOrNull(node, "bankId"), textOrNull(node, "bankCode"));
            String bankShortName = firstNonBlank(textOrNull(node, "bank_short_name"), textOrNull(node, "bankShortName"));

            boolean matchesConfigId = hasText(sepayProperties.getBankAccountId()) && sepayProperties.getBankAccountId().equals(id);
            boolean matchesConfigNum = hasText(sepayProperties.getAccountNumber()) && sepayProperties.getAccountNumber().equals(accountNumber);

            if (matchesConfigId || matchesConfigNum || (sepayProperties.getBankAccountId() == null && sepayProperties.getAccountNumber() == null)) {
                this.cachedBankAccount = new ResolvedBankAccount(id, accountNumber, accountName, bankBin, bankCode, bankShortName);
                return cachedBankAccount;
            }
        }
        return null;
    }

    private JsonNode exchangeJson(String url, HttpMethod method, Object body, boolean bearerAuth) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            if (bearerAuth) {
                if (hasText(sepayProperties.getApiToken())) {
                    String token = sepayProperties.getApiToken().trim();
                    if (!token.toLowerCase().startsWith("bearer ")) {
                        token = "Bearer " + token;
                    }
                    headers.set("Authorization", token);
                }
            }

            HttpEntity<?> entity = body == null
                    ? new HttpEntity<>(headers)
                    : new HttpEntity<>(body, headers);
            
            log.info("Executing external SePay request: {} {}", method, url);
            long start = System.currentTimeMillis();
            ResponseEntity<String> response = restTemplate.exchange(url, method, entity, String.class);
            long duration = System.currentTimeMillis() - start;
            log.info("SePay request completed in {}ms with status {}", duration, response.getStatusCode());

            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                log.error("SePay v2 Error Response: Status={}, Body={}", response.getStatusCode(), response.getBody());
                throw new RuntimeException("SePay API error: " + response.getStatusCode() + " - " + response.getBody());
            }
            return PaymentSupportUtils.parseJson(response.getBody(), objectMapper);
        } catch (RestClientException ex) {
            throw new AppException(ErrorCode.PAYMENT_GATEWAY_ERROR);
        }
    }

    private String normalizeApiBaseUrl() {
        String baseUrl = sepayProperties.getApiBaseUrl();
        if (baseUrl == null || baseUrl.isBlank()) {
            return "https://userapi.sepay.vn";
        }
        return baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
    }

    private String buildStaticInstructions(Payment payment) {
        if (!hasText(sepayProperties.getAccountNumber())) {
            return "Chưa cấu hình tài khoản SePay/nhận tiền. Bạn có thể dùng mock mode và gọi webhook để mô phỏng giao dịch.";
        }
        return "Chuyển khoản đúng số tiền "
                + payment.getAmount().toPlainString()
                + " VND với nội dung "
                + payment.getGatewayOrderCode()
                + ". Hệ thống sẽ đổi trạng thái order sau khi webhook ghi nhận giao dịch hợp lệ.";
    }

    private String buildLiveInstructions(Payment payment, String accountNumber, String accountName) {
        return "Thanh toán qua SePay cho khoản "
                + payment.getAmount().toPlainString()
                + " VND. Bạn có thể quét QR hoặc chuyển tới tài khoản "
                + firstNonBlank(accountNumber, "VA do SePay cấp")
                + (hasText(accountName) ? " - " + accountName : "")
                + ".";
    }

    private boolean isBidvAccount(ResolvedBankAccount resolvedBankAccount) {
        String bankCode = firstNonBlank(resolvedBankAccount.bankCode(), resolvedBankAccount.bankShortName());
        return hasText(bankCode) && bankCode.toUpperCase().contains("BIDV");
    }

    private String buildQrCodeUrl(Payment payment, String bankBin, String accountNumber, String accountName) {
        if (!hasText(bankBin) || !hasText(accountNumber)) {
            return null;
        }

        String safeAccountName = accountName != null ? accountName : "";
        return "https://img.vietqr.io/image/"
                + bankBin
                + "-"
                + accountNumber
                + "-compact2.png?amount="
                + payment.getAmount().toPlainString()
                + "&addInfo="
                + URLEncoder.encode(payment.getGatewayOrderCode(), StandardCharsets.UTF_8)
                + "&accountName="
                + URLEncoder.encode(safeAccountName, StandardCharsets.UTF_8);
    }

    private int computeDurationSeconds(Order order) {
        if (order.getPaymentDeadline() == null) {
            return 900;
        }
        long seconds = Duration.between(LocalDateTime.now(), order.getPaymentDeadline()).getSeconds();
        if (seconds < 60) {
            return 60;
        }
        return Math.toIntExact(Math.min(seconds, 86400));
    }
}
