package com.example.demo.controller;

import com.example.demo.dto.course.CourseGetRq;
import com.example.demo.dto.module.ModuleGetRq;
import com.example.demo.dto.module.ModulesGetRq;
import com.example.demo.service.achievements.GetAchievementsService;
import com.example.demo.service.course.GetCourseService;
import com.example.demo.service.module.GetModuleService;
import com.example.demo.service.tag.GetTagService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

// todo
// обновление текущего курса
// обновление модуля


@RequiredArgsConstructor
@RestController
@RequestMapping("/lms/v1")
@Validated
public class GetController {
    private final GetCourseService getCourseService;
    private final GetTagService getTagService;
    private final GetAchievementsService getAchievementsService;
    private final GetModuleService getModuleService;

    @PostMapping("/get-course")
    public ResponseEntity<String> getAllCourse(@RequestBody @Valid CourseGetRq courseGetRq,
                                               @RequestHeader String rqUid){
        return getCourseService.getCourse(courseGetRq, rqUid);
    }

    @GetMapping("get-tags")
    public ResponseEntity<String> getAllTags(@RequestHeader String rqUid){
        return getTagService.getTag(rqUid);
    }

    @GetMapping("get-achievements")
    public ResponseEntity<String> getAllAchievements(@RequestHeader String rqUid) throws JsonProcessingException {
        return getAchievementsService.getAchievements(rqUid);
    }

    @PostMapping("/get-modules")
    public ResponseEntity<String> getModules(@RequestBody @Valid ModulesGetRq modulesGetRq,
                                            @RequestHeader String rqUid){
        return getModuleService.getModules(modulesGetRq, rqUid);
    }

    @PostMapping(value = "/get-module")
    public ResponseEntity<String> getModule(@RequestBody @Valid ModuleGetRq moduleGetRq,
                                                                   @RequestHeader String rqUid){
        return getModuleService.getModule(moduleGetRq, rqUid);
    }

}