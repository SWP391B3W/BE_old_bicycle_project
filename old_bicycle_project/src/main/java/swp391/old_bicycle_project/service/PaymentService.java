package swp391.old_bicycle_project.service;

import swp391.old_bicycle_project.dto.response.PaymentRequestResponseDTO;
import swp391.old_bicycle_project.dto.response.PaymentResponseDTO;
import swp391.old_bicycle_project.entity.User;

import java.util.List;
import java.util.UUID;

public interface PaymentService {

    PaymentRequestResponseDTO createUpfrontPaymentRequest(UUID orderId, User currentUser);

    List<PaymentResponseDTO> getOrderPayments(UUID orderId, User currentUser);

    void handleSepayWebhook(String rawPayload, String authorizationHeader);

    int expireOverdueUpfrontPayments();
}
