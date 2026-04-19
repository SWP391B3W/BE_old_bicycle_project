package swp391.old_bicycle_project.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import swp391.old_bicycle_project.entity.enums.ProductStatus;

@Getter
@Setter
public class AdminProductStatusUpdateRequest {

    @NotNull(message = "Product status is required")
    private ProductStatus status;

    // Manual Getter
    public ProductStatus getStatus() { return status; }

    // Manual Setter
    public void setStatus(ProductStatus status) { this.status = status; }
}
