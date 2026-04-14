package swp391.old_bicycle_project.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import swp391.old_bicycle_project.exception.AppException;
import swp391.old_bicycle_project.exception.ErrorCode;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.UUID;

@Service
public class StorageService {

    private static final String PUBLIC_OBJECT_PATH = "/storage/v1/object/public/";
    private static final String PRIVATE_OBJECT_PATH = "/storage/v1/object/";

    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(15))
            .build();

    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.anon-key}")
    private String supabaseAnonKey;

    @Value("${supabase.service-role-key:}")
    private String supabaseServiceRoleKey;

    @Value("${supabase.storage.bucket:product-images}")
    private String bucket;

    public String uploadFile(MultipartFile file, String folder) {
        return uploadFile(file, folder, bucket);
    }

    public String uploadFile(MultipartFile file, String folder, String bucketName) {
        if (file == null || file.isEmpty()) {
            throw new AppException(ErrorCode.INVALID_REQUEST_BODY);
        }

        String normalizedBucket = normalizeBucket(bucketName);
        String normalizedFolder = normalizeFolder(folder);
        String filename = UUID.randomUUID() + "_" + sanitizeFilename(file.getOriginalFilename());
        String path = normalizedFolder + "/" + filename;

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(buildObjectUrl(normalizedBucket, path)))
                    .timeout(Duration.ofSeconds(30))
                    .header("apikey", resolveStorageApiKey())
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + resolveStorageApiKey())
                    .header(HttpHeaders.CONTENT_TYPE, resolveContentType(file.getContentType()).toString())
                    .POST(HttpRequest.BodyPublishers.ofByteArray(file.getBytes()))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                return buildPublicUrl(normalizedBucket, path);
            }

            throw new RuntimeException("Upload file thất bại: " + response.statusCode());
        } catch (IOException exception) {
            throw new RuntimeException("Không thể đọc file: " + exception.getMessage(), exception);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Upload file bị gián đoạn", exception);
        }
    }

    public void deleteFile(String fileUrl) {
        StorageObjectLocation storageObjectLocation = resolveStorageObjectLocation(fileUrl);
        if (storageObjectLocation == null) {
            return;
        }

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(buildObjectUrl(storageObjectLocation.bucket(), storageObjectLocation.path())))
                    .timeout(Duration.ofSeconds(15))
                    .header("apikey", resolveStorageApiKey())
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + resolveStorageApiKey())
                    .DELETE()
                    .build();

            httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception ignored) {
            // Best-effort cleanup only.
        }
    }

    private String resolveStorageApiKey() {
        if (supabaseServiceRoleKey != null && !supabaseServiceRoleKey.isBlank()
                && !"your_supabase_service_role_key".equals(supabaseServiceRoleKey)) {
            return supabaseServiceRoleKey;
        }
        return supabaseAnonKey;
    }

    private String normalizeBucket(String bucketName) {
        if (bucketName == null || bucketName.isBlank()) {
            throw new AppException(ErrorCode.INVALID_REQUEST_BODY);
        }

        String normalizedBucket = bucketName.trim();
        if (normalizedBucket.contains("/") || normalizedBucket.contains("\\")) {
            throw new AppException(ErrorCode.INVALID_REQUEST_BODY);
        }

        return normalizedBucket;
    }

    private String normalizeFolder(String folder) {
        if (folder == null || folder.isBlank()) {
            throw new AppException(ErrorCode.INVALID_REQUEST_BODY);
        }

        String normalizedFolder = folder.trim()
                .replace('\\', '/')
                .replaceAll("/+", "/");

        if (normalizedFolder.startsWith("/")) {
            normalizedFolder = normalizedFolder.substring(1);
        }
        if (normalizedFolder.endsWith("/")) {
            normalizedFolder = normalizedFolder.substring(0, normalizedFolder.length() - 1);
        }
        if (normalizedFolder.isBlank() || normalizedFolder.contains("..")) {
            throw new AppException(ErrorCode.INVALID_REQUEST_BODY);
        }

        return normalizedFolder;
    }

    private String sanitizeFilename(String originalFilename) {
        String cleanedPath = StringUtils.cleanPath(originalFilename != null ? originalFilename : "");
        String filename = cleanedPath.replace('\\', '/');
        int lastSlashIndex = filename.lastIndexOf('/');
        if (lastSlashIndex >= 0) {
            filename = filename.substring(lastSlashIndex + 1);
        }

        String sanitizedFilename = filename.trim()
                .replace(' ', '_')
                .replaceAll("[^A-Za-z0-9._-]", "_")
                .replaceAll("^\\.+", "");

        if (sanitizedFilename.isBlank()) {
            return "file";
        }

        return sanitizedFilename.length() <= 120
                ? sanitizedFilename
                : sanitizedFilename.substring(sanitizedFilename.length() - 120);
    }

    private MediaType resolveContentType(String contentType) {
        if (contentType == null || contentType.isBlank()) {
            return MediaType.APPLICATION_OCTET_STREAM;
        }

        try {
            return MediaType.parseMediaType(contentType);
        } catch (Exception exception) {
            return MediaType.APPLICATION_OCTET_STREAM;
        }
    }

    private String buildObjectUrl(String bucketName, String path) {
        return supabaseUrl + PRIVATE_OBJECT_PATH + bucketName + "/" + path;
    }

    private String buildPublicUrl(String bucketName, String path) {
        return supabaseUrl + PUBLIC_OBJECT_PATH + bucketName + "/" + path;
    }

    private StorageObjectLocation resolveStorageObjectLocation(String fileUrl) {
        String publicPrefix = supabaseUrl + PUBLIC_OBJECT_PATH;
        if (fileUrl == null || !fileUrl.startsWith(publicPrefix)) {
            return null;
        }

        String relativePath = fileUrl.substring(publicPrefix.length());
        int firstSlashIndex = relativePath.indexOf('/');
        if (firstSlashIndex <= 0 || firstSlashIndex == relativePath.length() - 1) {
            return null;
        }

        return new StorageObjectLocation(
                relativePath.substring(0, firstSlashIndex),
                relativePath.substring(firstSlashIndex + 1)
        );
    }

    private record StorageObjectLocation(String bucket, String path) {
    }
}