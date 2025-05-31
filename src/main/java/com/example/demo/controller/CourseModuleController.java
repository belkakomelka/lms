package com.example.demo.controller;

import com.example.demo.dto.course.CourseUploadRq;
import com.example.demo.dto.module.ModuleUploadRq;
import com.example.demo.service.module.UploadModuleService;
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
public class CourseModuleController {
    private final UploadModuleService uploadModuleService;
    @PostMapping(path = "/module", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<String> uploadModule(@RequestPart("moduleUploadRq") @Valid ModuleUploadRq moduleUploadRq,
                                               @RequestPart("video") MultipartFile video,
                                               @RequestHeader String rqUid){
        return uploadModuleService.upload(moduleUploadRq, video, rqUid);
    }
}
