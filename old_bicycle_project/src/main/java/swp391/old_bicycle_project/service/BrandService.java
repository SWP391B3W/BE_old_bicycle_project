package swp391.old_bicycle_project.service;

import swp391.old_bicycle_project.entity.Brand;
import swp391.old_bicycle_project.exception.AppException;
import swp391.old_bicycle_project.exception.ErrorCode;
import swp391.old_bicycle_project.repository.BrandRepository;
import swp391.old_bicycle_project.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BrandService {

    private final BrandRepository brandRepository;
    private final ProductRepository productRepository;

    // getAll gồm lấy toàn bộ brand và sort tăng dần theo tên.
    public List<Brand> getAll() {
        return brandRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
    }

    // getById gồm lấy brand theo id hoặc ném RESOURCE_NOT_FOUND.
    public Brand getById(UUID id) {
        return brandRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));
    }

    // create brand gồm normalize name, check trùng tên, rồi lưu mới.
    public Brand create(String name, String logoUrl) {
        // 1. Normalize name
        String normalizedName = name.trim();
        // 2. Check name unique (ignore case)
        if (brandRepository.existsByNameIgnoreCase(normalizedName)) {
            throw new AppException(ErrorCode.RECORD_ALREADY_EXISTS);
        }
        // 3. Lưu brand
        return brandRepository.save(Brand.builder()
                .name(normalizedName)
                .logoUrl(logoUrl)
                .build());
    }

    // update brand gồm lấy brand hiện tại, check trùng tên,
    // cập nhật name/logo và lưu.
    public Brand update(UUID id, String name, String logoUrl) {
        // 1. Lấy brand cần sửa
        Brand brand = getById(id);
        // 2. Normalize + check unique (trừ chính nó)
        String normalizedName = name.trim();
        if (brandRepository.existsByNameIgnoreCaseAndIdNot(normalizedName, id)) {
            throw new AppException(ErrorCode.RECORD_ALREADY_EXISTS);
        }
        // 3. Cập nhật và lưu
        brand.setName(normalizedName);
        brand.setLogoUrl(logoUrl);
        return brandRepository.save(brand);
    }

    // delete brand gồm check tồn tại, check không bị product tham chiếu rồi mới xóa.
    public void delete(UUID id) {
        // 1. Check brand tồn tại
        getById(id);
        // 2. Chặn xóa nếu còn product đang dùng
        if (productRepository.existsByBrandIdAndDeletedAtIsNull(id)) {
            throw new AppException(ErrorCode.REFERENCE_DATA_IN_USE);
        }
        // 3. Xóa brand
        brandRepository.deleteById(id);
    }
}
