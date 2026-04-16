package swp391.old_bicycle_project.service;

import swp391.old_bicycle_project.dto.request.PayoutCompleteRequestDTO;
import swp391.old_bicycle_project.dto.request.PayoutProfileUpsertRequestDTO;
import swp391.old_bicycle_project.dto.response.AdminPayoutResponseDTO;
import swp391.old_bicycle_project.dto.response.PayoutProfileResponseDTO;
import swp391.old_bicycle_project.entity.Order;
import swp391.old_bicycle_project.entity.Payout;
import swp391.old_bicycle_project.entity.RefundRequest;
import swp391.old_bicycle_project.entity.User;
import swp391.old_bicycle_project.entity.enums.PayoutStatus;
import swp391.old_bicycle_project.entity.enums.PayoutType;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface PayoutService {
    PayoutProfileResponseDTO getMyProfile(User currentUser);

    PayoutProfileResponseDTO upsertMyProfile(User currentUser, PayoutProfileUpsertRequestDTO request);

    Page<AdminPayoutResponseDTO> getAdminPayouts(String keyword, PayoutType type, PayoutStatus status, int page, int size);

    AdminPayoutResponseDTO completePayout(UUID payoutId, User currentUser, PayoutCompleteRequestDTO request);

    AdminPayoutResponseDTO remindProfileRequiredPayout(UUID payoutId, User currentUser);

    Payout ensureRefundPayout(RefundRequest refundRequest);

    Payout ensureSellerReleasePayout(Order order);

    Payout completeRefundPayout(Payout payout, User currentUser, String bankReference, String adminNote);

    boolean hasCompleteProfile(User user);
}
