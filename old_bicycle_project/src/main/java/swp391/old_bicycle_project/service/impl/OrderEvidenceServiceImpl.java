package swp391.old_bicycle_project.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import swp391.old_bicycle_project.entity.Order;
import swp391.old_bicycle_project.entity.OrderEvidenceFile;
import swp391.old_bicycle_project.entity.OrderEvidenceSubmission;
import swp391.old_bicycle_project.entity.User;
import swp391.old_bicycle_project.entity.enums.OrderEvidenceType;
import swp391.old_bicycle_project.exception.AppException;
import swp391.old_bicycle_project.exception.ErrorCode;
import swp391.old_bicycle_project.repository.OrderEvidenceSubmissionRepository;
import swp391.old_bicycle_project.service.OrderEvidenceService;
import swp391.old_bicycle_project.service.StorageService;
import swp391.old_bicycle_project.validation.MultipartFileValidationUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderEvidenceServiceImpl implements OrderEvidenceService {

    private static final int MAX_EVIDENCE_FILES = 3;

    private final OrderEvidenceSubmissionRepository orderEvidenceSubmissionRepository;
    private final StorageService storageService;

    @Override
    @Transactional
    public OrderEvidenceSubmission createSellerHandoverEvidence(
            Order order,
            User submittedBy,
            String note,
            List<MultipartFile> files
    ) {
        return createSubmission(order, submittedBy, OrderEvidenceType.seller_handover, note, files, true, false);
    }

    @Override
    @Transactional
    public OrderEvidenceSubmission createBuyerReceiptEvidence(
            Order order,
            User submittedBy,
            String note,
            List<MultipartFile> files
    ) {
        return createSubmission(order, submittedBy, OrderEvidenceType.buyer_receipt, note, files, false, true);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<UUID, Map<OrderEvidenceType, OrderEvidenceSubmission>> getEvidenceByOrderIds(Collection<UUID> orderIds) {
        if (orderIds == null || orderIds.isEmpty()) {
            return Collections.emptyMap();
        }

        List<OrderEvidenceSubmission> submissions = orderEvidenceSubmissionRepository.findDetailedByOrderIds(orderIds);
        Map<UUID, Map<OrderEvidenceType, OrderEvidenceSubmission>> result = new LinkedHashMap<>();

        for (OrderEvidenceSubmission submission : submissions) {
            result.computeIfAbsent(submission.getOrder().getId(), ignored -> new EnumMap<>(OrderEvidenceType.class))
                    .put(submission.getEvidenceType(), submission);
        }

        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Map<OrderEvidenceType, OrderEvidenceSubmission> getEvidenceByOrderId(UUID orderId) {
        if (orderId == null) {
            return Collections.emptyMap();
        }

        return getEvidenceByOrderIds(List.of(orderId)).getOrDefault(orderId, Collections.emptyMap());
    }

    private OrderEvidenceSubmission createSubmission(
            Order order,
            User submittedBy,
            OrderEvidenceType evidenceType,
            String note,
            List<MultipartFile> files,
            boolean requireFiles,
            boolean skipWhenEmpty
    ) {
        List<MultipartFile> normalizedFiles = MultipartFileValidationUtils.normalizeFiles(files);
        String normalizedNote = normalizeNote(note);

        if (skipWhenEmpty && normalizedFiles.isEmpty() && normalizedNote == null) {
            return null;
        }

        validateFiles(normalizedFiles, requireFiles);

        if (orderEvidenceSubmissionRepository.existsByOrderIdAndEvidenceType(order.getId(), evidenceType)) {
            throw new AppException(ErrorCode.RECORD_ALREADY_EXISTS);
        }

        OrderEvidenceSubmission submission = OrderEvidenceSubmission.builder()
                .order(order)
                .submittedByUser(submittedBy)
                .submittedByRole(submittedBy.getRole())
                .evidenceType(evidenceType)
                .note(normalizedNote)
                .build();

        List<String> uploadedUrls = new ArrayList<>();

        try {
            for (int index = 0; index < normalizedFiles.size(); index++) {
                MultipartFile file = normalizedFiles.get(index);
                String fileUrl = storageService.uploadFile(file, buildFolder(order.getId(), evidenceType));
                uploadedUrls.add(fileUrl);
                submission.addFile(OrderEvidenceFile.builder()
                        .fileUrl(fileUrl)
                        .fileName(file.getOriginalFilename())
                        .contentType(file.getContentType())
                        .sortOrder(index)
                        .build());
            }

            return orderEvidenceSubmissionRepository.save(submission);
        } catch (RuntimeException exception) {
            uploadedUrls.forEach(storageService::deleteFile);
            throw exception;
        }
    }

    private String normalizeNote(String note) {
        if (note == null) {
            return null;
        }

        String trimmed = note.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private void validateFiles(List<MultipartFile> files, boolean requireFiles) {
        if (requireFiles && files.isEmpty()) {
            throw new AppException(ErrorCode.ORDER_EVIDENCE_REQUIRED);
        }

        MultipartFileValidationUtils.validateImageFiles(
                files,
                MAX_EVIDENCE_FILES,
                ErrorCode.ORDER_EVIDENCE_LIMIT_EXCEEDED,
                ErrorCode.ORDER_EVIDENCE_IMAGE_ONLY
        );
    }

    private String buildFolder(UUID orderId, OrderEvidenceType evidenceType) {
        return "orders/" + orderId + "/" + evidenceType.name();
    }
}
