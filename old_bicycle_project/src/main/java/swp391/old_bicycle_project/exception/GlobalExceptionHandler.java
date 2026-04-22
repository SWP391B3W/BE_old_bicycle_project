package swp391.old_bicycle_project.exception;

import swp391.old_bicycle_project.dto.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApiResponse<?>> handlingRuntimeException(Exception exception) {
        log.error("Exception: ", exception);
        ApiResponse<?> apiResponse = new ApiResponse<>();

        apiResponse.setCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode());
        apiResponse.setMessage(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage());

        return ResponseEntity.status(ErrorCode.UNCATEGORIZED_EXCEPTION.getStatusCode()).body(apiResponse);
    }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse<?>> handlingAppException(AppException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        ApiResponse<?> apiResponse = new ApiResponse<>();

        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());

        return ResponseEntity.status(errorCode.getStatusCode().value()).body(apiResponse);
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    ResponseEntity<ApiResponse<?>> handlingAccessDeniedException(AccessDeniedException exception) {
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;

        return ResponseEntity.status(errorCode.getStatusCode().value())
                .body(ApiResponse.<Object>builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build());
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse<?>> handlingValidation(MethodArgumentNotValidException exception) {
        String validationMessage;
        if (exception.getFieldError() != null) {
            validationMessage = exception.getFieldError().getDefaultMessage();
        } else if (exception.getGlobalError() != null) {
            validationMessage = exception.getGlobalError().getDefaultMessage();
        } else {
            validationMessage = ErrorCode.INVALID_REQUEST_BODY.name();
        }

        ErrorCode errorCode = ErrorCode.INVALID_REQUEST_BODY;
        String responseMessage = validationMessage != null ? validationMessage : errorCode.getMessage();

        try {
            errorCode = ErrorCode.valueOf(validationMessage);
            responseMessage = errorCode.getMessage();
        } catch (IllegalArgumentException e) {
            log.debug("Validation message does not map to ErrorCode enum: {}", validationMessage);
        }

        ApiResponse<Object> apiResponse = new ApiResponse<>();

        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(responseMessage);

        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    ResponseEntity<ApiResponse<?>> handlingMalformedRequest(HttpMessageNotReadableException exception) {
        log.error("Malformed request body", exception);

        return ResponseEntity.badRequest().body(ApiResponse.builder()
                .code(ErrorCode.INVALID_REQUEST_BODY.getCode())
                .message(ErrorCode.INVALID_REQUEST_BODY.getMessage())
                .build());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<ApiResponse<?>> handlingIllegalArgumentException(IllegalArgumentException exception) {
        String message = exception.getMessage();

        return ResponseEntity.badRequest().body(ApiResponse.builder()
                .code(ErrorCode.INVALID_REQUEST_BODY.getCode())
                .message(message == null || message.isBlank()
                        ? ErrorCode.INVALID_REQUEST_BODY.getMessage()
                        : message)
                .build());
    }
}
