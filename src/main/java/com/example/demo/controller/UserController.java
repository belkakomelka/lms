package com.example.demo.controller;

import com.example.demo.dto.course.CourseGetRq;
import com.example.demo.dto.user.*;
import com.example.demo.service.user.AddUserService;
import com.example.demo.service.user.CompletionService;
import com.example.demo.service.user.GetUserInfoService;
import com.example.demo.service.user.TakeCourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/lms/v1/user")
@Validated
public class UserController {

    private final AddUserService addUserService;
    private final GetUserInfoService getUserInfoService;
    private final CompletionService completionService;
    private final TakeCourseService takeCourseService;

    @PostMapping("/user")
    public ResponseEntity<String> addUser(@RequestBody @Valid UserRegistrationRq userRegistrationRq,
                                          @RequestHeader String rqUid) {
        return addUserService.addUser(userRegistrationRq, rqUid);
    }


    @GetMapping("achievements/{userId}")
    public ResponseEntity<String> getUserAchievements(@PathVariable("userId") String userId,
                                          @RequestHeader String rqUid) {
        return getUserInfoService.getUserAchievements(userId, rqUid);
    }

    @PostMapping("complete-module")
    public ResponseEntity<String> completeModule(@RequestBody @Valid CompleteModuleRq completeModuleRq,
                                                 @RequestHeader String rqUid) {
        return completionService.completeModule(completeModuleRq, rqUid);
    }

    @PostMapping("complete-course")
    public ResponseEntity<String> completeCourse(@RequestBody @Valid CompleteCourseRq completeCourseRq,
                                                 @RequestHeader String rqUid) {
        return completionService.completeCourse(completeCourseRq, rqUid);
    }

    @PostMapping("take-course")
    public ResponseEntity<String> takeCourse(@RequestBody @Valid TakeCourseRq takeCourseRq,
                                             @RequestHeader String rqUid){
        return takeCourseService.takeCourse(takeCourseRq, rqUid);
    }
}