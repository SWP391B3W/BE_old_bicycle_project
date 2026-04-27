package swp391.old_bicycle_project.service.impl;

import swp391.old_bicycle_project.config.NotificationEvent;
import swp391.old_bicycle_project.dto.request.RefundCreateRequestDTO;
import swp391.old_bicycle_project.dto.request.RefundReviewRequestDTO;
import swp391.old_bicycle_project.dto.response.AdminRefundResponseDTO;
import swp391.old_bicycle_project.dto.response.OrderEvidenceSubmissionResponseDTO;
import swp391.old_bicycle_project.dto.response.RefundEvidenceFileResponseDTO;
import swp391.old_bicycle_project.dto.response.RefundResponseDTO;
import swp391.old_bicycle_project.entity.Order;
import swp391.old_bicycle_project.entity.Payment;
import swp391.old_bicycle_project.entity.Payout;
import swp391.old_bicycle_project.entity.RefundRequest;
import swp391.old_bicycle_project.entity.RefundRequestFile;
import swp391.old_bicycle_project.entity.User;
import swp391.old_bicycle_project.entity.enums.AppRole;
import swp391.old_bicycle_project.entity.enums.NotificationType;
import swp391.old_bicycle_project.entity.enums.OrderEvidenceType;
import swp391.old_bicycle_project.entity.enums.OrderFundingStatus;
import swp391.old_bicycle_project.entity.enums.OrderStatus;
import swp391.old_bicycle_project.entity.enums.PaymentPhase;
import swp391.old_bicycle_project.entity.enums.PaymentStatus;
import swp391.old_bicycle_project.entity.enums.RefundStatus;
import swp391.old_bicycle_project.exception.AppException;
import swp391.old_bicycle_project.exception.ErrorCode;
import swp391.old_bicycle_project.repository.InspectionRepository;
import swp391.old_bicycle_project.repository.OrderRepository;
import swp391.old_bicycle_project.repository.PaymentRepository;
import swp391.old_bicycle_project.repository.RefundRequestRepository;
import swp391.old_bicycle_project.repository.UserRepository;
import swp391.old_bicycle_project.service.OrderEvidenceService;
import swp391.old_bicycle_project.service.PayoutService;
import swp391.old_bicycle_project.service.RefundService;
import swp391.old_bicycle_project.service.StorageService;
import swp391.old_bicycle_project.specification.RefundRequestSpecification;
import swp391.old_bicycle_project.validation.MultipartFileValidationUtils;
import swp391.old_bicycle_project.validation.PaginationValidationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefundServiceImpl implements RefundService {

    private static final int MAX_REFUND_EVIDENCE_FILES = 3;

    private final RefundRequestRepository refundRequestRepository;
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final InspectionRepository inspectionRepository;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final PayoutService payoutService;
    private final OrderEvidenceService orderEvidenceService;
    private final StorageService storageService;

    @Override
    @Transactional
    // requestRefund gồm check quyền buyer trên order, validate điều kiện refundable,
    // tạo refund request + upload evidence, cập nhật funding status và gửi notification.
    public RefundResponseDTO requestRefund(
            UUID orderId,
            User currentUser,
            RefundCreateRequestDTO requestDTO,
            List<MultipartFile> files
    ) {
        // 1. Lấy order của buyer hiện tại
        Order order = orderRepository.findByIdAndBuyerId(orderId, currentUser.getId())
                .orElseThrow(() -> new AppException(ErrorCode.RECORD_NOT_EXISTS));

        // 2. Chỉ cho refund khi order đang deposited/awaiting_buyer_confirmation và tiền đang held
        boolean refundableStatus = order.getStatus() == OrderStatus.deposited
                || order.getStatus() == OrderStatus.awaiting_buyer_confirmation;
        if (!refundableStatus || order.getFundingStatus() != OrderFundingStatus.held) {
            throw new AppException(ErrorCode.REFUND_NOT_ALLOWED);
        }

        // 3. Chặn tạo trùng refund pending cho cùng order
        if (refundRequestRepository.findFirstByOrderIdAndStatusOrderByCreatedAtDesc(orderId, RefundStatus.pending).isPresent()) {
            throw new AppException(ErrorCode.RECORD_ALREADY_EXISTS);
        }

        // 4. Lấy payment upfront thành công để xác định số tiền hoàn
        Payment payment = paymentRepository.findFirstByOrderIdAndPhaseOrderByCreatedAtDesc(orderId, PaymentPhase.upfront)
                .filter(candidate -> candidate.getStatus() == PaymentStatus.success)
                .orElseThrow(() -> new AppException(ErrorCode.REFUND_NOT_ALLOWED));

        // 5. Tính và validate refund amount
        BigDecimal refundAmount = payment.getAmount() != null ? payment.getAmount() : order.getPaidAmount();
        if (refundAmount == null || refundAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new AppException(ErrorCode.REFUND_NOT_ALLOWED);
        }
        if (requestDTO.getAmount() != null && requestDTO.getAmount().compareTo(refundAmount) != 0) {
            throw new AppException(ErrorCode.REFUND_NOT_ALLOWED);
        }

        // 6. Tạo refund request pending
        RefundRequest refundRequest = refundRequestRepository.save(RefundRequest.builder()
                .order(order)
                .payment(payment)
                .requester(currentUser)
                .amount(refundAmount)
                .reason(normalizeText(requestDTO.getReason()))
                .evidenceNote(normalizeText(requestDTO.getEvidenceNote()))
                .status(RefundStatus.pending)
                .build());

            // 7. Gắn evidence files (nếu có)
        refundRequest = attachEvidenceFiles(refundRequest, files);

            // 8. Cập nhật funding status order sang refund_pending
        order.setFundingStatus(OrderFundingStatus.refund_pending);
        orderRepository.save(order);

            // 9. Gửi notification cho buyer và admin
        publishOrderNotification(
                currentUser.getId(),
                "Đã tạo yêu cầu hoàn tiền",
                "Yêu cầu hoàn tiền của bạn đã được gửi cho admin xem xét.",
                "{\"orderId\":\"" + order.getId() + "\",\"refundId\":\"" + refundRequest.getId() + "\"}"
        );
        publishAdminRefundPendingNotification(order, refundRequest);

        return mapToDTO(refundRequest);
    }

    @Override
    @Transactional
    // reviewRefund gồm lấy refund theo id, route theo action (approve/reject/complete),
    // lưu trạng thái liên quan và trả kết quả mới nhất.
    public RefundResponseDTO reviewRefund(UUID refundId, User currentUser, RefundReviewRequestDTO requestDTO) {
        // 1. Lấy refund request cần xử lý
        RefundRequest refundRequest = refundRequestRepository.findById(refundId)
                .orElseThrow(() -> new AppException(ErrorCode.RECORD_NOT_EXISTS));
        Order order = refundRequest.getOrder();
        Payment payment = refundRequest.getPayment();

        // 2. Điều hướng xử lý theo status admin gửi lên
        switch (requestDTO.getStatus()) {
            case approved -> approveRefund(refundRequest, order, currentUser, requestDTO);
            case rejected -> rejectRefund(refundRequest, order, currentUser, requestDTO);
            case completed -> completeRefund(refundRequest, currentUser, requestDTO);
            default -> throw new AppException(ErrorCode.INVALID_STATUS);
        }

        // 3. Persist các thay đổi refund/payment/order
        refundRequestRepository.save(refundRequest);
        if (refundRequest.getStatus() != RefundStatus.completed) {
            paymentRepository.save(payment);
            orderRepository.save(order);
        }

        return mapToDTO(refundRequest);
    }

    @Override
    @Transactional(readOnly = true)
    // getAdminRefunds gồm filter + phân trang danh sách refund,
    // enrich dữ liệu inspection và order evidence cho màn admin.
    public Page<AdminRefundResponseDTO> getAdminRefunds(String keyword, RefundStatus status, int page, int size) {
        // 1. Tạo pageable chuẩn
        var pageable = PaginationValidationUtils.createPageRequest(page, size, Sort.by("createdAt").descending());
        // 2. Query danh sách refund theo bộ lọc admin
        Page<RefundRequest> refundPage = refundRequestRepository.findAll(
                RefundRequestSpecification.fromAdminFilter(keyword, status),
                pageable
        );

        // 3. Gom productIds để check inspection
        Set<UUID> productIds = refundPage.getContent().stream()
                .map(RefundRequest::getOrder)
                .filter(order -> order != null && order.getProduct() != null)
                .map(order -> order.getProduct().getId())
                .collect(java.util.stream.Collectors.toSet());
            // 4. Lấy tập product đã có inspection
        Set<UUID> inspectedProductIds = productIds.isEmpty()
                ? Set.of()
                : new HashSet<>(inspectionRepository.findDistinctProductIdsWithInspection(productIds));
            // 5. Lấy evidence theo order để enrich response
        Map<UUID, Map<OrderEvidenceType, OrderEvidenceSubmissionResponseDTO>> evidenceByOrder =
                orderEvidenceService.getEvidenceByOrderIds(
                        refundPage.getContent().stream()
                                .map(RefundRequest::getOrder)
                                .filter(order -> order != null)
                                .map(Order::getId)
                                .toList()
                );

            // 6. Map sang AdminRefundResponseDTO
        return refundPage.map(refundRequest -> mapToAdminDTO(
                refundRequest,
                refundRequest.getOrder() != null
                        && refundRequest.getOrder().getProduct() != null
                        && inspectedProductIds.contains(refundRequest.getOrder().getProduct().getId()),
                evidenceByOrder.getOrDefault(
                        refundRequest.getOrder() != null ? refundRequest.getOrder().getId() : null,
                        Map.of()
                )
        ));
    }

    private void approveRefund(
            RefundRequest refundRequest,
            Order order,
            User currentUser,
            RefundReviewRequestDTO requestDTO
    ) {
        // 1. Chỉ approve khi refund đang pending
        if (refundRequest.getStatus() != RefundStatus.pending) {
            throw new AppException(ErrorCode.INVALID_STATUS);
        }

        // 2. Cập nhật trạng thái duyệt + thông tin reviewer
        refundRequest.setStatus(RefundStatus.approved);
        refundRequest.setAdminNote(requestDTO.getAdminNote());
        refundRequest.setReviewedBy(currentUser);
        refundRequest.setReviewedAt(LocalDateTime.now());
        order.setFundingStatus(OrderFundingStatus.refund_pending_transfer);

        // 3. Đảm bảo payout bản ghi hoàn tiền đã được tạo
        payoutService.ensureRefundPayout(refundRequest);
    }

    private void rejectRefund(
            RefundRequest refundRequest,
            Order order,
            User currentUser,
            RefundReviewRequestDTO requestDTO
    ) {
        // 1. Chỉ reject khi refund đang pending
        if (refundRequest.getStatus() != RefundStatus.pending) {
            throw new AppException(ErrorCode.INVALID_STATUS);
        }

        // 2. Cập nhật trạng thái từ chối + reviewer
        refundRequest.setStatus(RefundStatus.rejected);
        refundRequest.setAdminNote(requestDTO.getAdminNote());
        refundRequest.setReviewedBy(currentUser);
        refundRequest.setReviewedAt(LocalDateTime.now());
        order.setFundingStatus(OrderFundingStatus.held);

        // 3. Thông báo kết quả cho buyer
        publishOrderNotification(
                refundRequest.getRequester().getId(),
                "Yêu cầu hoàn tiền bị từ chối",
                "Admin đã từ chối yêu cầu hoàn tiền của bạn. Hãy xem ghi chú để biết thêm chi tiết.",
                "{\"orderId\":\"" + order.getId() + "\",\"refundId\":\"" + refundRequest.getId() + "\"}"
        );
    }

    private void completeRefund(
            RefundRequest refundRequest,
            User currentUser,
            RefundReviewRequestDTO requestDTO
    ) {
        // 1. Chỉ complete khi refund đã approved
        if (refundRequest.getStatus() != RefundStatus.approved) {
            throw new AppException(ErrorCode.INVALID_STATUS);
        }

        // 2. Lấy payout refund và đánh dấu completed
        Payout payout = payoutService.ensureRefundPayout(refundRequest);
        payoutService.completeRefundPayout(payout, currentUser, requestDTO.getRefundReference(), requestDTO.getAdminNote());
    }

    private void publishOrderNotification(UUID userId, String title, String content, String metadata) {
        eventPublisher.publishEvent(new NotificationEvent(
                this,
                userId,
                title,
                content,
                NotificationType.order,
                metadata
        ));
    }

    private void publishAdminRefundPendingNotification(Order order, RefundRequest refundRequest) {
        String metadata = "{\"orderId\":\"" + order.getId() + "\",\"refundId\":\"" + refundRequest.getId() + "\"}";
        String productTitle = order.getProduct() != null && order.getProduct().getTitle() != null
                ? order.getProduct().getTitle()
                : "đơn hàng";
        userRepository.findByRole(AppRole.admin).stream()
                .map(User::getId)
                .distinct()
                .forEach(adminId -> eventPublisher.publishEvent(new NotificationEvent(
                        this,
                        adminId,
                        "Có yêu cầu hoàn tiền mới cần duyệt",
                        "Đơn \"" + productTitle + "\" vừa có yêu cầu hoàn tiền từ người mua.",
                        NotificationType.order,
                        metadata
                )));
    }

    private RefundRequest attachEvidenceFiles(RefundRequest refundRequest, List<MultipartFile> files) {
        // 1. Normalize + validate file đầu vào
        List<MultipartFile> normalizedFiles = MultipartFileValidationUtils.normalizeFiles(files);
        validateFiles(normalizedFiles);

        // 2. Không có file thì giữ nguyên refund request
        if (normalizedFiles.isEmpty()) {
            return refundRequest;
        }

        List<String> uploadedUrls = new ArrayList<>();

        try {
            // 3. Upload từng file và gắn vào evidence list
            for (int index = 0; index < normalizedFiles.size(); index++) {
                MultipartFile file = normalizedFiles.get(index);
                String fileUrl = storageService.uploadFile(file, buildRefundEvidenceFolder(refundRequest.getId()));
                uploadedUrls.add(fileUrl);
                refundRequest.addEvidenceFile(RefundRequestFile.builder()
                        .fileUrl(fileUrl)
                        .fileName(file.getOriginalFilename())
                        .contentType(file.getContentType())
                        .sortOrder(index)
                        .build());
            }

            // 4. Lưu refund request sau khi gắn file
            return refundRequestRepository.save(refundRequest);
        } catch (RuntimeException exception) {
            // 5. Nếu lỗi thì rollback best-effort các file đã upload
            uploadedUrls.forEach(storageService::deleteFile);
            throw exception;
        }
    }

    // validateFiles gồm validate số lượng + định dạng ảnh của evidence refund.
    private void validateFiles(List<MultipartFile> files) {
        MultipartFileValidationUtils.validateImageFiles(
                files,
                MAX_REFUND_EVIDENCE_FILES,
                ErrorCode.REFUND_EVIDENCE_LIMIT_EXCEEDED,
                ErrorCode.REFUND_EVIDENCE_IMAGE_ONLY
        );
    }

    private String normalizeText(String value) {
        if (value == null) {
            return null;
        }

        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private String buildRefundEvidenceFolder(UUID refundId) {
        return "refunds/" + refundId;
    }

    private List<RefundEvidenceFileResponseDTO> mapEvidenceFiles(Collection<RefundRequestFile> files) {
        if (files == null || files.isEmpty()) {
            return List.of();
        }

        return files.stream()
                .map(file -> RefundEvidenceFileResponseDTO.builder()
                        .id(file.getId())
                        .fileUrl(file.getFileUrl())
                        .fileName(file.getFileName())
                        .contentType(file.getContentType())
                        .sortOrder(file.getSortOrder())
                        .build())
                .toList();
    }

    private RefundResponseDTO mapToDTO(RefundRequest refundRequest) {
        return RefundResponseDTO.builder()
                .id(refundRequest.getId())
                .orderId(refundRequest.getOrder().getId())
                .paymentId(refundRequest.getPayment().getId())
                .requesterId(refundRequest.getRequester().getId())
                .requesterName(refundRequest.getRequester().getFullName())
                .amount(refundRequest.getAmount())
                .reason(refundRequest.getReason())
                .evidenceNote(refundRequest.getEvidenceNote())
                .evidenceFiles(mapEvidenceFiles(refundRequest.getEvidenceFiles()))
                .status(refundRequest.getStatus())
                .adminNote(refundRequest.getAdminNote())
                .refundReference(refundRequest.getRefundReference())
                .reviewedBy(refundRequest.getReviewedBy() != null ? refundRequest.getReviewedBy().getId() : null)
                .reviewedByName(refundRequest.getReviewedBy() != null ? refundRequest.getReviewedBy().getFullName() : null)
                .reviewedAt(refundRequest.getReviewedAt())
                .processedAt(refundRequest.getProcessedAt())
                .createdAt(refundRequest.getCreatedAt())
                .build();
    }

    private AdminRefundResponseDTO mapToAdminDTO(
            RefundRequest refundRequest,
            boolean hasInspection,
            Map<OrderEvidenceType, OrderEvidenceSubmissionResponseDTO> evidenceByType
    ) {
        Order order = refundRequest.getOrder();
        Payment payment = refundRequest.getPayment();

        return AdminRefundResponseDTO.builder()
                .id(refundRequest.getId())
                .orderId(order != null ? order.getId() : null)
                .paymentId(payment != null ? payment.getId() : null)
                .requesterId(refundRequest.getRequester().getId())
                .requesterName(refundRequest.getRequester().getFullName())
                .buyerId(order != null && order.getBuyer() != null ? order.getBuyer().getId() : null)
                .buyerName(order != null && order.getBuyer() != null ? order.getBuyer().getFullName() : null)
                .sellerId(order != null && order.getSeller() != null ? order.getSeller().getId() : null)
                .sellerName(order != null && order.getSeller() != null ? order.getSeller().getFullName() : null)
                .productId(order != null && order.getProduct() != null ? order.getProduct().getId() : null)
                .productTitle(order != null && order.getProduct() != null ? order.getProduct().getTitle() : null)
                .hasInspection(hasInspection)
                .amount(refundRequest.getAmount())
                .reason(refundRequest.getReason())
                .evidenceNote(refundRequest.getEvidenceNote())
                .evidenceFiles(mapEvidenceFiles(refundRequest.getEvidenceFiles()))
                .status(refundRequest.getStatus())
                .adminNote(refundRequest.getAdminNote())
                .refundReference(refundRequest.getRefundReference())
                .reviewedBy(refundRequest.getReviewedBy() != null ? refundRequest.getReviewedBy().getId() : null)
                .reviewedByName(refundRequest.getReviewedBy() != null ? refundRequest.getReviewedBy().getFullName() : null)
                .reviewedAt(refundRequest.getReviewedAt())
                .processedAt(refundRequest.getProcessedAt())
                .createdAt(refundRequest.getCreatedAt())
                .sellerHandoverEvidence(evidenceByType.get(OrderEvidenceType.seller_handover))
                .buyerReceiptEvidence(evidenceByType.get(OrderEvidenceType.buyer_receipt))
                .paymentMethod(order != null ? order.getPaymentMethod() : null)
                .orderStatus(order != null ? order.getStatus() : null)
                .fundingStatus(order != null ? order.getFundingStatus() : null)
                .build();
    }
}
