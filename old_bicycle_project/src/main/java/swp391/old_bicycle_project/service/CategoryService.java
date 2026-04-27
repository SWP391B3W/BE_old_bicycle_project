package swp391.old_bicycle_project.service;

import swp391.old_bicycle_project.entity.Category;
import swp391.old_bicycle_project.exception.AppException;
import swp391.old_bicycle_project.exception.ErrorCode;
import swp391.old_bicycle_project.repository.CategoryRepository;
import swp391.old_bicycle_project.repository.ProductRepository;
import swp391.old_bicycle_project.repository.SizeChartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final SizeChartRepository sizeChartRepository;

    public List<Category> getRootCategories() {
        // Lấy danh mục gốc (không có parent).
        return categoryRepository.findByParentIsNull();
    }

    // getAll gồm lấy toàn bộ category và sort tăng dần theo tên.
    public List<Category> getAll() {
        return categoryRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
    }

    // getById gồm lấy category theo id hoặc ném RESOURCE_NOT_FOUND.
    public Category getById(UUID id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));
    }

    // create category gồm normalize slug, check trùng slug,
    // resolve parent (nếu có) và lưu mới.
    public Category create(String name, String slug, UUID parentId) {
        // 1. Normalize slug
        String normalizedSlug = slug.trim();
        // 2. Check slug unique
        if (categoryRepository.existsBySlug(normalizedSlug)) {
            throw new AppException(ErrorCode.RECORD_ALREADY_EXISTS);
        }

        // 3. Resolve parent và lưu category
        Category parent = parentId != null ? getById(parentId) : null;
        return categoryRepository.save(Category.builder()
                .name(name.trim())
                .slug(normalizedSlug)
                .parent(parent)
                .build());
    }

    // update category gồm lấy category hiện tại, check trùng slug,
    // validate parent hierarchy và lưu cập nhật.
    public Category update(UUID id, String name, String slug, UUID parentId) {
        // 1. Lấy category cần sửa
        Category category = getById(id);
        // 2. Normalize + check slug unique (trừ chính nó)
        String normalizedSlug = slug.trim();
        if (categoryRepository.existsBySlugAndIdNot(normalizedSlug, id)) {
            throw new AppException(ErrorCode.RECORD_ALREADY_EXISTS);
        }

        // 3. Cập nhật dữ liệu và lưu
        category.setName(name.trim());
        category.setSlug(normalizedSlug);
        category.setParent(resolveParentCategory(id, parentId));
        return categoryRepository.save(category);
    }

    // delete category gồm check tồn tại, check không có node con,
    // check không bị tham chiếu bởi product/sizeChart rồi mới xóa.
    public void delete(UUID id) {
        // 1. Check category tồn tại
        getById(id);
        // 2. Chặn xóa nếu còn category con
        if (categoryRepository.existsByParentId(id)) {
            throw new AppException(ErrorCode.CATEGORY_HIERARCHY_INVALID);
        }
        // 3. Chặn xóa nếu đang được product tham chiếu
        if (productRepository.existsByCategoryIdAndDeletedAtIsNull(id)) {
            throw new AppException(ErrorCode.REFERENCE_DATA_IN_USE);
        }
        // 4. Chặn xóa nếu đang được size chart tham chiếu
        if (sizeChartRepository.existsByCategoryId(id)) {
            throw new AppException(ErrorCode.REFERENCE_DATA_IN_USE);
        }
        // 5. Xóa category
        categoryRepository.deleteById(id);
    }

    // resolveParentCategory gồm validate self-parent và vòng lặp hierarchy,
    // sau đó trả parent hợp lệ.
    private Category resolveParentCategory(UUID categoryId, UUID parentId) {
        // 1. Không truyền parent thì coi là root category
        if (parentId == null) {
            return null;
        }
        // 2. Chặn parent là chính nó
        if (categoryId.equals(parentId)) {
            throw new AppException(ErrorCode.CATEGORY_HIERARCHY_INVALID);
        }

        // 3. Duyệt ngược chuỗi parent để chặn cycle
        Category parent = getById(parentId);
        Category current = parent;
        while (current != null) {
            if (categoryId.equals(current.getId())) {
                throw new AppException(ErrorCode.CATEGORY_HIERARCHY_INVALID);
            }
            current = current.getParent();
        }
        // 4. Parent hợp lệ
        return parent;
    }
}
