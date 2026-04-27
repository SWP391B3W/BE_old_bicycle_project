package swp391.old_bicycle_project.service.impl;

import swp391.old_bicycle_project.dto.response.DashboardStatsDTO;
import swp391.old_bicycle_project.entity.enums.OrderStatus;
import swp391.old_bicycle_project.entity.enums.PlatformFeeStatus;
import swp391.old_bicycle_project.entity.enums.UserStatus;
import swp391.old_bicycle_project.repository.InspectionRepository;
import swp391.old_bicycle_project.repository.OrderRepository;
import swp391.old_bicycle_project.repository.ProductRepository;
import swp391.old_bicycle_project.repository.UserRepository;
import swp391.old_bicycle_project.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final InspectionRepository inspectionRepository;

    @Override
    // getDashboardStats gồm tổng hợp số liệu admin dashboard:
    // user/product/order, doanh thu (GMV), platform fee theo status,
    // inspection stats và dữ liệu theo tháng.
    public DashboardStatsDTO getDashboardStats() {

        // 1. Tổng quan thực thể chính (active users, products chưa xóa mềm, orders chưa hủy).
        long totalUsers = userRepository.countByStatus(UserStatus.active);
        long totalProducts = productRepository.countByDeletedAtIsNull();
        long totalOrders = orderRepository.countByStatusNot(OrderStatus.cancelled);

        // 2. Doanh thu tổng: lấy GMV từ orders completed.
        BigDecimal totalGmv = defaultZero(orderRepository.sumTotalAmountByStatus(OrderStatus.completed));

        // 3. Platform fee theo từng status để admin theo dõi dòng tiền:
        //    - pending: đã phát sinh nhưng chưa ghi nhận doanh thu,
        //    - recognized: đã ghi nhận doanh thu nền tảng,
        //    - reversed: đã đảo/hoàn phí.
        BigDecimal pendingPlatformFee = defaultZero(
                orderRepository.sumPlatformFeeTotalByPlatformFeeStatus(PlatformFeeStatus.pending)
        );
        BigDecimal recognizedPlatformRevenue = defaultZero(
                orderRepository.sumPlatformFeeTotalByPlatformFeeStatus(PlatformFeeStatus.recognized)
        );
        BigDecimal reversedPlatformFee = defaultZero(
                orderRepository.sumPlatformFeeTotalByPlatformFeeStatus(PlatformFeeStatus.reversed)
        );

        // 4. Thống kê kiểm định cho dashboard inspector/admin.
        long totalInspections = inspectionRepository.countByInspectorIsNotNull();
        long passedInspections = inspectionRepository.countByInspectorIsNotNullAndPassedTrue();
        long failedInspections = inspectionRepository.countByInspectorIsNotNullAndPassedFalse();

        // 5. Chuỗi thời gian theo tháng (GMV, platform revenue đã recognized, số đơn completed).
        Map<String, BigDecimal> monthlyGmv = toBigDecimalMap(orderRepository.getMonthlyCompletedGmv());
        Map<String, BigDecimal> monthlyRecognizedPlatformRevenue =
                toBigDecimalMap(orderRepository.getMonthlyRecognizedPlatformRevenue());
        Map<String, Long> monthlyOrders = toLongMap(orderRepository.getMonthlyCompletedOrderCount());

        // 6. Build DTO trả về cho admin dashboard.
        return DashboardStatsDTO.builder()
                .totalUsers(totalUsers)
                .totalProducts(totalProducts)
                .totalOrders(totalOrders)
                .totalRevenue(totalGmv)
                .totalGmv(totalGmv)
                .pendingPlatformFee(pendingPlatformFee)
                .recognizedPlatformRevenue(recognizedPlatformRevenue)
                .reversedPlatformFee(reversedPlatformFee)
                .totalInspections(totalInspections)
                .passedInspections(passedInspections)
                .failedInspections(failedInspections)
                .monthlyRevenue(new LinkedHashMap<>(monthlyGmv))
                .monthlyGmv(monthlyGmv)
                .monthlyRecognizedPlatformRevenue(monthlyRecognizedPlatformRevenue)
                .monthlyOrders(monthlyOrders)
                .build();
    }

    // defaultZero: chuẩn hóa số liệu null từ query aggregate về 0.
    private BigDecimal defaultZero(BigDecimal value) {
        return value != null ? value : BigDecimal.ZERO;
    }

    // toBigDecimalMap: chuyển danh sách [month, amount] từ native query thành map month -> amount.
    private Map<String, BigDecimal> toBigDecimalMap(List<Object[]> rawRows) {
        Map<String, BigDecimal> result = new LinkedHashMap<>();
        for (Object[] row : rawRows) {
            String month = (String) row[0];
            BigDecimal amount = row[1] instanceof BigDecimal
                    ? (BigDecimal) row[1]
                    : new BigDecimal(row[1].toString());
            result.put(month, amount);
        }
        return result;
    }

    // toLongMap: chuyển danh sách [month, count] từ native query thành map month -> count.
    private Map<String, Long> toLongMap(List<Object[]> rawRows) {
        Map<String, Long> result = new LinkedHashMap<>();
        for (Object[] row : rawRows) {
            String month = (String) row[0];
            Long count = row[1] instanceof Number
                    ? ((Number) row[1]).longValue()
                    : Long.parseLong(row[1].toString());
            result.put(month, count);
        }
        return result;
    }
}
