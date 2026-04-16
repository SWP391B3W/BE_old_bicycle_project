package swp391.old_bicycle_project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import swp391.old_bicycle_project.entity.Order;
import swp391.old_bicycle_project.entity.enums.OrderFundingStatus;
import swp391.old_bicycle_project.entity.enums.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {

    @Query(value = """
            select exists(
                select 1
                from orders o
                where o.product_id = :productId
                  and (
                    (o.status = 'pending' and o.funding_status = 'awaiting_payment')
                    or o.status in ('deposited', 'awaiting_buyer_confirmation')
                  )
            )
            """, nativeQuery = true)
    boolean existsExclusiveOrderLockByProductId(@Param("productId") UUID productId);

    List<Order> findByBuyerIdOrSellerIdOrderByCreatedAtDesc(UUID buyerId, UUID sellerId);

    List<Order> findAllByOrderByCreatedAtDesc();

    long countByBuyerId(UUID buyerId);

    long countBySellerId(UUID sellerId);

    List<Order> findByStatusAndFundingStatusAndPaymentDeadlineBefore(
            OrderStatus status,
            OrderFundingStatus fundingStatus,
            LocalDateTime paymentDeadline
    );
}
