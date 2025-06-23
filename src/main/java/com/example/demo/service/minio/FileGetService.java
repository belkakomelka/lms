package com.example.demo.service.minio;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Service
public class FileGetService {
    private final MinioStorageService minioStorage;

    public FileGetService(MinioStorageService minioStorage) {
        this.minioStorage = minioStorage;
    }

    public String getContent(String fileName) {
         return minioStorage.generatePresignedUrl(fileName);
    }
}
