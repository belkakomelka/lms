package com.example.demo.service.course;

import com.example.demo.database.entity.Course;
import com.example.demo.database.entity.Tag;
import com.example.demo.database.repository.CourseRepository;
import com.example.demo.dto.course.CourseGetFiltersRq;
import com.example.demo.dto.course.CourseGetRq;
import com.example.demo.dto.course.CourseGetRs;
import com.example.demo.dto.course.UploadCourseRq;
import com.example.demo.service.minio.FileUploadService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UploadCourseService {

    private final CourseRepository courseRepository;

    private final ObjectMapper objectMapping;

    private final FileUploadService fileUploadService;


    public ResponseEntity<String> upload(UploadCourseRq uploadCourseRq, MultipartFile photo, String rqUid) {
        try {
            String objectName = fileUploadService.uploadPhoto(photo);
            return ResponseEntity.ok("Photo uploaded successfully: " + objectName);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload photo: " + e.getMessage());
        }
    }

}
