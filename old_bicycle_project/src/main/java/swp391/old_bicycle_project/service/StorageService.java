package swp391.old_bicycle_project.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * Interface for file storage operations.
 */
public interface StorageService {

    /**
     * Upload file to default bucket.
     */
    String uploadFile(MultipartFile file, String folder);

    /**
     * Upload file to specific bucket.
     */
    String uploadFile(MultipartFile file, String folder, String bucketName);

    /**
     * Delete file by its public URL.
     */
    void deleteFile(String fileUrl);
}
