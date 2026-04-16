package swp391.old_bicycle_project.service;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

    String uploadFile(MultipartFile file, String folder);

    String uploadFile(MultipartFile file, String folder, String bucketName);

    void deleteFile(String fileUrl);
}
