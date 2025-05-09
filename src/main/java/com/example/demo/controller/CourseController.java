package com.example.demo.controller;

import com.example.demo.dto.course.CourseGetRq;
import com.example.demo.dto.user.UserRegistrationRq;
import com.example.demo.service.course.GetCourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/lms/v1")
@Validated
public class CourseController {
    private final GetCourseService getCourseService;
    @PostMapping("/get-course")
    public ResponseEntity<String> getAllCourse(@RequestBody @Valid CourseGetRq courseGetRq,
                                               @RequestHeader String rqUid){
        return getCourseService.getCourse(courseGetRq, rqUid);
    }

}