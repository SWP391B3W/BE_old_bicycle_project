package swp391.old_bicycle_project.specification;

import org.springframework.data.jpa.domain.Specification;
import swp391.old_bicycle_project.entity.User;
import swp391.old_bicycle_project.entity.enums.AppRole;
import swp391.old_bicycle_project.entity.enums.UserStatus;

import java.util.ArrayList;
import java.util.List;

public final class UserSpecification {

    private UserSpecification() {
    }

    public static Specification<User> fromAdminFilter(String keyword, AppRole role, UserStatus status, Boolean verified) {
        return (root, query, cb) -> {
            List<jakarta.persistence.criteria.Predicate> predicates = new ArrayList<>();

            if (keyword != null && !keyword.isBlank()) {
                String normalizedKeyword = "%" + keyword.trim().toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("email")), normalizedKeyword),
                        cb.like(cb.lower(cb.coalesce(root.get("firstName"), "")), normalizedKeyword),
                        cb.like(cb.lower(cb.coalesce(root.get("lastName"), "")), normalizedKeyword),
                        cb.like(cb.lower(cb.coalesce(root.get("phone"), "")), normalizedKeyword)
                ));
            }

            if (role != null) {
                predicates.add(cb.equal(root.get("role"), role));
            }

            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }

            if (verified != null) {
                predicates.add(cb.equal(root.get("isVerified"), verified));
            }

            return cb.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        };
    }
}
