package com.example.demo.controller;

import com.example.demo.dto.course.CourseGetRq;
import com.example.demo.dto.module.ModuleGetRq;
import com.example.demo.dto.module.ModulesGetRq;
import com.example.demo.service.achievements.GetAchievementsService;
import com.example.demo.service.course.GetCourseService;
import com.example.demo.service.module.GetModuleService;
import com.example.demo.service.tag.GetTagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
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
        // получить список курсов(есть фильтрация по тегам для общего меню, а еще по userId для показа курсов)
        return getCourseService.getCourse(courseGetRq, rqUid);
    }

    @GetMapping("get-tags") // получить все теги из бд
    public ResponseEntity<String> getAllTags(@RequestHeader String rqUid){
        return getTagService.getTag(rqUid);
    }

    @GetMapping("get-achievements") // получить все ачивки, занесенные в бд
    public ResponseEntity<String> getAllAchievements(@RequestHeader String rqUid){
        return getAchievementsService.getAchievements(rqUid);
    }

    @PostMapping("/get-modules")  // получить список модулей внутри курса
    public ResponseEntity<String> getModules(@RequestBody @Valid ModulesGetRq modulesGetRq,
                                            @RequestHeader String rqUid){
        return getModuleService.getModules(modulesGetRq, rqUid);
    }

    @PostMapping(value = "/get-module", produces = MediaType.MULTIPART_MIXED_VALUE)  // получить модуль для демонстрации с контентом
    public ResponseEntity<MultiValueMap<String, Object>> getModule(@RequestBody @Valid ModuleGetRq moduleGetRq,
                                                                   @RequestHeader String rqUid){
        return getModuleService.getModule(moduleGetRq, rqUid);
    }

}
// todo

// обновление текущего курса
// обновление модуля

// отбивка о прохождении курса = один флаг сделать true
// отбивка о прохождении модуля = один флаг сделать true

// привязка человека к курсу

// расчет completion percentage -> достать пройденные модули -> пройденные/все модули курса
