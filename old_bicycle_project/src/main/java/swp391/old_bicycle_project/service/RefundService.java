package swp391.old_bicycle_project.service;

import swp391.old_bicycle_project.dto.request.RefundCreateRequestDTO;
import swp391.old_bicycle_project.dto.request.RefundReviewRequestDTO;
import swp391.old_bicycle_project.dto.response.AdminRefundResponseDTO;
import swp391.old_bicycle_project.dto.response.RefundResponseDTO;
import swp391.old_bicycle_project.entity.User;
import swp391.old_bicycle_project.entity.enums.RefundStatus;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface RefundService {

    RefundResponseDTO requestRefund(UUID orderId, User currentUser, RefundCreateRequestDTO requestDTO, List<MultipartFile> files);

    RefundResponseDTO reviewRefund(UUID refundId, User currentUser, RefundReviewRequestDTO requestDTO);

    Page<AdminRefundResponseDTO> getAdminRefunds(String keyword, RefundStatus status, int page, int size);
}
