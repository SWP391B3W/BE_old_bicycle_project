package swp391.old_bicycle_project.controller;

import swp391.old_bicycle_project.dto.product.ProductCreateRequest;
import swp391.old_bicycle_project.dto.product.ProductFilterRequest;
import swp391.old_bicycle_project.dto.product.ProductResponse;
import swp391.old_bicycle_project.dto.product.ProductUpdateRequest;
import swp391.old_bicycle_project.dto.response.ApiResponse;
import swp391.old_bicycle_project.entity.User;
import swp391.old_bicycle_project.entity.enums.ConditionType;
import swp391.old_bicycle_project.exception.AppException;
import swp391.old_bicycle_project.exception.ErrorCode;
import swp391.old_bicycle_project.service.ProductService;
import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final Validator validator;

    @GetMapping
    @Operation(
            summary = "Lấy danh sách xe công khai",
            description = "Để trống toàn bộ bộ lọc nếu muốn lấy tất cả tin đang hiển thị cho người mua. "
                    + "Chỉ nhập các query param thực sự cần lọc, ví dụ keyword hoặc categoryId."
    )
    public ApiResponse<Page<ProductResponse>> searchProducts(
            @Parameter(description = "Từ khóa tìm trong tiêu đề xe")
            @RequestParam(required = false) String keyword,
            @Parameter(description = "ID thương hiệu")
            @RequestParam(required = false) UUID brandId,
            @Parameter(description = "ID danh mục")
            @RequestParam(required = false) UUID categoryId,
            @Parameter(description = "ID loại phanh")
            @RequestParam(required = false) UUID brakeTypeId,
            @Parameter(description = "ID chất liệu khung")
            @RequestParam(required = false) UUID frameMaterialId,
            @Parameter(description = "Tình trạng xe")
            @RequestParam(required = false) ConditionType condition,
            @Parameter(description = "Kích thước khung")
            @RequestParam(required = false) String frameSize,
            @Parameter(description = "Kích thước bánh")
            @RequestParam(required = false) String wheelSize,
            @Parameter(description = "Nhóm truyền động")
            @RequestParam(required = false) String groupset,
            @Parameter(description = "ID groupset")
            @RequestParam(required = false) UUID groupsetId,
            @Parameter(description = "Giá tối thiểu")
            @RequestParam(required = false) BigDecimal minPrice,
            @Parameter(description = "Giá tối đa")
            @RequestParam(required = false) BigDecimal maxPrice,
            @Parameter(description = "Tỉnh hoặc thành phố")
            @RequestParam(required = false) String province,
            @Parameter(description = "Chỉ lấy xe có inspection hợp lệ")
            @RequestParam(required = false) Boolean hasInspection,
            @Parameter(description = "Cách sắp xếp")
            @RequestParam(required = false) String sortBy,
            @Parameter(description = "Trang bắt đầu từ 0", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Số lượng phần tử mỗi trang", example = "12")
            @RequestParam(defaultValue = "12") int size
    ) {
        ProductFilterRequest filter = new ProductFilterRequest();
        filter.setKeyword(keyword);
        filter.setBrandId(brandId);
        filter.setCategoryId(categoryId);
        filter.setBrakeTypeId(brakeTypeId);
        filter.setFrameMaterialId(frameMaterialId);
        filter.setCondition(condition);
        filter.setFrameSize(frameSize);
        filter.setWheelSize(wheelSize);
        filter.setGroupset(groupset);
        filter.setGroupsetId(groupsetId);
        filter.setMinPrice(minPrice);
        filter.setMaxPrice(maxPrice);
        filter.setProvince(province);
        filter.setHasInspection(hasInspection);
        filter.setSortBy(sortBy);

        return ApiResponse.<Page<ProductResponse>>builder()
                .result(productService.searchProducts(filter, page, size))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<ProductResponse> getProduct(
            @PathVariable UUID id,
            @AuthenticationPrincipal User currentUser
    ) {
        return ApiResponse.<ProductResponse>builder()
                .result(productService.getById(id, currentUser))
                .build();
    }

    @GetMapping("/my/{id}")
    @PreAuthorize("hasRole('SELLER')")
    public ApiResponse<ProductResponse> getMyProductById(
            @PathVariable UUID id,
            @AuthenticationPrincipal User currentUser
    ) {
        return ApiResponse.<ProductResponse>builder()
                .result(productService.getMineById(id, currentUser))
                .build();
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('SELLER')")
    public ApiResponse<ProductResponse> createProduct(
            @ModelAttribute ProductCreateRequest request,
            @RequestPart(value = "request", required = false) JsonNode requestPayload,
            @RequestPart(value = "images", required = false) List<MultipartFile> images,
            @RequestPart(value = "images[]", required = false) List<MultipartFile> imagesArray,
            @AuthenticationPrincipal User currentUser
    ) {
        List<MultipartFile> mergedImages = mergeFiles(images, imagesArray);
        ProductCreateRequest resolvedRequest = requestPayload != null && !requestPayload.isNull()
                ? normalizeCreateRequest(requestPayload)
                : request;
        validateRequest(resolvedRequest);
        return ApiResponse.<ProductResponse>builder()
                .result(productService.create(resolvedRequest, mergedImages, currentUser))
                .build();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('SELLER')")
    public ApiResponse<ProductResponse> createProductJson(
                        @RequestBody JsonNode payload,
            @AuthenticationPrincipal User currentUser
    ) {
                ProductCreateRequest request = normalizeCreateRequest(payload);
                validateRequest(request);
                List<String> imageUrls = extractImageUrls(payload);
        return ApiResponse.<ProductResponse>builder()
                .result(productService.create(request, List.of(), imageUrls, currentUser))
                .build();
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('SELLER')")
    public ApiResponse<ProductResponse> updateProduct(
            @PathVariable UUID id,
            @ModelAttribute ProductUpdateRequest request,
            @RequestPart(value = "request", required = false) JsonNode requestPayload,
            @RequestPart(value = "images", required = false) List<MultipartFile> newImages,
            @RequestPart(value = "images[]", required = false) List<MultipartFile> newImagesArray,
            @AuthenticationPrincipal User currentUser
    ) {
        List<MultipartFile> mergedImages = mergeFiles(newImages, newImagesArray);
        ProductUpdateRequest resolvedRequest = requestPayload != null && !requestPayload.isNull()
                ? normalizeUpdateRequest(requestPayload)
                : request;
        validateRequest(resolvedRequest);
        return ApiResponse.<ProductResponse>builder()
                .result(productService.update(id, resolvedRequest, mergedImages, currentUser))
                .build();
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('SELLER')")
    public ApiResponse<ProductResponse> updateProductJson(
            @PathVariable UUID id,
                        @RequestBody JsonNode payload,
            @AuthenticationPrincipal User currentUser
    ) {
                ProductUpdateRequest request = normalizeUpdateRequest(payload);
                validateRequest(request);
                List<String> imageUrls = extractImageUrls(payload);
        return ApiResponse.<ProductResponse>builder()
                .result(productService.update(id, request, List.of(), imageUrls, currentUser))
                .build();
    }

        private ProductCreateRequest normalizeCreateRequest(JsonNode payload) {
                if (payload == null || payload.isNull()) {
                        throw new AppException(ErrorCode.INVALID_REQUEST_BODY);
                }

                JsonNode base = pickObjectNode(payload,
                                "request", "productCreateRequest", "createProductRequest", "product", "payload", "data");
                JsonNode basicInfo = pickObjectNode(payload, "basicInfo", "basic_info");
                JsonNode technicalSpecs = pickObjectNode(payload, "technicalSpecs", "technical_specs");
                JsonNode priceLocation = pickObjectNode(payload, "priceLocation", "price_location", "pricing", "location");

                ProductCreateRequest request = new ProductCreateRequest();
                request.setTitle(firstText(List.of(base, basicInfo, payload), "title"));
                request.setDescription(firstText(List.of(base, basicInfo, payload), "description"));
                request.setPrice(firstDecimal(List.of(base, priceLocation, payload), "price", "price_amount"));
                request.setOriginalPrice(firstDecimal(List.of(base, priceLocation, payload), "originalPrice", "original_price"));
                request.setBrakeTypeId(firstUuid(List.of(base, technicalSpecs, payload), "brakeTypeId", "brake_type_id"));
                request.setFrameMaterialId(firstUuid(List.of(base, technicalSpecs, payload), "frameMaterialId", "frame_material_id"));
                request.setBrandId(firstUuid(List.of(base, basicInfo, payload), "brandId", "brand_id"));
                request.setCategoryId(firstUuid(List.of(base, basicInfo, payload), "categoryId", "category_id"));
                request.setFrameSize(firstText(List.of(base, technicalSpecs, payload), "frameSize", "frame_size"));
                request.setWheelSize(firstText(List.of(base, technicalSpecs, payload), "wheelSize", "wheel_size"));
                request.setGroupsetId(firstUuid(List.of(base, technicalSpecs, payload), "groupsetId", "groupset_id"));
                request.setGroupset(firstText(List.of(base, technicalSpecs, payload), "groupset"));
                request.setProvince(firstText(List.of(base, priceLocation, payload), "province"));
                request.setDistrict(firstText(List.of(base, priceLocation, payload), "district"));
                request.setCondition(parseCondition(firstText(List.of(base, technicalSpecs, payload), "condition")));

                if (isBlank(request.getTitle())
                                || request.getPrice() == null
                                || request.getBrakeTypeId() == null
                                || request.getFrameMaterialId() == null
                                || isBlank(request.getFrameSize())
                                || isBlank(request.getWheelSize())
                                || isBlank(request.getProvince())) {
                        throw new AppException(ErrorCode.INVALID_REQUEST_BODY);
                }

                return request;
        }

        private ProductUpdateRequest normalizeUpdateRequest(JsonNode payload) {
                if (payload == null || payload.isNull()) {
                        throw new AppException(ErrorCode.INVALID_REQUEST_BODY);
                }

                JsonNode base = pickObjectNode(payload,
                                "request", "productUpdateRequest", "updateProductRequest", "product", "payload", "data");
                JsonNode basicInfo = pickObjectNode(payload, "basicInfo", "basic_info");
                JsonNode technicalSpecs = pickObjectNode(payload, "technicalSpecs", "technical_specs");
                JsonNode priceLocation = pickObjectNode(payload, "priceLocation", "price_location", "pricing", "location");

                ProductUpdateRequest request = new ProductUpdateRequest();
                request.setTitle(firstText(List.of(base, basicInfo, payload), "title"));
                request.setDescription(firstText(List.of(base, basicInfo, payload), "description"));
                request.setPrice(firstDecimal(List.of(base, priceLocation, payload), "price", "price_amount"));
                request.setOriginalPrice(firstDecimal(List.of(base, priceLocation, payload), "originalPrice", "original_price"));
                request.setBrakeTypeId(firstUuid(List.of(base, technicalSpecs, payload), "brakeTypeId", "brake_type_id"));
                request.setFrameMaterialId(firstUuid(List.of(base, technicalSpecs, payload), "frameMaterialId", "frame_material_id"));
                request.setBrandId(firstUuid(List.of(base, basicInfo, payload), "brandId", "brand_id"));
                request.setCategoryId(firstUuid(List.of(base, basicInfo, payload), "categoryId", "category_id"));
                request.setFrameSize(firstText(List.of(base, technicalSpecs, payload), "frameSize", "frame_size"));
                request.setWheelSize(firstText(List.of(base, technicalSpecs, payload), "wheelSize", "wheel_size"));
                request.setGroupsetId(firstUuid(List.of(base, technicalSpecs, payload), "groupsetId", "groupset_id"));
                request.setGroupset(firstText(List.of(base, technicalSpecs, payload), "groupset"));
                request.setProvince(firstText(List.of(base, priceLocation, payload), "province"));
                request.setDistrict(firstText(List.of(base, priceLocation, payload), "district"));
                request.setCondition(parseCondition(firstText(List.of(base, technicalSpecs, payload), "condition")));

                return request;
        }

        private JsonNode pickObjectNode(JsonNode root, String... keys) {
                if (root == null || root.isNull()) {
                        return null;
                }
                for (String key : keys) {
                        JsonNode child = root.get(key);
                        if (child != null && !child.isNull()) {
                                return child;
                        }
                }
                return root;
        }

        private String firstText(List<JsonNode> nodes, String... keys) {
                for (JsonNode node : nodes) {
                        if (node == null) {
                                continue;
                        }
                        for (String key : keys) {
                                JsonNode value = node.get(key);
                                if (value != null && !value.isNull()) {
                                        String text = value.asText();
                                        if (text != null && !text.isBlank()) {
                                                return text;
                                        }
                                }
                        }
                }
                return null;
        }

        private BigDecimal firstDecimal(List<JsonNode> nodes, String... keys) {
                String raw = firstText(nodes, keys);
                if (raw == null) {
                        return null;
                }
                try {
                        return new BigDecimal(raw);
                } catch (NumberFormatException e) {
                        return null;
                }
        }

        private UUID firstUuid(List<JsonNode> nodes, String... keys) {
                String raw = firstText(nodes, keys);
                if (raw == null) {
                        return null;
                }
                try {
                        return UUID.fromString(raw);
                } catch (IllegalArgumentException e) {
                        return null;
                }
        }

        private ConditionType parseCondition(String raw) {
                if (raw == null || raw.isBlank()) {
                        return null;
                }

                String normalized = raw.trim().toLowerCase(Locale.ROOT);
                return Arrays.stream(ConditionType.values())
                                .filter(value -> value.name().equalsIgnoreCase(normalized))
                                .findFirst()
                                .orElseThrow(() -> new AppException(ErrorCode.INVALID_REQUEST_BODY));
        }

        private List<String> extractImageUrls(JsonNode payload) {
                List<JsonNode> candidates = List.of(
                                pickObjectNode(payload, "request", "productCreateRequest", "createProductRequest", "product", "payload", "data"),
                                payload
                );

                for (JsonNode candidate : candidates) {
                        if (candidate == null) {
                                continue;
                        }
                        for (String key : List.of("imageUrls", "image_urls", "images")) {
                                JsonNode value = candidate.get(key);
                                if (value == null || value.isNull()) {
                                        continue;
                                }
                                if (!value.isArray()) {
                                        throw new AppException(ErrorCode.INVALID_REQUEST_BODY);
                                }
                                List<String> urls = new ArrayList<>();
                                value.forEach(item -> {
                                        if (item != null && !item.isNull()) {
                                                String url = item.asText();
                                                if (url != null && !url.isBlank()) {
                                                        urls.add(url.trim());
                                                }
                                        }
                                });
                                return urls;
                        }
                }
                return List.of();
        }

        private boolean isBlank(String value) {
                return value == null || value.isBlank();
        }

        private <T> void validateRequest(T request) {
                if (request == null) {
                        throw new AppException(ErrorCode.INVALID_REQUEST_BODY);
                }

                java.util.Set<ConstraintViolation<T>> violations = validator.validate(request);
                if (!violations.isEmpty()) {
                        ConstraintViolation<T> violation = violations.iterator().next();
                        String message = violation.getMessage();
                        throw new IllegalArgumentException(
                                        message == null || message.isBlank()
                                                        ? ErrorCode.INVALID_REQUEST_BODY.getMessage()
                                                        : message);
                }
        }

    private List<MultipartFile> mergeFiles(List<MultipartFile> first, List<MultipartFile> second) {
        List<MultipartFile> merged = new ArrayList<>();
        if (first != null && !first.isEmpty()) {
            merged.addAll(first);
        }
        if (second != null && !second.isEmpty()) {
            merged.addAll(second);
        }
        return merged;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SELLER')")
    public ApiResponse<String> deleteProduct(
            @PathVariable UUID id,
            @AuthenticationPrincipal User currentUser
    ) {
        productService.delete(id, currentUser);
        return ApiResponse.<String>builder()
                .result("Đã xóa tin đăng thành công")
                .build();
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('SELLER')")
    public ApiResponse<Page<ProductResponse>> getMyProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @AuthenticationPrincipal User currentUser
    ) {
        return ApiResponse.<Page<ProductResponse>>builder()
                .result(productService.getMyProducts(currentUser, page, size))
                .build();
    }

    @PatchMapping("/{id}/hide")
    @PreAuthorize("hasRole('SELLER')")
    public ApiResponse<ProductResponse> hideProduct(
            @PathVariable UUID id,
            @AuthenticationPrincipal User currentUser
    ) {
        return ApiResponse.<ProductResponse>builder()
                .result(productService.hide(id, currentUser))
                .build();
    }

    @PatchMapping("/{id}/show")
    @PreAuthorize("hasRole('SELLER')")
    public ApiResponse<ProductResponse> showProduct(
            @PathVariable UUID id,
            @AuthenticationPrincipal User currentUser
    ) {
        return ApiResponse.<ProductResponse>builder()
                .result(productService.show(id, currentUser))
                .build();
    }
}
