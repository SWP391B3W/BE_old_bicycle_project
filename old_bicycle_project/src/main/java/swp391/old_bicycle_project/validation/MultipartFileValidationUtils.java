package swp391.old_bicycle_project.validation;

import swp391.old_bicycle_project.exception.AppException;
import swp391.old_bicycle_project.exception.ErrorCode;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public final class MultipartFileValidationUtils {

    private static final byte[] PDF_SIGNATURE = "%PDF-".getBytes(StandardCharsets.US_ASCII);
    private static final byte[] PNG_SIGNATURE = new byte[]{
            (byte) 0x89, 0x50, 0x4E, 0x47,
            0x0D, 0x0A, 0x1A, 0x0A
    };
    private static final byte[] JPEG_SIGNATURE = new byte[]{(byte) 0xFF, (byte) 0xD8, (byte) 0xFF};
    private static final byte[] GIF87A_SIGNATURE = "GIF87a".getBytes(StandardCharsets.US_ASCII);
    private static final byte[] GIF89A_SIGNATURE = "GIF89a".getBytes(StandardCharsets.US_ASCII);
    private static final byte[] RIFF_SIGNATURE = "RIFF".getBytes(StandardCharsets.US_ASCII);
    private static final byte[] WEBP_SIGNATURE = "WEBP".getBytes(StandardCharsets.US_ASCII);
    private static final byte[] BMP_SIGNATURE = "BM".getBytes(StandardCharsets.US_ASCII);
    private static final byte[] TIFF_LE_SIGNATURE = new byte[]{0x49, 0x49, 0x2A, 0x00};
    private static final byte[] TIFF_BE_SIGNATURE = new byte[]{0x4D, 0x4D, 0x00, 0x2A};
    private static final byte[] FTYP_SIGNATURE = "ftyp".getBytes(StandardCharsets.US_ASCII);

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

    public static void validateRequiredImages(
            List<MultipartFile> files,
            int minimumFiles,
            ErrorCode minimumFilesError,
            ErrorCode invalidImageError) {
        if (files.size() < minimumFiles) {
            throw new AppException(minimumFilesError);
        }

        validateImageFiles(files, invalidImageError);
    }

    public static void validateImageFiles(
            List<MultipartFile> files,
            int maxFiles,
            ErrorCode limitExceededError,
            ErrorCode invalidImageError) {
        if (files.size() > maxFiles) {
            throw new AppException(limitExceededError);
        }

        validateImageFiles(files, invalidImageError);
    }

    public static void validatePdfReport(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new AppException(ErrorCode.INVALID_REQUEST_BODY);
        }

        if (!isPdfFile(file)) {
            throw new AppException(ErrorCode.INSPECTION_REPORT_INVALID);
        }
    }

    private static void validateImageFiles(List<MultipartFile> files, ErrorCode invalidImageError) {
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

        if (isKnownImageBySignature(file)) {
            return true;
        }

        try (InputStream inputStream = file.getInputStream()) {
            return ImageIO.read(inputStream) != null;
        } catch (IOException exception) {
            return false;
        }
    }

    private static boolean isKnownImageBySignature(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            byte[] header = inputStream.readNBytes(32);
            if (header.length < 2) {
                return false;
            }

            if (startsWith(header, PNG_SIGNATURE)
                    || startsWith(header, JPEG_SIGNATURE)
                    || startsWith(header, GIF87A_SIGNATURE)
                    || startsWith(header, GIF89A_SIGNATURE)
                    || startsWith(header, BMP_SIGNATURE)
                    || startsWith(header, TIFF_LE_SIGNATURE)
                    || startsWith(header, TIFF_BE_SIGNATURE)) {
                return true;
            }

            // WEBP: RIFF....WEBP
            if (startsWith(header, RIFF_SIGNATURE)
                    && header.length >= 12
                    && matchesAt(header, 8, WEBP_SIGNATURE)) {
                return true;
            }

            // HEIC/HEIF: ....ftypheic|heix|hevc|hevx|mif1|msf1
            if (header.length >= 12 && matchesAt(header, 4, FTYP_SIGNATURE)) {
                String brand = new String(header, 8, 4, StandardCharsets.US_ASCII).toLowerCase();
                return brand.equals("heic")
                        || brand.equals("heix")
                        || brand.equals("hevc")
                        || brand.equals("hevx")
                        || brand.equals("mif1")
                        || brand.equals("msf1");
            }

            return false;
        } catch (IOException exception) {
            return false;
        }
    }

    private static boolean startsWith(byte[] source, byte[] prefix) {
        return matchesAt(source, 0, prefix);
    }

    private static boolean matchesAt(byte[] source, int offset, byte[] pattern) {
        if (source.length < offset + pattern.length) {
            return false;
        }

        for (int index = 0; index < pattern.length; index++) {
            if (source[offset + index] != pattern[index]) {
                return false;
            }
        }

        return true;
    }

    private static boolean isPdfFile(MultipartFile file) {
        String contentType = normalizeContentType(file.getContentType());
        boolean pdfContentType = "application/pdf".equals(contentType);
        boolean pdfExtension = hasPdfExtension(file.getOriginalFilename());

        if (!pdfContentType && !pdfExtension) {
            return false;
        }

        try (InputStream inputStream = file.getInputStream()) {
            byte[] header = inputStream.readNBytes(PDF_SIGNATURE.length);
            return Arrays.equals(PDF_SIGNATURE, header);
        } catch (IOException exception) {
            return false;
        }
    }

    private static boolean hasPdfExtension(String originalFilename) {
        if (originalFilename == null) {
            return false;
        }

        return originalFilename.trim().toLowerCase().endsWith(".pdf");
    }

    private static String normalizeContentType(String contentType) {
        if (contentType == null) {
            return null;
        }

        String normalizedContentType = contentType.trim().toLowerCase();
        return normalizedContentType.isEmpty() ? null : normalizedContentType;
    }
}
