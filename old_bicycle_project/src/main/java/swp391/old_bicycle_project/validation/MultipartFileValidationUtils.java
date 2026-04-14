package swp391.old_bicycle_project.validation;

import org.springframework.web.multipart.MultipartFile;
import swp391.old_bicycle_project.exception.AppException;
import swp391.old_bicycle_project.exception.ErrorCode;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public final class MultipartFileValidationUtils {

    private MultipartFileValidationUtils() {
    }

    public static List<MultipartFile> normalizeFiles(List<MultipartFile> files) {
        if (files == null || files.isEmpty()) {
            return List.of();
        }

        return files.stream()
                .filter(file -> file != null && !file.isEmpty())
                .toList();
    }

    public static void validateImageFiles(
            List<MultipartFile> files,
            int maxFiles,
            ErrorCode limitExceededError,
            ErrorCode invalidImageError
    ) {
        if (files.size() > maxFiles) {
            throw new AppException(limitExceededError);
        }

        for (MultipartFile file : files) {
            if (!isImageFile(file)) {
                throw new AppException(invalidImageError);
            }
        }
    }

    private static boolean isImageFile(MultipartFile file) {
        String contentType = normalizeContentType(file.getContentType());
        if (contentType == null || !contentType.startsWith("image/")) {
            return false;
        }

        try (InputStream inputStream = file.getInputStream()) {
            return ImageIO.read(inputStream) != null;
        } catch (IOException exception) {
            return false;
        }
    }

    private static String normalizeContentType(String contentType) {
        if (contentType == null) {
            return null;
        }

        String normalizedContentType = contentType.trim().toLowerCase();
        return normalizedContentType.isEmpty() ? null : normalizedContentType;
    }
}