package swp391.old_bicycle_project.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardStatsDTO {

    // General Stats
    private long totalUsers;
    private long totalProducts;
    private long totalOrders;
    private BigDecimal totalRevenue; // Legacy alias of totalGmv for FE compatibility
    private BigDecimal totalGmv;
    private BigDecimal pendingPlatformFee;
    private BigDecimal recognizedPlatformRevenue;
    private BigDecimal reversedPlatformFee;

    // Inspection Stats
    private long totalInspections;
    private long passedInspections;
    private long failedInspections;

    // Monthly Data (for charts)
    private Map<String, BigDecimal> monthlyRevenue; // Legacy alias of monthlyGmv for FE compatibility
    private Map<String, BigDecimal> monthlyGmv; // "YYYY-MM" -> amount
    private Map<String, BigDecimal> monthlyRecognizedPlatformRevenue; // "YYYY-MM" -> amount
    private Map<String, Long> monthlyOrders; // "YYYY-MM" -> count

    // Manual Builder
    public static DashboardStatsDTOBuilder builder() { return new DashboardStatsDTOBuilder(); }
    public static class DashboardStatsDTOBuilder {
        private DashboardStatsDTO r = new DashboardStatsDTO();
        public DashboardStatsDTOBuilder totalUsers(long totalUsers) { r.totalUsers = totalUsers; return this; }
        public DashboardStatsDTOBuilder totalProducts(long totalProducts) { r.totalProducts = totalProducts; return this; }
        public DashboardStatsDTOBuilder totalOrders(long totalOrders) { r.totalOrders = totalOrders; return this; }
        public DashboardStatsDTOBuilder totalRevenue(BigDecimal totalRevenue) { r.totalRevenue = totalRevenue; return this; }
        public DashboardStatsDTOBuilder totalGmv(BigDecimal totalGmv) { r.totalGmv = totalGmv; return this; }
        public DashboardStatsDTOBuilder pendingPlatformFee(BigDecimal pendingPlatformFee) { r.pendingPlatformFee = pendingPlatformFee; return this; }
        public DashboardStatsDTOBuilder recognizedPlatformRevenue(BigDecimal recognizedPlatformRevenue) { r.recognizedPlatformRevenue = recognizedPlatformRevenue; return this; }
        public DashboardStatsDTOBuilder reversedPlatformFee(BigDecimal reversedPlatformFee) { r.reversedPlatformFee = reversedPlatformFee; return this; }
        public DashboardStatsDTOBuilder totalInspections(long totalInspections) { r.totalInspections = totalInspections; return this; }
        public DashboardStatsDTOBuilder passedInspections(long passedInspections) { r.passedInspections = passedInspections; return this; }
        public DashboardStatsDTOBuilder failedInspections(long failedInspections) { r.failedInspections = failedInspections; return this; }
        public DashboardStatsDTOBuilder monthlyRevenue(Map<String, BigDecimal> monthlyRevenue) { r.monthlyRevenue = monthlyRevenue; return this; }
        public DashboardStatsDTOBuilder monthlyGmv(Map<String, BigDecimal> monthlyGmv) { r.monthlyGmv = monthlyGmv; return this; }
        public DashboardStatsDTOBuilder monthlyRecognizedPlatformRevenue(Map<String, BigDecimal> monthlyRecognizedPlatformRevenue) { r.monthlyRecognizedPlatformRevenue = monthlyRecognizedPlatformRevenue; return this; }
        public DashboardStatsDTOBuilder monthlyOrders(Map<String, Long> monthlyOrders) { r.monthlyOrders = monthlyOrders; return this; }
        public DashboardStatsDTO build() { return r; }
    }
}
