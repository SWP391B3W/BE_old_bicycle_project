package swp391.old_bicycle_project.controller;

import swp391.old_bicycle_project.dto.request.AdminBrandUpsertRequest;
import swp391.old_bicycle_project.dto.request.AdminCategoryUpsertRequest;
import swp391.old_bicycle_project.dto.request.AdminReferenceValueUpsertRequest;
import swp391.old_bicycle_project.dto.response.ApiResponse;
import swp391.old_bicycle_project.dto.response.BrandResponseDTO;
import swp391.old_bicycle_project.dto.response.CategoryResponseDTO;
import swp391.old_bicycle_project.dto.response.ReferenceValueResponseDTO;
import swp391.old_bicycle_project.dto.response.SizeChartResponseDTO;
import swp391.old_bicycle_project.dto.response.SizeChartRowResponseDTO;
import swp391.old_bicycle_project.entity.Brand;
import swp391.old_bicycle_project.entity.BrakeType;
import swp391.old_bicycle_project.entity.Category;
import swp391.old_bicycle_project.entity.FrameMaterial;
import swp391.old_bicycle_project.entity.Groupset;
import swp391.old_bicycle_project.entity.SizeChart;
import swp391.old_bicycle_project.entity.SizeChartRow;
import swp391.old_bicycle_project.service.BrandService;
import swp391.old_bicycle_project.service.BrakeTypeService;
import swp391.old_bicycle_project.service.CategoryService;
import swp391.old_bicycle_project.service.FrameMaterialService;
import swp391.old_bicycle_project.service.GroupsetService;
import swp391.old_bicycle_project.service.SizeChartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class ReferenceDataController {

    private final BrandService brandService;
    private final CategoryService categoryService;
    private final BrakeTypeService brakeTypeService;
    private final FrameMaterialService frameMaterialService;
    private final GroupsetService groupsetService;
    private final SizeChartService sizeChartService;

    @GetMapping("/api/brands")
    public ApiResponse<List<BrandResponseDTO>> getAllBrands() {
        return ApiResponse.<List<BrandResponseDTO>>builder()
                .result(brandService.getAll().stream().map(this::toBrandResponse).toList())
                .build();
    }

    @PostMapping("/api/admin/brands")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<BrandResponseDTO> createBrand(@Valid @RequestBody AdminBrandUpsertRequest request) {
        return ApiResponse.<BrandResponseDTO>builder()
                .result(toBrandResponse(brandService.create(request.getName(), request.getLogoUrl())))
                .build();
    }

    @PutMapping("/api/admin/brands/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<BrandResponseDTO> updateBrand(
            @PathVariable UUID id,
            @Valid @RequestBody AdminBrandUpsertRequest request
    ) {
        return ApiResponse.<BrandResponseDTO>builder()
                .result(toBrandResponse(brandService.update(id, request.getName(), request.getLogoUrl())))
                .build();
    }

    @DeleteMapping("/api/admin/brands/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<String> deleteBrand(@PathVariable UUID id) {
        brandService.delete(id);
        return ApiResponse.<String>builder()
                .result("Da xoa hang xe")
                .build();
    }

    @GetMapping("/api/categories")
    public ApiResponse<List<CategoryResponseDTO>> getAllCategories() {
        return ApiResponse.<List<CategoryResponseDTO>>builder()
                .result(categoryService.getAll().stream().map(this::toCategoryResponse).toList())
                .build();
    }

    @PostMapping("/api/admin/categories")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<CategoryResponseDTO> createCategory(@Valid @RequestBody AdminCategoryUpsertRequest request) {
        return ApiResponse.<CategoryResponseDTO>builder()
                .result(toCategoryResponse(categoryService.create(
                        request.getName(),
                        request.getSlug(),
                        request.getParentId()
                )))
                .build();
    }

    @PutMapping("/api/admin/categories/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<CategoryResponseDTO> updateCategory(
            @PathVariable UUID id,
            @Valid @RequestBody AdminCategoryUpsertRequest request
    ) {
        return ApiResponse.<CategoryResponseDTO>builder()
                .result(toCategoryResponse(categoryService.update(
                        id,
                        request.getName(),
                        request.getSlug(),
                        request.getParentId()
                )))
                .build();
    }

    @DeleteMapping("/api/admin/categories/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<String> deleteCategory(@PathVariable UUID id) {
        categoryService.delete(id);
        return ApiResponse.<String>builder()
                .result("Da xoa danh muc")
                .build();
    }

    @GetMapping("/api/brake-types")
    public ApiResponse<List<ReferenceValueResponseDTO>> getAllBrakeTypes() {
        return ApiResponse.<List<ReferenceValueResponseDTO>>builder()
                .result(brakeTypeService.getAll().stream().map(this::toReferenceValueResponse).toList())
                .build();
    }

    @PostMapping("/api/admin/brake-types")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<ReferenceValueResponseDTO> createBrakeType(
            @Valid @RequestBody AdminReferenceValueUpsertRequest request
    ) {
        return ApiResponse.<ReferenceValueResponseDTO>builder()
                .result(toReferenceValueResponse(brakeTypeService.create(request.getName(), request.getDescription())))
                .build();
    }

    @PutMapping("/api/admin/brake-types/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<ReferenceValueResponseDTO> updateBrakeType(
            @PathVariable UUID id,
            @Valid @RequestBody AdminReferenceValueUpsertRequest request
    ) {
        return ApiResponse.<ReferenceValueResponseDTO>builder()
                .result(toReferenceValueResponse(brakeTypeService.update(id, request.getName(), request.getDescription())))
                .build();
    }

    @DeleteMapping("/api/admin/brake-types/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<String> deleteBrakeType(@PathVariable UUID id) {
        brakeTypeService.delete(id);
        return ApiResponse.<String>builder()
                .result("Da xoa loai phanh")
                .build();
    }

    @GetMapping("/api/frame-materials")
    public ApiResponse<List<ReferenceValueResponseDTO>> getAllFrameMaterials() {
        return ApiResponse.<List<ReferenceValueResponseDTO>>builder()
                .result(frameMaterialService.getAll().stream().map(this::toReferenceValueResponse).toList())
                .build();
    }

    @GetMapping("/api/groupsets")
    public ApiResponse<List<ReferenceValueResponseDTO>> getAllGroupsets() {
        return ApiResponse.<List<ReferenceValueResponseDTO>>builder()
                .result(groupsetService.getAll().stream().map(this::toReferenceValueResponse).toList())
                .build();
    }

    @GetMapping("/api/size-charts/category/{categoryId}")
    public ApiResponse<SizeChartResponseDTO> getSizeChartByCategory(@PathVariable UUID categoryId) {
        return ApiResponse.<SizeChartResponseDTO>builder()
                .result(sizeChartService.getByCategory(categoryId).map(this::toSizeChartResponse).orElse(null))
                .build();
    }

    private BrandResponseDTO toBrandResponse(Brand brand) {
        return BrandResponseDTO.builder()
                .id(brand.getId())
                .name(brand.getName())
                .logoUrl(brand.getLogoUrl())
                .build();
    }

    private CategoryResponseDTO toCategoryResponse(Category category) {
        return CategoryResponseDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .slug(category.getSlug())
                .parentId(category.getParent() != null ? category.getParent().getId() : null)
                .parentName(category.getParent() != null ? category.getParent().getName() : null)
                .build();
    }

    private ReferenceValueResponseDTO toReferenceValueResponse(BrakeType brakeType) {
        return ReferenceValueResponseDTO.builder()
                .id(brakeType.getId())
                .name(brakeType.getName())
                .description(brakeType.getDescription())
                .build();
    }

    private ReferenceValueResponseDTO toReferenceValueResponse(FrameMaterial frameMaterial) {
        return ReferenceValueResponseDTO.builder()
                .id(frameMaterial.getId())
                .name(frameMaterial.getName())
                .description(frameMaterial.getDescription())
                .build();
    }

    private ReferenceValueResponseDTO toReferenceValueResponse(Groupset groupset) {
        return ReferenceValueResponseDTO.builder()
                .id(groupset.getId())
                .name(groupset.getName())
                .description(groupset.getDescription())
                .build();
    }

    private SizeChartResponseDTO toSizeChartResponse(SizeChart sizeChart) {
        return SizeChartResponseDTO.builder()
                .id(sizeChart.getId())
                .categoryId(sizeChart.getCategory().getId())
                .categoryName(sizeChart.getCategory().getName())
                .name(sizeChart.getName())
                .description(sizeChart.getDescription())
                .rows(sizeChart.getRows().stream().map(this::toSizeChartRowResponse).toList())
                .createdAt(sizeChart.getCreatedAt())
                .updatedAt(sizeChart.getUpdatedAt())
                .build();
    }

    private SizeChartRowResponseDTO toSizeChartRowResponse(SizeChartRow row) {
        return SizeChartRowResponseDTO.builder()
                .id(row.getId())
                .frameSize(row.getFrameSize())
                .heightMinCm(row.getHeightMinCm())
                .heightMaxCm(row.getHeightMaxCm())
                .note(row.getNote())
                .displayOrder(row.getDisplayOrder())
                .build();
    }
}
