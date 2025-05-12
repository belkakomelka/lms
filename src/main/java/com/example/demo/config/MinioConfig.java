package com.example.demo.config;

import com.example.demo.service.minio.MinioStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfig {
    @Value("${minio.endpoint}")
    private String endpoint;

    @Value("${minio.access-key}")
    private String accessKey;

    @Value("${minio.secret-key}")
    private String secretKey;

    @Value("${minio.bucket-name}")
    private String bucketName;

    @Bean
    public MinioStorageService minioStorageService() throws Exception {
        return new MinioStorageService(endpoint, accessKey, secretKey, bucketName);
    }
}