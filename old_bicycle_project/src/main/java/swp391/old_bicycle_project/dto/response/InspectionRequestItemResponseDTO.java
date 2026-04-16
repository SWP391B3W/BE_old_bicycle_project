package swp391.old_bicycle_project.dto.response;

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
public class InspectionRequestItemResponseDTO {
    private UUID inspectionId;
    private UUID productId;
    private String productTitle;
    private BigDecimal productPrice;
    private String province;
    private String productImageUrl;
    private UUID sellerId;
    private String sellerName;
    private String sellerPhone;
    private LocalDateTime requestedAt;

    public static InspectionRequestItemResponseDTOBuilder builder() {
        return new InspectionRequestItemResponseDTOBuilder();
    }

    public static class InspectionRequestItemResponseDTOBuilder {
        private InspectionRequestItemResponseDTO k = new InspectionRequestItemResponseDTO();
        public InspectionRequestItemResponseDTOBuilder inspectionId(UUID id) { k.inspectionId = id; return this; }
        public InspectionRequestItemResponseDTOBuilder productId(UUID id) { k.productId = id; return this; }
        public InspectionRequestItemResponseDTOBuilder productTitle(String t) { k.productTitle = t; return this; }
        public InspectionRequestItemResponseDTOBuilder productPrice(BigDecimal p) { k.productPrice = p; return this; }
        public InspectionRequestItemResponseDTOBuilder province(String p) { k.province = p; return this; }
        public InspectionRequestItemResponseDTOBuilder productImageUrl(String u) { k.productImageUrl = u; return this; }
        public InspectionRequestItemResponseDTOBuilder sellerId(UUID id) { k.sellerId = id; return this; }
        public InspectionRequestItemResponseDTOBuilder sellerName(String n) { k.sellerName = n; return this; }
        public InspectionRequestItemResponseDTOBuilder sellerPhone(String p) { k.sellerPhone = p; return this; }
        public InspectionRequestItemResponseDTOBuilder requestedAt(LocalDateTime d) { k.requestedAt = d; return this; }
        public InspectionRequestItemResponseDTO build() { return k; }
    }
}
