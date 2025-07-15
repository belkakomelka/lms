package com.example.demo.service.minio;

import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

public class MinioStorageService {
    private final MinioClient minioInternalClient;
    private final String bucketName;

    public MinioStorageService(String endpoint, String accessKey, String secretKey, String bucketName)
            throws Exception {
        this.minioInternalClient = MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
        this.bucketName = bucketName;

        initializeBucket();
    }

    private void initializeBucket() throws Exception {
        boolean found = minioInternalClient.bucketExists(BucketExistsArgs.builder()
                .bucket(bucketName)
                .build());

        if (!found) {
            minioInternalClient.makeBucket(MakeBucketArgs.builder()
                    .bucket(bucketName)
                    .build());
        }
    }

    public void uploadFile(String objectName, String filePath, String contentType)
            throws Exception {
        minioInternalClient.uploadObject(
                UploadObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .filename(filePath)
                        .contentType(contentType)
                        .build());
    }

    public void uploadFromStream(String objectName, InputStream stream, long size, String contentType)
            throws Exception {
        minioInternalClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .stream(stream, size, -1)
                        .contentType(contentType)
                        .build());
    }

    public String generatePresignedUrl(String objectKey) {
        try {
            String presignedUrl = minioInternalClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucketName)
                            .object(objectKey)
                            .expiry(1, TimeUnit.HOURS)
                            .build()
            );
            return presignedUrl.replace("minio:9000", "localhost:8082"); // todo потом вынести в конфиг

        } catch (ServerException | ErrorResponseException | IOException | InsufficientDataException |
                 NoSuchAlgorithmException | InvalidKeyException | InvalidResponseException | XmlParserException |
                 InternalException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteFile(String objectName) throws Exception {
        minioInternalClient.removeObject(
                RemoveObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .build());
    }
}
