package swp391.old_bicycle_project.service.impl;

import swp391.old_bicycle_project.config.NotificationEvent;
import swp391.old_bicycle_project.dto.request.ReportProcessDTO;
import swp391.old_bicycle_project.dto.request.ReportRequestDTO;
import swp391.old_bicycle_project.dto.response.ReportEvidenceFileResponseDTO;
import swp391.old_bicycle_project.dto.response.ReportResponseDTO;
import swp391.old_bicycle_project.entity.Product;
import swp391.old_bicycle_project.entity.Report;
import swp391.old_bicycle_project.entity.ReportFile;
import swp391.old_bicycle_project.entity.User;
import swp391.old_bicycle_project.entity.enums.AppRole;
import swp391.old_bicycle_project.entity.enums.NotificationType;
import swp391.old_bicycle_project.entity.enums.ProductStatus;
import swp391.old_bicycle_project.entity.enums.ReportStatus;
import swp391.old_bicycle_project.entity.enums.UserStatus;
import swp391.old_bicycle_project.exception.AppException;
import swp391.old_bicycle_project.exception.ErrorCode;
import swp391.old_bicycle_project.repository.ProductRepository;
import swp391.old_bicycle_project.repository.ReportRepository;
import swp391.old_bicycle_project.repository.UserRepository;
import swp391.old_bicycle_project.service.ReportService;
import swp391.old_bicycle_project.service.StorageService;
import swp391.old_bicycle_project.validation.MultipartFileValidationUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private static final Logger log = LoggerFactory.getLogger(ReportServiceImpl.class);

    private static final List<ReportStatus> OPEN_REPORT_STATUSES = List.of(
            ReportStatus.pending,
            ReportStatus.investigating
    );
    private static final Set<ReportStatus> TERMINAL_REPORT_STATUSES = EnumSet.of(
            ReportStatus.resolved_upheld,
            ReportStatus.resolved_dismissed
    );
    private static final int MAX_REPORT_EVIDENCE_FILES = 3;

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final StorageService storageService;
   
    @Override
    @Transactional
    // submitReport gồm check user tồn tại, check target tồn tại, check đã có report mở nào cho target chưa, tạo report, upload file (nếu có), publish notification cho admin
    public ReportResponseDTO submitReport(UUID reporterId, ReportRequestDTO requestDTO, List<MultipartFile> files) {
        // 1. Check user tồn tại
        User reporter = userRepository.findById(reporterId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        // 2. Check target tồn tại
        validateTargetExists(requestDTO.getTargetType(), requestDTO.getTargetId());
        // 3. Check đã có report mở nào cho target chưa
        if (reportRepository.existsByReporterIdAndTargetIdAndStatusIn(
                reporterId,
                requestDTO.getTargetId(),
                OPEN_REPORT_STATUSES
        )) {
            throw new AppException(ErrorCode.RECORD_ALREADY_EXISTS);
        }
        // 4. Tạo report
        Report report = reportRepository.save(Report.builder()
                .reporter(reporter)
                .targetId(requestDTO.getTargetId())
                .targetType(normalizeTargetType(requestDTO.getTargetType()))
                .reason(requestDTO.getReason())
                .description(trimToNull(requestDTO.getDescription()))
                .status(ReportStatus.pending)
                .build());
        // 5. Upload file (nếu có) và liên kết với report
        report = attachEvidenceFiles(report, files);
        publishPendingReportNotification(report);
        return mapToDTO(report);
    }

    @Override
    public Page<ReportResponseDTO> getAllReports(ReportStatus status, String targetType, Pageable pageable) {
        Specification<Report> specification = Specification.where(null);

        if (status != null) {
            specification = specification.and((root, query, cb) -> cb.equal(root.get("status"), status));
        }
        if (targetType != null && !targetType.isBlank()) {
            String normalizedTargetType = normalizeTargetType(targetType);
            specification = specification.and((root, query, cb) -> cb.equal(root.get("targetType"), normalizedTargetType));
        }

        return reportRepository.findAll(specification, pageable)
                .map(this::mapToDTO);
    }

    @Override
    public Page<ReportResponseDTO> getMyReports(UUID reporterId, Pageable pageable) {
        if (!userRepository.existsById(reporterId)) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }

        return reportRepository.findByReporterIdOrderByCreatedAtDesc(reporterId, pageable)
                .map(this::mapToDTO);
    }

    @Override
    @Transactional
    // processReport gồm check report tồn tại, check admin tồn tại, validate chuyển trạng thái,
    // cập nhật report, áp dụng xử lý (nếu upheld), publish notification cho các bên liên quan
    public ReportResponseDTO processReport(UUID reportId, ReportProcessDTO processDTO, UUID adminId) {
        // 1. Check report tồn tại
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new AppException(ErrorCode.RECORD_NOT_EXISTS));
        // 2. Check admin tồn tại
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // 3. Validate chuyển trạng thái hợp lệ
        validateProcessTransition(report.getStatus(), processDTO.getStatus());

        // 4. Cập nhật trạng thái + thông tin xử lý
        report.setStatus(processDTO.getStatus());
        report.setAdminNote(trimToNull(processDTO.getAdminNote()));
        report.setProcessedAt(LocalDateTime.now());
        report.setProcessedBy(admin);

        UUID affectedUserId = null;
        // 5. Nếu xác nhận vi phạm thì áp dụng xử lý theo target
        if (processDTO.getStatus() == ReportStatus.resolved_upheld) {
            affectedUserId = applySanctions(report.getTargetType(), report.getTargetId());
        }

        // 6. Lưu report + gửi thông báo
        report = reportRepository.save(report);
        publishReportNotifications(report, affectedUserId);
        return mapToDTO(report);
    }

    // validateTargetExists gồm normalize targetType, check target theo từng loại (USER/PRODUCT),
    // và ném lỗi nếu target không tồn tại hoặc loại target không hợp lệ.
    private void validateTargetExists(String targetType, UUID targetId) {
        // 1. Normalize loại target để so sánh ổn định
        String normalizedTargetType = normalizeTargetType(targetType);
        // 2. Nếu target là USER thì check user tồn tại
        if ("USER".equals(normalizedTargetType)) {
            if (!userRepository.existsById(targetId)) {
                throw new AppException(ErrorCode.USER_NOT_EXISTED);
            }
            return;
        }
        // 3. Nếu target là PRODUCT thì check product tồn tại
        if ("PRODUCT".equals(normalizedTargetType)) {
            if (!productRepository.existsById(targetId)) {
                throw new AppException(ErrorCode.PRODUCT_NOT_FOUND);
            }
            return;
        }
        // 4. TargetType không hợp lệ
        throw new AppException(ErrorCode.INVALID_KEY);
    }

    // validateProcessTransition gồm check null, chặn trạng thái terminal/lặp lại,
    // và chỉ cho phép các transition hợp lệ theo state machine.
    private void validateProcessTransition(ReportStatus currentStatus, ReportStatus nextStatus) {
        // 1. Check trạng thái đầu vào không được null
        if (currentStatus == null || nextStatus == null) {
            throw new AppException(ErrorCode.INVALID_REPORT_STATUS_TRANSITION);
        }

        // 2. Chặn chuyển từ trạng thái terminal hoặc chuyển sang chính nó
        if (TERMINAL_REPORT_STATUSES.contains(currentStatus) || currentStatus == nextStatus) {
            throw new AppException(ErrorCode.INVALID_REPORT_STATUS_TRANSITION);
        }

        // 3. Kiểm tra transition hợp lệ theo trạng thái hiện tại
        boolean isValidTransition = switch (currentStatus) {
            case pending -> nextStatus == ReportStatus.investigating
                    || nextStatus == ReportStatus.resolved_upheld
                    || nextStatus == ReportStatus.resolved_dismissed;
            case investigating -> nextStatus == ReportStatus.resolved_upheld
                    || nextStatus == ReportStatus.resolved_dismissed;
            default -> false;
        };

        // 4. Nếu không hợp lệ thì ném lỗi
        if (!isValidTransition) {
            throw new AppException(ErrorCode.INVALID_REPORT_STATUS_TRANSITION);
        }
    }

    // applySanctions gồm xử lý theo targetType: USER thì ban user, PRODUCT thì ẩn product,
    // trả về affectedUserId để gửi notification hậu xử lý.
    private UUID applySanctions(String targetType, UUID targetId) {
        try {
            // 1. Target USER -> ban user
            if ("USER".equalsIgnoreCase(targetType)) {
                User user = userRepository.findById(targetId)
                        .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
                user.setStatus(UserStatus.banned);
                userRepository.save(user);
                log.info("Banned user with ID: {}", targetId);
                return user.getId();
            }
            // 2. Target PRODUCT -> hidden product
            if ("PRODUCT".equalsIgnoreCase(targetType)) {
                Product product = productRepository.findById(targetId)
                        .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
                product.setStatus(ProductStatus.hidden);
                productRepository.save(product);
                log.info("Hidden product with ID: {}", targetId);
                return product.getSeller() != null ? product.getSeller().getId() : null;
            }
        } catch (AppException ex) {
            // 3. Lỗi nghiệp vụ thì ném lại nguyên trạng
            throw ex;
        } catch (Exception ex) {
            // 4. Lỗi hệ thống thì log để điều tra
            log.error("Failed to apply sanctions for targetType: {}, targetId: {}", targetType, targetId, ex);
        }
        // 5. Fallback khi targetType không hỗ trợ hoặc xử lý thất bại
        throw new AppException(ErrorCode.INVALID_STATUS);
    }

    // publishReportNotifications gồm gửi thông báo cho reporter và user bị ảnh hưởng (nếu có).
    private void publishReportNotifications(Report report, UUID affectedUserId) {
        // 1. Dựng metadata dùng chung cho notification
        String metadata = "{\"reportId\":\"" + report.getId() + "\",\"status\":\"" + report.getStatus() + "\"}";

        // 2. Gửi thông báo cập nhật trạng thái cho reporter
        eventPublisher.publishEvent(new NotificationEvent(
                this,
                report.getReporter().getId(),
                "Báo cáo đã được cập nhật",
                "Báo cáo của bạn hiện ở trạng thái " + mapStatusLabel(report.getStatus()) + ".",
                NotificationType.system,
                metadata
        ));

        // 3. Nếu có affectedUser thì gửi thêm thông báo xử lý vi phạm
        if (affectedUserId != null) {
            eventPublisher.publishEvent(new NotificationEvent(
                    this,
                    affectedUserId,
                    "Nội dung của bạn đã bị xử lý",
                    "Hệ thống đã áp dụng xử lý sau khi một báo cáo được xác nhận vi phạm.",
                    NotificationType.system,
                    metadata
            ));
        }
    }

    // publishPendingReportNotification gồm lấy danh sách admin và broadcast báo cáo mới cần xử lý.
    private void publishPendingReportNotification(Report report) {
        // 1. Dựng metadata cho thông báo
        String metadata = "{\"reportId\":\"" + report.getId() + "\",\"status\":\"" + report.getStatus() + "\"}";
        // 2. Lấy toàn bộ admin và gửi notification theo từng người
        userRepository.findByRole(AppRole.admin).stream()
                .map(User::getId)
                .distinct()
                .forEach(adminId -> eventPublisher.publishEvent(new NotificationEvent(
                        this,
                        adminId,
                        "Có báo cáo mới cần xử lý",
                        "Hệ thống vừa nhận một báo cáo mới cho " + report.getTargetType().toLowerCase() + ".",
                        NotificationType.system,
                        metadata
                )));
    }

    private Report attachEvidenceFiles(Report report, List<MultipartFile> files) {
        // 1. Normalize + validate danh sách file đầu vào
        List<MultipartFile> normalizedFiles = MultipartFileValidationUtils.normalizeFiles(files);
        validateFiles(normalizedFiles);

        // 2. Không có file thì trả report hiện tại
        if (normalizedFiles.isEmpty()) {
            return report;
        }

        List<String> uploadedUrls = new ArrayList<>();

        try {
            // 3. Upload từng file lên storage và gắn vào report
            for (int index = 0; index < normalizedFiles.size(); index++) {
                MultipartFile file = normalizedFiles.get(index);
                String fileUrl = storageService.uploadFile(file, buildReportEvidenceFolder(report.getId()));
                uploadedUrls.add(fileUrl);
                report.addEvidenceFile(ReportFile.builder()
                        .fileUrl(fileUrl)
                        .fileName(file.getOriginalFilename())
                        .contentType(file.getContentType())
                        .sortOrder(index)
                        .build());
            }

            // 4. Lưu report sau khi đã gắn danh sách evidence
            return reportRepository.save(report);
        } catch (RuntimeException exception) {
            // 5. Nếu lỗi thì rollback file đã upload (best effort) rồi ném lại lỗi
            uploadedUrls.forEach(storageService::deleteFile);
            throw exception;
        }
    }

    // validateFiles gồm validate loại file ảnh và số lượng file evidence theo rule hệ thống.
    private void validateFiles(List<MultipartFile> files) {
        MultipartFileValidationUtils.validateImageFiles(
                files,
                MAX_REPORT_EVIDENCE_FILES,
                ErrorCode.REPORT_EVIDENCE_LIMIT_EXCEEDED,
                ErrorCode.REPORT_EVIDENCE_IMAGE_ONLY
        );
    }

    private List<ReportEvidenceFileResponseDTO> mapEvidenceFiles(Collection<ReportFile> files) {
        if (files == null || files.isEmpty()) {
            return List.of();
        }

        return files.stream()
                .map(file -> ReportEvidenceFileResponseDTO.builder()
                        .id(file.getId())
                        .fileUrl(file.getFileUrl())
                        .fileName(file.getFileName())
                        .contentType(file.getContentType())
                        .sortOrder(file.getSortOrder())
                        .build())
                .toList();
    }

    private ReportResponseDTO mapToDTO(Report report) {
        return ReportResponseDTO.builder()
                .id(report.getId())
                .reporterId(report.getReporter().getId())
                .reporterName(report.getReporter().getFullName())
                .targetId(report.getTargetId())
                .targetType(report.getTargetType())
                .reason(report.getReason())
                .description(report.getDescription())
                .evidenceFiles(mapEvidenceFiles(report.getEvidenceFiles()))
                .status(report.getStatus())
                .adminNote(report.getAdminNote())
                .processedById(report.getProcessedBy() != null ? report.getProcessedBy().getId() : null)
                .processedByName(report.getProcessedBy() != null ? report.getProcessedBy().getFullName() : null)
                .createdAt(report.getCreatedAt())
                .processedAt(report.getProcessedAt())
                .build();
    }

    private String buildReportEvidenceFolder(UUID reportId) {
        return "reports/" + reportId;
    }

    private String normalizeTargetType(String targetType) {
        return targetType == null ? null : targetType.trim().toUpperCase();
    }

    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private String mapStatusLabel(ReportStatus status) {
        return switch (status) {
            case pending -> "chờ xử lý";
            case investigating -> "đang điều tra";
            case resolved_upheld -> "xác nhận vi phạm";
            case resolved_dismissed -> "bác bỏ";
        };
    }
}
