package swp391.old_bicycle_project.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import swp391.old_bicycle_project.exception.AppException;
import swp391.old_bicycle_project.exception.ErrorCode;
import swp391.old_bicycle_project.service.StorageService;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.UUID;

@Service
public class StorageServiceImpl implements StorageService {

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

    @Override
    public String uploadFile(MultipartFile file, String folder) {
        return uploadFile(file, folder, bucket);
    }

    @Override
    public String uploadFile(MultipartFile file, String folder, String bucketName) {
        if (file == null || file.isEmpty()) {
            throw new AppException(ErrorCode.INVALID_REQUEST_BODY);
        }

        String normalizedBucket = normalizeBucket(bucketName);
        String normalizedFolder = normalizeFolder(folder);
        String filename = UUID.randomUUID() + "_" + sanitizeFilename(file.getOriginalFilename());
        String path = normalizedFolder + "/" + filename;
        String apiKey = resolveStorageApiKey();
        String bearerToken = resolveBearerToken(apiKey);

        try {
            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create(buildObjectUrl(normalizedBucket, path)))
                    .timeout(Duration.ofSeconds(30))
                    .header(HttpHeaders.CONTENT_TYPE, resolveContentType(file.getContentType()).toString())
                    .header("apikey", apiKey)
                    .POST(HttpRequest.BodyPublishers.ofByteArray(file.getBytes()));

            if (bearerToken != null) {
                requestBuilder.header(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken);
            }

            HttpRequest request = requestBuilder.build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                return buildPublicUrl(normalizedBucket, path);
            }

            String responseBody = response.body() == null ? "" : response.body().trim();
            throw new RuntimeException("Upload file thất bại: HTTP " + response.statusCode()
                    + (responseBody.isEmpty() ? "" : " - " + responseBody));
        } catch (IOException exception) {
            throw new RuntimeException("Không thể đọc file: " + exception.getMessage(), exception);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Upload file bị gián đoạn", exception);
        }
    }

    @Override
    public void deleteFile(String fileUrl) {
        StorageObjectLocation storageObjectLocation = resolveStorageObjectLocation(fileUrl);
        if (storageObjectLocation == null) {
            return;
        }

        String apiKey = resolveStorageApiKey();
        String bearerToken = resolveBearerToken(apiKey);

        try {
            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create(buildObjectUrl(storageObjectLocation.bucket(), storageObjectLocation.path())))
                    .timeout(Duration.ofSeconds(15))
                    .header("apikey", apiKey)
                    .DELETE();

            if (bearerToken != null) {
                requestBuilder.header(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken);
            }

            HttpRequest request = requestBuilder.build();

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

    private String resolveBearerToken(String selectedApiKey) {
        if (isJwtLike(selectedApiKey)) {
            return selectedApiKey;
        }

        if (isJwtLike(supabaseAnonKey)) {
            return supabaseAnonKey;
        }

        return null;
    }

    private boolean isJwtLike(String token) {
        if (token == null || token.isBlank()) {
            return false;
        }

        int dotCount = 0;
        for (int index = 0; index < token.length(); index++) {
            if (token.charAt(index) == '.') {
                dotCount++;
            }
        }
        return dotCount == 2;
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
        return normalizeSupabaseUrl() + PRIVATE_OBJECT_PATH + bucketName + "/" + path;
    }

    private String buildPublicUrl(String bucketName, String path) {
        return normalizeSupabaseUrl() + PUBLIC_OBJECT_PATH + bucketName + "/" + path;
    }

    private StorageObjectLocation resolveStorageObjectLocation(String fileUrl) {
        String publicPrefix = normalizeSupabaseUrl() + PUBLIC_OBJECT_PATH;
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

    private String normalizeSupabaseUrl() {
        if (supabaseUrl == null) {
            throw new AppException(ErrorCode.INVALID_REQUEST_BODY);
        }

        String normalizedUrl = supabaseUrl.trim();
        if (normalizedUrl.endsWith("/")) {
            normalizedUrl = normalizedUrl.substring(0, normalizedUrl.length() - 1);
        }
        return normalizedUrl;
    }

    private record StorageObjectLocation(String bucket, String path) {
    }
}
