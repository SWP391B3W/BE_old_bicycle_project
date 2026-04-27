package swp391.old_bicycle_project.service;

import swp391.old_bicycle_project.entity.BrakeType;
import swp391.old_bicycle_project.exception.AppException;
import swp391.old_bicycle_project.exception.ErrorCode;
import swp391.old_bicycle_project.repository.BrakeTypeRepository;
import swp391.old_bicycle_project.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BrakeTypeService {

    private final BrakeTypeRepository brakeTypeRepository;
    private final ProductRepository productRepository;

    // getAll gồm lấy toàn bộ brake type và sort tăng dần theo tên.
    public List<BrakeType> getAll() {
        return brakeTypeRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
    }

    // getById gồm lấy brake type theo id hoặc ném RESOURCE_NOT_FOUND.
    public BrakeType getById(UUID id) {
        return brakeTypeRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));
    }

    // create brake type gồm normalize name, check trùng tên, rồi lưu mới.
    public BrakeType create(String name, String description) {
        // 1. Normalize name
        String normalizedName = name.trim();
        // 2. Check name unique (ignore case)
        if (brakeTypeRepository.existsByNameIgnoreCase(normalizedName)) {
            throw new AppException(ErrorCode.RECORD_ALREADY_EXISTS);
        }
        // 3. Lưu brake type
        return brakeTypeRepository.save(BrakeType.builder()
                .name(normalizedName)
                .description(description)
                .build());
    }

    // update brake type gồm lấy dữ liệu hiện tại, check trùng tên,
    // cập nhật name/description và lưu.
    public BrakeType update(UUID id, String name, String description) {
        // 1. Lấy brake type cần sửa
        BrakeType brakeType = getById(id);
        // 2. Normalize + check unique (trừ chính nó)
        String normalizedName = name.trim();
        if (brakeTypeRepository.existsByNameIgnoreCaseAndIdNot(normalizedName, id)) {
            throw new AppException(ErrorCode.RECORD_ALREADY_EXISTS);
        }
        // 3. Cập nhật và lưu
        brakeType.setName(normalizedName);
        brakeType.setDescription(description);
        return brakeTypeRepository.save(brakeType);
    }

    // delete brake type gồm check tồn tại, check không bị product tham chiếu rồi xóa.
    public void delete(UUID id) {
        // 1. Check brake type tồn tại
        getById(id);
        // 2. Chặn xóa nếu còn product đang dùng
        if (productRepository.existsByBrakeTypeIdAndDeletedAtIsNull(id)) {
            throw new AppException(ErrorCode.REFERENCE_DATA_IN_USE);
        }
        // 3. Xóa brake type
        brakeTypeRepository.deleteById(id);
    }
}
