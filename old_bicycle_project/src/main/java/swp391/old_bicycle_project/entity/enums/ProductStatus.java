package swp391.old_bicycle_project.entity.enums;

public enum ProductStatus {
    pending,              // trạng thái cũ/legacy: chờ admin kiểm duyệt nội dung ban đầu
    active,               // đã kiểm định đạt và đang hiển thị công khai
    hidden,               // bị ẩn bởi admin hoặc seller
    sold,                 // đã bán
    pending_inspection,   // tin đã vào hàng chờ kiểm định và đang chờ inspector xử lý
    inspected_passed,     // trạng thái legacy: đã kiểm định đạt
    inspected_failed      // đã kiểm định nhưng không đạt
}
