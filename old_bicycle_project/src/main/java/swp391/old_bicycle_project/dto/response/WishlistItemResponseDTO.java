package swp391.old_bicycle_project.dto.response;

import swp391.old_bicycle_project.entity.enums.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WishlistItemResponseDTO {
    private UUID productId;
    private String title;
    private BigDecimal price;
    private ProductStatus status;
    private UUID sellerId;
    private String sellerName;
    private String primaryImageUrl;
    private LocalDateTime addedAt;

    // Manual Builder
    public static WishlistItemResponseDTOBuilder builder() { return new WishlistItemResponseDTOBuilder(); }
    public static class WishlistItemResponseDTOBuilder {
        private WishlistItemResponseDTO r = new WishlistItemResponseDTO();
        public WishlistItemResponseDTOBuilder productId(UUID productId) { r.productId = productId; return this; }
        public WishlistItemResponseDTOBuilder title(String title) { r.title = title; return this; }
        public WishlistItemResponseDTOBuilder price(BigDecimal price) { r.price = price; return this; }
        public WishlistItemResponseDTOBuilder status(ProductStatus status) { r.status = status; return this; }
        public WishlistItemResponseDTOBuilder sellerId(UUID sellerId) { r.sellerId = sellerId; return this; }
        public WishlistItemResponseDTOBuilder sellerName(String sellerName) { r.sellerName = sellerName; return this; }
        public WishlistItemResponseDTOBuilder primaryImageUrl(String primaryImageUrl) { r.primaryImageUrl = primaryImageUrl; return this; }
        public WishlistItemResponseDTOBuilder addedAt(LocalDateTime addedAt) { r.addedAt = addedAt; return this; }
        public WishlistItemResponseDTO build() { return r; }
    }
}
