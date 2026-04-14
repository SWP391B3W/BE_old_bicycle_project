package swp391.old_bicycle_project.service;

import org.springframework.web.multipart.MultipartFile;
import swp391.old_bicycle_project.entity.Order;
import swp391.old_bicycle_project.entity.OrderEvidenceSubmission;
import swp391.old_bicycle_project.entity.User;
import swp391.old_bicycle_project.entity.enums.OrderEvidenceType;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface OrderEvidenceService {

    OrderEvidenceSubmission createSellerHandoverEvidence(
            Order order,
            User submittedBy,
            String note,
            List<MultipartFile> files
    );

    OrderEvidenceSubmission createBuyerReceiptEvidence(
            Order order,
            User submittedBy,
            String note,
            List<MultipartFile> files
    );

    Map<UUID, Map<OrderEvidenceType, OrderEvidenceSubmission>> getEvidenceByOrderIds(Collection<UUID> orderIds);

    Map<OrderEvidenceType, OrderEvidenceSubmission> getEvidenceByOrderId(UUID orderId);
}
