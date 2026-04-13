package swp391.old_bicycle_project.service;

import swp391.old_bicycle_project.exception.AppException;
import swp391.old_bicycle_project.exception.ErrorCode;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class PasswordPolicyValidator {

    private static final Pattern PASSWORD_POLICY = Pattern.compile("^(?=.*[A-Z])(?=.*\\d).{8,}$");

    public void validate(String password) {
        if (password == null || !PASSWORD_POLICY.matcher(password).matches()) {
            throw new AppException(ErrorCode.INVALID_PASSWORD);
        }
    }
}
