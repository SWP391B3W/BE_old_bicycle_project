package swp391.old_bicycle_project.service;

import swp391.old_bicycle_project.entity.RefreshToken;
import swp391.old_bicycle_project.entity.User;

import java.util.Optional;

public interface RefreshTokenService {

    RefreshToken createRefreshToken(User user);

    Optional<RefreshToken> findByToken(String token);

    void deleteAllByUser(User user);
}
