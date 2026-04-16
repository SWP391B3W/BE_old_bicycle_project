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
public class InspectionHistoryItemResponseDTO {
    private UUID inspectionId;
    private UUID productId;
    private String productTitle;
    private BigDecimal productPrice;
    private String province;
    private String productImageUrl;
    private UUID sellerId;
    private String sellerName;
    private String sellerPhone;
    private UUID inspectorId;
    private String inspectorName;
    private BigDecimal overallScore;
    private Boolean passed;
    private String reportFileUrl;
    private LocalDateTime requestedAt;
    private LocalDateTime evaluatedAt;
    private LocalDateTime validUntil;

    public static InspectionHistoryItemResponseDTOBuilder builder() {
        return new InspectionHistoryItemResponseDTOBuilder();
    }

    public static class InspectionHistoryItemResponseDTOBuilder {
        private InspectionHistoryItemResponseDTO k = new InspectionHistoryItemResponseDTO();
        public InspectionHistoryItemResponseDTOBuilder inspectionId(UUID id) { k.inspectionId = id; return this; }
        public InspectionHistoryItemResponseDTOBuilder productId(UUID id) { k.productId = id; return this; }
        public InspectionHistoryItemResponseDTOBuilder productTitle(String t) { k.productTitle = t; return this; }
        public InspectionHistoryItemResponseDTOBuilder productPrice(BigDecimal p) { k.productPrice = p; return this; }
        public InspectionHistoryItemResponseDTOBuilder province(String p) { k.province = p; return this; }
        public InspectionHistoryItemResponseDTOBuilder productImageUrl(String u) { k.productImageUrl = u; return this; }
        public InspectionHistoryItemResponseDTOBuilder sellerId(UUID id) { k.sellerId = id; return this; }
        public InspectionHistoryItemResponseDTOBuilder sellerName(String n) { k.sellerName = n; return this; }
        public InspectionHistoryItemResponseDTOBuilder sellerPhone(String p) { k.sellerPhone = p; return this; }
        public InspectionHistoryItemResponseDTOBuilder inspectorId(UUID id) { k.inspectorId = id; return this; }
        public InspectionHistoryItemResponseDTOBuilder inspectorName(String n) { k.inspectorName = n; return this; }
        public InspectionHistoryItemResponseDTOBuilder overallScore(BigDecimal s) { k.overallScore = s; return this; }
        public InspectionHistoryItemResponseDTOBuilder passed(Boolean p) { k.passed = p; return this; }
        public InspectionHistoryItemResponseDTOBuilder reportFileUrl(String u) { k.reportFileUrl = u; return this; }
        public InspectionHistoryItemResponseDTOBuilder requestedAt(LocalDateTime d) { k.requestedAt = d; return this; }
        public InspectionHistoryItemResponseDTOBuilder evaluatedAt(LocalDateTime d) { k.evaluatedAt = d; return this; }
        public InspectionHistoryItemResponseDTOBuilder validUntil(LocalDateTime d) { k.validUntil = d; return this; }
        public InspectionHistoryItemResponseDTO build() { return k; }
    }
}
