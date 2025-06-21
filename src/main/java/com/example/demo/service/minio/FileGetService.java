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

    public InputStream getContent(String fileName) throws Exception {
         return minioStorage.downloadFile(fileName);
    }
}
