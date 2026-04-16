package swp391.old_bicycle_project.repository;

import swp391.old_bicycle_project.entity.FrameMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FrameMaterialRepository extends JpaRepository<FrameMaterial, UUID> {
    boolean existsByNameIgnoreCase(String name);
    boolean existsByNameIgnoreCaseAndIdNot(String name, UUID id);
}
