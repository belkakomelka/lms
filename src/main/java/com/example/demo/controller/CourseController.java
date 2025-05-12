package com.example.demo.controller;

import com.example.demo.dto.course.CourseGetRq;
import com.example.demo.dto.course.UploadCourseRq;
import com.example.demo.dto.user.UserRegistrationRq;
import com.example.demo.service.course.GetCourseService;
import com.example.demo.service.course.UploadCourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("/lms/v1")
@Validated
public class CourseController {
    private final GetCourseService getCourseService;
    private final UploadCourseService uploadCourseService;
    @PostMapping("/get-course")
    public ResponseEntity<String> getAllCourse(@RequestBody @Valid CourseGetRq courseGetRq,
                                               @RequestHeader String rqUid){
        return getCourseService.getCourse(courseGetRq, rqUid);
    }

    @PostMapping(path = "/course", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<String> uploadCourse(@RequestPart("data") UploadCourseRq uploadCourseRq,  // todo потом валид
                                               @RequestPart("photo") MultipartFile photo,
                                               @RequestHeader String rqUid){
        return uploadCourseService.upload(uploadCourseRq, photo, rqUid);
    }
}