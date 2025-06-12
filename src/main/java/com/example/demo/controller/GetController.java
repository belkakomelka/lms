package com.example.demo.controller;

import com.example.demo.dto.course.CourseGetRq;
import com.example.demo.dto.module.ModuleGetRq;
import com.example.demo.service.achievements.GetAchievementsService;
import com.example.demo.service.course.GetCourseService;
import com.example.demo.service.module.GetModuleService;
import com.example.demo.service.tag.GetTagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<String> getAllAchievements(@RequestHeader String rqUid){
        return getAchievementsService.getAchievements(rqUid);
    }

    @PostMapping("/get-module")  // получить модуль внутри курса
    public ResponseEntity<String> getModule(@RequestBody @Valid ModuleGetRq moduleGetRq,
                                               @RequestHeader String rqUid){
        return getModuleService.getCourse(moduleGetRq, rqUid);
    }

}
// todo
// отдать курс

// обновление текущего курса
// обновление модуля

// отбивка о прохождении курса = один флаг сделать true
// отбивка о прохождении модуля = один флаг сделать true

// привязка человека к курсу

// расчет completion percentage -> достать пройденные модули -> пройденные/все модули курса
