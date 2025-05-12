package com.example.demo.service.minio;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Service
public class FileUploadService {
    private final MinioStorageService minioStorage;

    public FileUploadService(MinioStorageService minioStorage) {
        this.minioStorage = minioStorage;
    }

    public String uploadPhoto(MultipartFile file) throws Exception {
        String objectName = "photos/" + System.currentTimeMillis() + "-" + file.getOriginalFilename();
        String contentType = file.getContentType();

        try (InputStream stream = file.getInputStream()) {
            minioStorage.uploadFromStream(objectName, stream, file.getSize(), contentType);
        }

        return objectName;
    }

    public String uploadVideo(MultipartFile file) throws Exception {
        String objectName = "videos/" + System.currentTimeMillis() + "-" + file.getOriginalFilename();
        String contentType = file.getContentType();

        try (InputStream stream = file.getInputStream()) {
            minioStorage.uploadFromStream(objectName, stream, file.getSize(), contentType);
        }

        return objectName;
    }
}
