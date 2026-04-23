package swp391.old_bicycle_project.service;

import swp391.old_bicycle_project.config.NotificationEvent;
import swp391.old_bicycle_project.dto.product.ProductCreateRequest;
import swp391.old_bicycle_project.dto.product.ProductFilterRequest;
import swp391.old_bicycle_project.dto.product.ProductResponse;
import swp391.old_bicycle_project.dto.product.ProductUpdateRequest;
import swp391.old_bicycle_project.entity.Brand;
import swp391.old_bicycle_project.entity.BrakeType;
import swp391.old_bicycle_project.entity.Category;
import swp391.old_bicycle_project.entity.FrameMaterial;
import swp391.old_bicycle_project.entity.Groupset;
import swp391.old_bicycle_project.entity.Inspection;
import swp391.old_bicycle_project.entity.Product;
import swp391.old_bicycle_project.entity.ProductImage;
import swp391.old_bicycle_project.entity.User;
import swp391.old_bicycle_project.entity.enums.AppRole;
import swp391.old_bicycle_project.entity.enums.NotificationType;
import swp391.old_bicycle_project.entity.enums.OrderStatus;
import swp391.old_bicycle_project.entity.enums.ProductStatus;
import swp391.old_bicycle_project.exception.AppException;
import swp391.old_bicycle_project.exception.ErrorCode;
import swp391.old_bicycle_project.repository.BrandRepository;
import swp391.old_bicycle_project.repository.BrakeTypeRepository;
import swp391.old_bicycle_project.repository.CategoryRepository;
import swp391.old_bicycle_project.repository.FrameMaterialRepository;
import swp391.old_bicycle_project.repository.GroupsetRepository;
import swp391.old_bicycle_project.repository.InspectionRepository;
import swp391.old_bicycle_project.repository.OrderRepository;
import swp391.old_bicycle_project.repository.ProductImageRepository;
import swp391.old_bicycle_project.repository.ProductRepository;
import swp391.old_bicycle_project.repository.UserRepository;
import swp391.old_bicycle_project.repository.WishlistRepository;
import swp391.old_bicycle_project.specification.ProductSpecification;
import swp391.old_bicycle_project.validation.MultipartFileValidationUtils;
import swp391.old_bicycle_project.validation.PaginationValidationUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private static final List<ProductStatus> PUBLIC_VISIBLE_STATUSES = List.of(
            ProductStatus.active,
            ProductStatus.inspected_passed
    );
    private static final List<OrderStatus> OPEN_ORDER_STATUSES = List.of(
            OrderStatus.pending,
            OrderStatus.deposited,
            OrderStatus.awaiting_buyer_confirmation
    );
    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    private final OrderRepository orderRepository;
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;
    private final BrakeTypeRepository brakeTypeRepository;
    private final FrameMaterialRepository frameMaterialRepository;
    private final GroupsetRepository groupsetRepository;
    private final InspectionRepository inspectionRepository;
    private final WishlistRepository wishlistRepository;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final StorageService storageService;

    public Page<ProductResponse> searchProducts(ProductFilterRequest filter, int page, int size) {
        validatePriceRange(filter);
        Specification<Product> spec = ProductSpecification.fromFilter(filter);
        Sort sort = buildSort(filter);
        Pageable pageable = PaginationValidationUtils.createPageRequest(page, size, sort);

        return mapProductPage(productRepository.findAll(spec, pageable));
    }

    public ProductResponse getById(UUID id, User currentUser) {
        Product product = findActiveProductById(id);
        Inspection inspection = inspectionRepository.findByProductId(product.getId()).orElse(null);
        if (!PUBLIC_VISIBLE_STATUSES.contains(product.getStatus()) || !isInspectionCurrentlyValid(product, inspection)) {
            throw new AppException(ErrorCode.PRODUCT_NOT_FOUND);
        }

        List<ProductImage> productImages = productImageRepository.findByProductIdOrderByDisplayOrderAsc(product.getId());
        if ((productImages == null || productImages.isEmpty()) && product.getImages() != null) {
            productImages = product.getImages();
        }

        List<ProductResponse.ImageInfo> imageInfos = productImages.stream()
                .map(img -> ProductResponse.ImageInfo.builder()
                        .id(img.getId())
                        .url(img.getUrl())
                        .isPrimary(img.isPrimary())
                        .displayOrder(img.getDisplayOrder())
                        .build())
                .collect(Collectors.toList());

        User seller = product.getSeller();
        ProductResponse.SellerInfo sellerInfo = seller != null
                ? ProductResponse.SellerInfo.builder()
                .id(seller.getId())
                .firstName(seller.getFirstName())
                .lastName(seller.getLastName())
                .avatarUrl(seller.getAvatarUrl())
                .phone(seller.getPhone())
                .build()
                : null;

        boolean hasPendingOrder = currentUser != null && orderRepository.existsByBuyerIdAndProductIdAndStatusIn(
                currentUser.getId(),
                product.getId(),
                OPEN_ORDER_STATUSES
        );

        boolean isFavorite = currentUser != null && wishlistRepository.existsByUserIdAndProductId(
                currentUser.getId(),
                product.getId()
        );

        return buildProductResponse(
                product,
                inspection,
                hasExclusiveTransactionLock(product.getId()),
                hasSellerActionLock(product.getId()),
                hasPendingOrder,
                isFavorite,
                imageInfos,
                sellerInfo
        );
    }

    public ProductResponse getMineById(UUID id, User currentUser) {
        Product product = findActiveProductById(id);
        if (!product.getSeller().getId().equals(currentUser.getId())) {
            throw new AppException(ErrorCode.FORBIDDEN);
        }
        return toResponse(product);
    }

    public ProductResponse getAdminById(UUID id) {
        return toResponse(findActiveProductById(id));
    }

    @Transactional
    public ProductResponse create(ProductCreateRequest request, List<MultipartFile> images, User seller) {
        return create(request, images, List.of(), seller);
    }

    @Transactional
    public ProductResponse create(ProductCreateRequest request, List<MultipartFile> images, List<String> imageUrls, User seller) {
        List<MultipartFile> normalizedImages = MultipartFileValidationUtils.normalizeFiles(images);
        validateRequiredTechnicalFields(request.getFrameSize(), request.getWheelSize());
        validateMinimumImages(normalizedImages, imageUrls);

        BrakeType brakeType = brakeTypeRepository.findById(request.getBrakeTypeId())
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));
        FrameMaterial frameMaterial = frameMaterialRepository.findById(request.getFrameMaterialId())
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));

        Brand brand = request.getBrandId() != null
                ? brandRepository.findById(request.getBrandId()).orElse(null)
                : null;
        Category category = request.getCategoryId() != null
                ? categoryRepository.findById(request.getCategoryId()).orElse(null)
                : null;
        Groupset groupsetReference = resolveGroupsetReference(request.getGroupsetId(), request.getGroupset());

        Product product = Product.builder()
                .seller(seller)
                .title(request.getTitle())
                .description(request.getDescription())
                .price(request.getPrice())
                .originalPrice(request.getOriginalPrice())
                .brand(brand)
                .category(category)
                .brakeType(brakeType)
                .frameMaterial(frameMaterial)
                .groupsetReference(groupsetReference)
                .frameSize(request.getFrameSize())
                .wheelSize(request.getWheelSize())
                .groupset(resolveGroupsetDisplayValue(groupsetReference, request.getGroupset()))
                .condition(request.getCondition() != null ? request.getCondition() : swp391.old_bicycle_project.entity.enums.ConditionType.used)
                .province(request.getProvince())
                .district(request.getDistrict())
                .status(ProductStatus.pending_inspection)
                .build();

        productRepository.save(product);
        product.setExpiresAt(product.getCreatedAt().plusDays(30));
        product.setImages(saveProductImages(product, normalizedImages, imageUrls));
        createPendingInspection(product);
        productRepository.save(product);
        publishInspectionQueuedNotification(product);

        return toResponse(product);
    }

    @Transactional
    public ProductResponse update(UUID id, ProductUpdateRequest request, List<MultipartFile> newImages, User currentUser) {
        return update(id, request, newImages, List.of(), currentUser);
    }

    @Transactional
    public ProductResponse update(UUID id, ProductUpdateRequest request, List<MultipartFile> newImages, List<String> newImageUrls, User currentUser) {
        Product product = findActiveProductById(id);
        validateSellerCanModify(product, currentUser);
        List<MultipartFile> normalizedNewImages = MultipartFileValidationUtils.normalizeFiles(newImages);

        if (request.getTitle() != null) product.setTitle(request.getTitle());
        if (request.getDescription() != null) product.setDescription(request.getDescription());
        if (request.getPrice() != null) product.setPrice(request.getPrice());
        if (request.getOriginalPrice() != null) product.setOriginalPrice(request.getOriginalPrice());
        if (request.getCondition() != null) product.setCondition(request.getCondition());
        if (request.getProvince() != null) product.setProvince(request.getProvince());
        if (request.getDistrict() != null) product.setDistrict(request.getDistrict());
        if (request.getFrameSize() != null) product.setFrameSize(request.getFrameSize());
        if (request.getWheelSize() != null) product.setWheelSize(request.getWheelSize());
        if (request.getGroupsetId() != null || request.getGroupset() != null) {
            Groupset groupsetReference = resolveGroupsetReference(request.getGroupsetId(), request.getGroupset());
            product.setGroupsetReference(groupsetReference);
            product.setGroupset(resolveGroupsetDisplayValue(groupsetReference, request.getGroupset()));
        }

        validateRequiredTechnicalFields(product.getFrameSize(), product.getWheelSize());

        if (request.getBrandId() != null) {
            product.setBrand(brandRepository.findById(request.getBrandId()).orElse(null));
        }
        if (request.getCategoryId() != null) {
            product.setCategory(categoryRepository.findById(request.getCategoryId()).orElse(null));
        }
        if (request.getBrakeTypeId() != null) {
            product.setBrakeType(brakeTypeRepository.findById(request.getBrakeTypeId())
                    .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)));
        }
        if (request.getFrameMaterialId() != null) {
            product.setFrameMaterial(frameMaterialRepository.findById(request.getFrameMaterialId())
                    .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)));
        }

        if (!normalizedNewImages.isEmpty() || (newImageUrls != null && !newImageUrls.isEmpty())) {
            validateMinimumImages(normalizedNewImages, newImageUrls);
            removeStoredImages(product);
            List<ProductImage> uploadedImages = saveProductImages(product, normalizedNewImages, newImageUrls);
            product.getImages().clear();
            product.getImages().addAll(uploadedImages);
        }

        product.setStatus(ProductStatus.pending_inspection);
        product.setExpiresAt(LocalDateTime.now().plusDays(30));
        createPendingInspection(product);
        Product savedProduct = productRepository.save(product);
        publishInspectionQueuedNotification(savedProduct);

        return toResponse(savedProduct);
    }

    @Transactional
    public void delete(UUID id, User currentUser) {
        Product product = findActiveProductById(id);
        validateSellerCanModify(product, currentUser);

        removeStoredImages(product);
        product.setDeletedAt(LocalDateTime.now());
        product.setStatus(ProductStatus.hidden);
        invalidateInspection(product);
        productRepository.save(product);
    }

    @Transactional
    public ProductResponse hide(UUID id, User currentUser) {
        Product product = findActiveProductById(id);
        validateSellerCanHide(product, currentUser);

        product.setStatus(ProductStatus.hidden);
        return toResponse(productRepository.save(product));
    }

    @Transactional
    public ProductResponse show(UUID id, User currentUser) {
        Product product = findActiveProductById(id);
        validateSellerCanModify(product, currentUser);

        if (product.getStatus() != ProductStatus.hidden) {
            throw new AppException(ErrorCode.INVALID_STATUS);
        }

        product.setStatus(ProductStatus.pending_inspection);
        product.setExpiresAt(LocalDateTime.now().plusDays(30));
        createPendingInspection(product);
        Product savedProduct = productRepository.save(product);
        publishInspectionQueuedNotification(savedProduct);
        return toResponse(savedProduct);
    }

    public Page<ProductResponse> getMyProducts(User currentUser, int page, int size) {
        Pageable pageable = PaginationValidationUtils.createPageRequest(page, size, Sort.by("createdAt").descending());
        return mapProductPage(productRepository.findBySellerIdAndDeletedAtIsNull(currentUser.getId(), pageable));
    }

    public Page<ProductResponse> getAllForAdmin(ProductStatus status, int page, int size) {
        return getAllForAdmin(status, null, null, page, size);
    }

    public Page<ProductResponse> getAllForAdmin(ProductStatus status, UUID sellerId, String keyword, int page, int size) {
        Pageable pageable = PaginationValidationUtils.createPageRequest(page, size, Sort.by("createdAt").descending());
        Specification<Product> specification = ProductSpecification.fromAdminFilter(status, sellerId, keyword);
        return mapProductPage(productRepository.findAll(specification, pageable));
    }

    @Transactional
    public ProductResponse changeStatus(UUID id, ProductStatus newStatus) {
        Product product = findActiveProductById(id);
        if (product.getStatus() == ProductStatus.sold) {
            throw new AppException(ErrorCode.INVALID_STATUS);
        }

        if (newStatus == ProductStatus.hidden) {
            product.setStatus(ProductStatus.hidden);
            return toResponse(productRepository.save(product));
        }

        if (newStatus == ProductStatus.active) {
            Inspection inspection = inspectionRepository.findByProductId(product.getId()).orElse(null);
            if (!isInspectionCurrentlyValid(product, inspection)) {
                throw new AppException(ErrorCode.INVALID_STATUS);
            }
            product.setStatus(ProductStatus.active);
            return toResponse(productRepository.save(product));
        }

        if (newStatus != ProductStatus.pending) {
            throw new AppException(ErrorCode.INVALID_STATUS);
        }

        product.setStatus(newStatus);
        return toResponse(productRepository.save(product));
    }

    @Transactional
    public void hideAfterRefundCompletion(Product product) {
        if (product == null || product.getDeletedAt() != null) {
            return;
        }

        product.setStatus(ProductStatus.hidden);
        invalidateInspection(product);
        productRepository.save(product);
    }

    public ProductResponse toResponse(Product product) {
        List<ProductImage> productImages = productImageRepository.findByProductIdOrderByDisplayOrderAsc(product.getId());
        if ((productImages == null || productImages.isEmpty()) && product.getImages() != null) {
            productImages = product.getImages();
        }
        List<ProductResponse.ImageInfo> imageInfos = productImages.stream()
                .map(img -> ProductResponse.ImageInfo.builder()
                        .id(img.getId())
                        .url(img.getUrl())
                        .isPrimary(img.isPrimary())
                        .displayOrder(img.getDisplayOrder())
                        .build())
                .collect(Collectors.toList());

        User seller = product.getSeller();
        ProductResponse.SellerInfo sellerInfo = seller != null
                ? ProductResponse.SellerInfo.builder()
                .id(seller.getId())
                .firstName(seller.getFirstName())
                .lastName(seller.getLastName())
                .avatarUrl(seller.getAvatarUrl())
                .phone(seller.getPhone())
                .build()
                : null;

        Inspection inspection = inspectionRepository.findByProductId(product.getId()).orElse(null);
        return buildProductResponse(
                product,
                inspection,
                hasExclusiveTransactionLock(product.getId()),
                hasSellerActionLock(product.getId()),
                false,
                false,
                imageInfos,
                sellerInfo
        );
    }

    private Page<ProductResponse> mapProductPage(Page<Product> productsPage) {
        if (productsPage.isEmpty()) {
            return productsPage.map(this::toResponse);
        }

        List<Product> products = productsPage.getContent();
        List<UUID> productIds = products.stream()
                .map(Product::getId)
                .toList();

        Map<UUID, Inspection> inspectionsByProductId = inspectionRepository.findByProductIdIn(productIds).stream()
                .collect(Collectors.toMap(
                        inspection -> inspection.getProduct().getId(),
                        Function.identity(),
                        (left, right) -> left
                ));

        Map<UUID, List<ProductImage>> imagesByProductId = productImageRepository.findByProductIdInOrderByProductIdAscDisplayOrderAsc(productIds)
                .stream()
                .collect(Collectors.groupingBy(
                        image -> image.getProduct().getId(),
                        Collectors.toList()
                ));

        HashSet<UUID> lockedProductIds = new HashSet<>(
                orderRepository.findProductIdsWithExclusiveOrderLock(productIds)
        );
        HashSet<UUID> sellerActionLockedProductIds = new HashSet<>(
                orderRepository.findLockedProductIdsByProductIdsAndStatuses(productIds, OPEN_ORDER_STATUSES)
        );

        return productsPage.map(product -> {
            List<ProductResponse.ImageInfo> imageInfos = imagesByProductId.getOrDefault(product.getId(), List.of()).stream()
                    .map(img -> ProductResponse.ImageInfo.builder()
                            .id(img.getId())
                            .url(img.getUrl())
                            .isPrimary(img.isPrimary())
                            .displayOrder(img.getDisplayOrder())
                            .build())
                    .collect(Collectors.toList());

            User seller = product.getSeller();
            ProductResponse.SellerInfo sellerInfo = seller != null
                    ? ProductResponse.SellerInfo.builder()
                    .id(seller.getId())
                    .firstName(seller.getFirstName())
                    .lastName(seller.getLastName())
                    .avatarUrl(seller.getAvatarUrl())
                    .phone(seller.getPhone())
                    .build()
                    : null;

            return buildProductResponse(
                    product,
                    inspectionsByProductId.get(product.getId()),
                    lockedProductIds.contains(product.getId()),
                    sellerActionLockedProductIds.contains(product.getId()),
                    false,
                    false,
                    imageInfos,
                    sellerInfo
            );
        });
    }

    private ProductResponse buildProductResponse(
            Product product,
            Inspection inspection,
            boolean lockedForTransaction,
            boolean sellerActionLocked,
            boolean currentUserHasPendingOrder,
            boolean isFavorite,
            List<ProductResponse.ImageInfo> imageInfos,
            ProductResponse.SellerInfo sellerInfo
    ) {
        boolean verified = isInspectionCurrentlyValid(product, inspection);
        ProductResponse.InspectionInfo inspectionInfo = inspection != null
                ? ProductResponse.InspectionInfo.builder()
                .id(inspection.getId())
                .overallScore(inspection.getOverallScore())
                .passed(inspection.getPassed())
                .reportFileUrl(inspection.getReportFileUrl())
                .validUntil(inspection.getValidUntil())
                .createdAt(inspection.getCreatedAt())
                .build()
                : null;

        return ProductResponse.builder()
                .id(product.getId())
                .title(product.getTitle())
                .description(product.getDescription())
                .price(product.getPrice())
                .originalPrice(product.getOriginalPrice())
                .condition(product.getCondition())
                .status(product.getStatus())
                .province(product.getProvince())
                .district(product.getDistrict())
                .frameSize(product.getFrameSize())
                .wheelSize(product.getWheelSize())
                .groupsetId(product.getGroupsetReference() != null ? product.getGroupsetReference().getId() : null)
                .groupset(resolveGroupsetDisplayValue(product.getGroupsetReference(), product.getGroupset()))
                .createdAt(product.getCreatedAt())
                .expiresAt(product.getExpiresAt())
                .seller(sellerInfo)
                .brandName(product.getBrand() != null ? product.getBrand().getName() : null)
                .categoryId(product.getCategory() != null ? product.getCategory().getId() : null)
                .categoryName(product.getCategory() != null ? product.getCategory().getName() : null)
                .brakeTypeName(product.getBrakeType() != null ? product.getBrakeType().getName() : null)
                .frameMaterialName(product.getFrameMaterial() != null ? product.getFrameMaterial().getName() : null)
                .images(imageInfos)
                .isVerified(verified)
                .lockedForTransaction(lockedForTransaction)
                .sellerActionLocked(sellerActionLocked)
                .currentUserHasPendingOrder(currentUserHasPendingOrder)
                .isFavorite(isFavorite)
                .inspection(inspectionInfo)
                .build();
    }

    private Product findActiveProductById(UUID id) {
        return productRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
    }

    private Sort buildSort(ProductFilterRequest filter) {
        if (filter == null || filter.getSortBy() == null) {
            return Sort.by("createdAt").descending();
        }
        return switch (filter.getSortBy()) {
            case "price_asc" -> Sort.by("price").ascending();
            case "price_desc" -> Sort.by("price").descending();
            default -> Sort.by("createdAt").descending();
        };
    }

    private Groupset resolveGroupsetReference(UUID groupsetId, String groupsetName) {
        if (groupsetId != null) {
            return groupsetRepository.findById(groupsetId)
                    .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));
        }
        if (groupsetName == null || groupsetName.isBlank()) {
            return null;
        }
        return groupsetRepository.findByNameIgnoreCase(groupsetName.trim()).orElse(null);
    }

    private String resolveGroupsetDisplayValue(Groupset groupsetReference, String fallbackGroupset) {
        if (groupsetReference != null) {
            return groupsetReference.getName();
        }
        if (fallbackGroupset == null) {
            return null;
        }
        String normalizedGroupset = fallbackGroupset.trim();
        return normalizedGroupset.isEmpty() ? null : normalizedGroupset;
    }

    private void validateRequiredTechnicalFields(String frameSize, String wheelSize) {
        if (frameSize == null || frameSize.isBlank() || wheelSize == null || wheelSize.isBlank()) {
            throw new AppException(ErrorCode.PRODUCT_TECHNICAL_FIELDS_REQUIRED);
        }
    }

    private void validateSellerCanHide(Product product, User currentUser) {
        if (!product.getSeller().getId().equals(currentUser.getId())) {
            throw new AppException(ErrorCode.FORBIDDEN);
        }
        if (product.getStatus() == ProductStatus.sold || product.getStatus() == ProductStatus.hidden) {
            throw new AppException(ErrorCode.INVALID_STATUS);
        }
        if (orderRepository.existsByProductIdAndStatusIn(product.getId(), OPEN_ORDER_STATUSES)) {
            throw new AppException(ErrorCode.INVALID_STATUS);
        }
    }

    private void validateSellerCanModify(Product product, User currentUser) {
        if (!product.getSeller().getId().equals(currentUser.getId())) {
            throw new AppException(ErrorCode.FORBIDDEN);
        }
        if (product.getStatus() == ProductStatus.sold) {
            throw new AppException(ErrorCode.INVALID_STATUS);
        }
        if (orderRepository.existsByProductIdAndStatusIn(product.getId(), OPEN_ORDER_STATUSES)) {
            throw new AppException(ErrorCode.INVALID_STATUS);
        }

        // Prevent modification if inspected
        if (product.getStatus() == ProductStatus.inspected_passed || product.getStatus() == ProductStatus.inspected_failed) {
            throw new AppException(ErrorCode.PRODUCT_ALREADY_INSPECTED);
        }
    }

    private void validateMinimumImages(List<MultipartFile> images, List<String> imageUrls) {
        List<MultipartFile> normalizedImages = images != null ? images : List.of();
        List<String> normalizedImageUrls = normalizeImageUrls(imageUrls);

        if (!normalizedImages.isEmpty()) {
            MultipartFileValidationUtils.validateRequiredImages(
                    normalizedImages,
                    3,
                    ErrorCode.PRODUCT_MINIMUM_IMAGES_REQUIRED,
                    ErrorCode.PRODUCT_IMAGE_INVALID
            );
            return;
        }

        if (normalizedImageUrls.size() < 3) {
            throw new AppException(ErrorCode.PRODUCT_MINIMUM_IMAGES_REQUIRED);
        }
    }

    private List<ProductImage> uploadImages(Product product, List<MultipartFile> images) {
        List<ProductImage> productImages = new ArrayList<>();
        int displayOrder = 0;
        for (MultipartFile file : images) {
            if (file == null || file.isEmpty()) {
                continue;
            }
            String url = storageService.uploadFile(file, "products/" + product.getId());
            productImages.add(ProductImage.builder()
                    .product(product)
                    .url(url)
                    .isPrimary(displayOrder == 0)
                    .displayOrder(displayOrder)
                    .build());
            displayOrder++;
        }
        productImageRepository.saveAll(productImages);
        return productImages;
    }

    private List<ProductImage> saveProductImages(Product product, List<MultipartFile> images, List<String> imageUrls) {
        List<MultipartFile> normalizedImages = images != null ? images : List.of();
        if (!normalizedImages.isEmpty()) {
            return uploadImages(product, normalizedImages);
        }

        List<String> normalizedImageUrls = normalizeImageUrls(imageUrls);
        List<ProductImage> productImages = new ArrayList<>();
        for (int displayOrder = 0; displayOrder < normalizedImageUrls.size(); displayOrder++) {
            productImages.add(ProductImage.builder()
                    .product(product)
                    .url(normalizedImageUrls.get(displayOrder))
                    .isPrimary(displayOrder == 0)
                    .displayOrder(displayOrder)
                    .build());
        }
        productImageRepository.saveAll(productImages);
        return productImages;
    }

    private List<String> normalizeImageUrls(List<String> imageUrls) {
        if (imageUrls == null || imageUrls.isEmpty()) {
            return List.of();
        }

        return imageUrls.stream()
                .filter(url -> url != null && !url.isBlank())
                .map(String::trim)
                .toList();
    }

    private void validatePriceRange(ProductFilterRequest filter) {
        if (filter == null) {
            return;
        }

        if (filter.getMinPrice() != null && filter.getMinPrice().compareTo(java.math.BigDecimal.ZERO) < 0) {
            throw new AppException(ErrorCode.INVALID_PRICE_RANGE);
        }
        if (filter.getMaxPrice() != null && filter.getMaxPrice().compareTo(java.math.BigDecimal.ZERO) < 0) {
            throw new AppException(ErrorCode.INVALID_PRICE_RANGE);
        }
        if (filter.getMinPrice() != null
                && filter.getMaxPrice() != null
                && filter.getMinPrice().compareTo(filter.getMaxPrice()) > 0) {
            throw new AppException(ErrorCode.INVALID_PRICE_RANGE);
        }
    }

    private void removeStoredImages(Product product) {
        if (product.getImages() == null || product.getImages().isEmpty()) {
            return;
        }

        product.getImages().forEach(img -> storageService.deleteFile(img.getUrl()));
        productImageRepository.deleteAllByProductId(product.getId());
        product.getImages().clear();
    }

    private void invalidateInspection(Product product) {
        inspectionRepository.findByProductId(product.getId()).ifPresent(inspection -> {
            inspection.setInspector(null);
            inspection.setOverallScore(null);
            inspection.setFrameScore(null);
            inspection.setForkScore(null);
            inspection.setBrakesScore(null);
            inspection.setDrivetrainScore(null);
            inspection.setWheelsScore(null);
            inspection.setWearPercentage(null);
            inspection.setExpertNotes(null);
            inspection.setPassed(false);
            inspection.setReportFileUrl(null);
            inspection.setValidUntil(null); // null = no valid inspection, consistent with createPendingInspection
            inspectionRepository.save(inspection);
        });
    }

    private void createPendingInspection(Product product) {
        Inspection inspection = inspectionRepository.findByProductId(product.getId())
                .orElseGet(() -> Inspection.builder()
                        .product(product)
                        .build());

        inspection.setInspector(null);
        inspection.setOverallScore(null);
        inspection.setFrameScore(null);
        inspection.setForkScore(null);
        inspection.setBrakesScore(null);
        inspection.setDrivetrainScore(null);
        inspection.setWheelsScore(null);
        inspection.setWearPercentage(null);
        inspection.setExpertNotes(null);
        inspection.setPassed(false);
        inspection.setReportFileUrl(null);
        inspection.setValidUntil(null);
        inspectionRepository.save(inspection);
    }

    private boolean isInspectionCurrentlyValid(Product product, Inspection inspection) {
        return inspection != null
                && Boolean.TRUE.equals(inspection.getPassed())
                && inspection.getValidUntil() != null
                && inspection.getValidUntil().isAfter(LocalDateTime.now())
                && product.getDeletedAt() == null
                && product.getStatus() != ProductStatus.sold
                && product.getStatus() != ProductStatus.hidden
                && product.getStatus() != ProductStatus.pending;
    }

    private boolean hasExclusiveTransactionLock(UUID productId) {
        return orderRepository.existsExclusiveOrderLockByProductId(productId);
    }

    private boolean hasSellerActionLock(UUID productId) {
        return orderRepository.existsByProductIdAndStatusIn(productId, OPEN_ORDER_STATUSES);
    }

    private void publishAdminModerationNotification(Product product, String actionDescription) {
        String productTitle = product.getTitle() != null ? product.getTitle() : "tin đăng mới";
        String metadata = "{\"productId\":\"" + product.getId() + "\"}";

        userRepository.findByRole(AppRole.admin).stream()
                .map(User::getId)
                .distinct()
                .forEach(adminId -> eventPublisher.publishEvent(new NotificationEvent(
                        this,
                        adminId,
                        "Có tin đăng chờ kiểm duyệt",
                        "Tin \"" + productTitle + "\" " + actionDescription + ". Vui lòng kiểm tra và quyết định bước tiếp theo.",
                        NotificationType.system,
                        metadata
                )));
    }

    private void publishInspectionQueuedNotification(Product product) {
        String metadata = "{\"productId\":\"" + product.getId() + "\"}";

        eventPublisher.publishEvent(new NotificationEvent(
                this,
                product.getSeller().getId(),
                "Tin đăng đã vào hàng chờ kiểm định",
                "Tin \"" + product.getTitle()
                        + "\" đã được đưa thẳng vào hàng chờ inspector. Tin chỉ hiển thị công khai sau khi kiểm định đạt.",
                NotificationType.inspection,
                metadata
        ));

        userRepository.findByRole(AppRole.inspector).stream()
                .map(User::getId)
                .distinct()
                .forEach(inspectorId -> eventPublisher.publishEvent(new NotificationEvent(
                        this,
                        inspectorId,
                        "Có tin đăng mới cần kiểm định",
                        "Tin \"" + product.getTitle() + "\" đang chờ inspector xử lý kiểm định.",
                        NotificationType.inspection,
                        metadata
                )));
    }
}
