package com.example.demo.controller;

import com.example.demo.dto.achievement.AchievementUploadRq;
import com.example.demo.dto.course.CourseUploadRq;
import com.example.demo.dto.module.ModuleUploadRq;
import com.example.demo.dto.tag.TagUploadRq;
import com.example.demo.service.achievements.UploadAchievementsService;
import com.example.demo.service.course.UploadCourseService;
import com.example.demo.service.module.UploadModuleService;
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
@RequestMapping("/lms/v1/upload")
@Validated
public class UploadController {
    private final UploadTagService uploadTagService;
    private final UploadModuleService uploadModuleService;
    private final UploadCourseService uploadCourseService;
    private final UploadAchievementsService uploadAchievementsService;
    @PostMapping(path = "/tag") // загрузить тег
    public ResponseEntity<String> uploadTag(@RequestBody @Valid TagUploadRq tagUploadRq,
                                               @RequestHeader String rqUid){
        return uploadTagService.upload(tagUploadRq, rqUid);
    }

    @PostMapping(path = "/module", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<String> uploadModule(@RequestPart("moduleUploadRq") @Valid ModuleUploadRq moduleUploadRq,
                                               @RequestPart("video") MultipartFile video,
                                               @RequestHeader String rqUid){
        // загрузить модуль
        return uploadModuleService.upload(moduleUploadRq, video, rqUid); // todo отдать мультипартом Владу
    }

    @PostMapping(path = "/course", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<String> uploadCourse(@RequestPart("uploadCourseRq") @Valid CourseUploadRq courseUploadRq,
                                               @RequestPart("image") MultipartFile photo,
                                               @RequestHeader String rqUid){ // загрузить курс
        return uploadCourseService.upload(courseUploadRq, photo, rqUid); //todo когда загружают курс провреять не было ли такого тега и просто маппить с ним
    }

    @PostMapping(path = "/achievement", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE }) // загрузить ачивку
    public ResponseEntity<String> uploadAchievement(@RequestPart("uploadAchievementRq") @Valid AchievementUploadRq achievementUploadRq,
                                               @RequestPart("image") MultipartFile photo,
                                               @RequestHeader String rqUid){
        return uploadAchievementsService.upload(achievementUploadRq, photo, rqUid);
    }
}
