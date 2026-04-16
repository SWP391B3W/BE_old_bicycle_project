package swp391.old_bicycle_project.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InspectionDashboardResponseDTO {
    private long pendingRequests;
    private long completedThisWeek;
    private BigDecimal passRate;
    private BigDecimal averageScore;
    private List<InspectionHistoryItemResponseDTO> recentInspections;

    public static InspectionDashboardResponseDTOBuilder builder() {
        return new InspectionDashboardResponseDTOBuilder();
    }

    public static class InspectionDashboardResponseDTOBuilder {
        private InspectionDashboardResponseDTO k = new InspectionDashboardResponseDTO();
        public InspectionDashboardResponseDTOBuilder pendingRequests(long n) { k.pendingRequests = n; return this; }
        public InspectionDashboardResponseDTOBuilder completedThisWeek(long n) { k.completedThisWeek = n; return this; }
        public InspectionDashboardResponseDTOBuilder passRate(BigDecimal r) { k.passRate = r; return this; }
        public InspectionDashboardResponseDTOBuilder averageScore(BigDecimal s) { k.averageScore = s; return this; }
        public InspectionDashboardResponseDTOBuilder recentInspections(List<InspectionHistoryItemResponseDTO> l) { k.recentInspections = l; return this; }
        public InspectionDashboardResponseDTO build() { return k; }
    }
}
