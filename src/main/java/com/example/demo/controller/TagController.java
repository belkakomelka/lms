package com.example.demo.controller;

import com.example.demo.dto.course.CourseGetRq;
import com.example.demo.dto.course.CourseUploadRq;
import com.example.demo.dto.tag.TagUploadRq;
import com.example.demo.service.course.GetCourseService;
import com.example.demo.service.course.UploadCourseService;
import com.example.demo.service.tag.UploadTagService;
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
public class TagController {
    private final UploadTagService uploadTagService;
    @PostMapping(path = "/tag")
    public ResponseEntity<String> uploadTag(@RequestBody @Valid TagUploadRq tagUploadRq,
                                               @RequestHeader String rqUid){
        return uploadTagService.upload(tagUploadRq, rqUid);
    }
}
