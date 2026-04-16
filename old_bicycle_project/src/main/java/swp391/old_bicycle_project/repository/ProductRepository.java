package swp391.old_bicycle_project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import swp391.old_bicycle_project.entity.Product;

import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    Page<Product> findBySellerIdOrderByCreatedAtDesc(UUID sellerId, Pageable pageable);

    long countBySellerId(UUID sellerId);
}
