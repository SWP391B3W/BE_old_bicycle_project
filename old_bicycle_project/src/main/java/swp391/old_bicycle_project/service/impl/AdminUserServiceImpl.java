package swp391.old_bicycle_project.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swp391.old_bicycle_project.dto.response.AdminUserActivityResponseDTO;
import swp391.old_bicycle_project.dto.response.AdminUserResponseDTO;
import swp391.old_bicycle_project.entity.Order;
import swp391.old_bicycle_project.entity.Product;
import swp391.old_bicycle_project.entity.User;
import swp391.old_bicycle_project.entity.enums.AppRole;
import swp391.old_bicycle_project.entity.enums.UserStatus;
import swp391.old_bicycle_project.exception.AppException;
import swp391.old_bicycle_project.exception.ErrorCode;
import swp391.old_bicycle_project.repository.OrderRepository;
import swp391.old_bicycle_project.repository.ProductRepository;
import swp391.old_bicycle_project.repository.UserRepository;
import swp391.old_bicycle_project.service.AdminUserService;
import swp391.old_bicycle_project.service.PasswordPolicyValidator;
import swp391.old_bicycle_project.service.RefreshTokenService;
import swp391.old_bicycle_project.specification.UserSpecification;
import swp391.old_bicycle_project.validation.PaginationValidationUtils;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminUserServiceImpl implements AdminUserService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final PasswordPolicyValidator passwordPolicyValidator;
    private final RefreshTokenService refreshTokenService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Page<AdminUserResponseDTO> getAllUsers(
            String keyword,
            AppRole role,
            UserStatus status,
            Boolean verified,
            int page,
            int size
    ) {
        Pageable pageable = PaginationValidationUtils.createPageRequest(page, size, Sort.by("createdAt").descending());
        return userRepository.findAll(
                UserSpecification.fromAdminFilter(keyword, role, status, verified),
                pageable
        ).map(this::toResponse);
    }

    @Override
    public AdminUserResponseDTO getUserById(UUID userId) {
        return toResponse(getRequiredUser(userId));
    }

    @Override
    @Transactional
    public AdminUserResponseDTO updateUserStatus(UUID userId, UserStatus status, UUID adminId) {
        if (adminId != null && adminId.equals(userId)) {
            throw new AppException(ErrorCode.SELF_STATUS_CHANGE_NOT_ALLOWED);
        }

        User user = getRequiredUser(userId);
        user.setStatus(status);
        return toResponse(userRepository.save(user));
    }

    @Override
    @Transactional
    public String resetUserPassword(UUID userId, String newPassword) {
        passwordPolicyValidator.validate(newPassword);

        User user = getRequiredUser(userId);
        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        refreshTokenService.deleteAllByUser(user);

        return "Password reset successfully. Existing refresh tokens were revoked.";
    }

    @Override
    public AdminUserActivityResponseDTO getUserActivity(UUID userId) {
        User user = getRequiredUser(userId);

        Pageable topFive = PaginationValidationUtils.createPageRequest(0, 5, Sort.by("createdAt").descending());
        List<Product> recentProducts = productRepository.findBySellerIdOrderByCreatedAtDesc(userId, topFive).getContent();
        List<Order> recentOrders = orderRepository.findByBuyerIdOrSellerIdOrderByCreatedAtDesc(userId, userId)
                .stream()
                .limit(5)
                .toList();

        return AdminUserActivityResponseDTO.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .role(user.getRole())
                .status(user.getStatus())
                .verified(user.isVerified())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .totalProducts(productRepository.countBySellerId(userId))
                .totalOrdersAsBuyer(orderRepository.countByBuyerId(userId))
                .totalOrdersAsSeller(orderRepository.countBySellerId(userId))
                .totalReportsSubmitted(0)
                .totalWishlistItems(0)
                .totalConversations(0)
                .unreadNotifications(0)
                .recentProducts(recentProducts.stream().map(this::toRecentProductItem).toList())
                .recentOrders(recentOrders.stream().map(order -> toRecentOrderItem(order, userId)).toList())
                .recentReports(List.of())
                .recentNotifications(List.of())
                .recentWishlistItems(List.of())
                .build();
    }

    private User getRequiredUser(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    }

    private AdminUserResponseDTO toResponse(User user) {
        return AdminUserResponseDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .fullName(user.getFullName())
                .phone(user.getPhone())
                .avatarUrl(user.getAvatarUrl())
                .defaultAddress(user.getDefaultAddress())
                .role(user.getRole())
                .status(user.getStatus())
                .verified(user.isVerified())
                .averageRating(user.getAverageRating())
                .totalReviews(user.getTotalReviews())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    private AdminUserActivityResponseDTO.RecentProductItem toRecentProductItem(Product product) {
        return AdminUserActivityResponseDTO.RecentProductItem.builder()
                .productId(product.getId())
                .title(product.getTitle())
                .price(product.getPrice())
                .status(product.getStatus())
                .createdAt(product.getCreatedAt())
                .build();
    }

    private AdminUserActivityResponseDTO.RecentOrderItem toRecentOrderItem(Order order, UUID userId) {
        String involvement = order.getBuyer() != null && userId.equals(order.getBuyer().getId()) ? "buyer" : "seller";
        return AdminUserActivityResponseDTO.RecentOrderItem.builder()
                .orderId(order.getId())
                .productId(order.getProduct() != null ? order.getProduct().getId() : null)
                .productTitle(order.getProduct() != null ? order.getProduct().getTitle() : null)
                .status(order.getStatus())
                .paymentMethod(order.getPaymentMethod())
                .totalAmount(order.getTotalAmount())
                .involvement(involvement)
                .createdAt(order.getCreatedAt())
                .build();
    }
}
