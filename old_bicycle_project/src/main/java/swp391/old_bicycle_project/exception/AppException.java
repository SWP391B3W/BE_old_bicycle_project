package swp391.old_bicycle_project.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppException extends RuntimeException {

    private ErrorCode errorCode;

    public AppException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    // Manual Getter
    public ErrorCode getErrorCode() { return errorCode; }

    // Manual Setter
    public void setErrorCode(ErrorCode errorCode) { this.errorCode = errorCode; }
}
