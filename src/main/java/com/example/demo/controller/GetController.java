package com.example.demo.controller;

import com.example.demo.dto.course.CourseGetRq;
import com.example.demo.service.achievements.GetAchievementsService;
import com.example.demo.service.course.GetCourseService;
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

}
// todo
// отдать курс
// отдать модуль

// обновление текущего курса
// обновление модуля

// отбивка о прохождении курса = один флаг сделать true
// отбивка о прохождении модуля = один флаг сделать true

// привязка человека к курсу

// расчет completion percentage -> достать пройденные модули -> пройденные/все модули курса
