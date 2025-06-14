package com.example.demo.controller;

import com.example.demo.dto.course.CourseGetRq;
import com.example.demo.dto.user.CompleteModuleRq;
import com.example.demo.dto.user.UserGetRq;
import com.example.demo.dto.user.UserRegistrationRq;
import com.example.demo.service.user.AddUserService;
import com.example.demo.service.user.CompletionService;
import com.example.demo.service.user.GetUserInfoService;
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

    @PostMapping("/")
    public ResponseEntity<String> addUser(@RequestBody @Valid UserRegistrationRq userRegistrationRq,
                                          @RequestHeader String rqUid) {
        return addUserService.addUser(userRegistrationRq, rqUid); // добавить пользователя в БД
    }


    @GetMapping("achievements/{userId}")
    public ResponseEntity<String> getUserAchievements(@PathVariable("userId") String userId,
                                          @RequestHeader String rqUid) { // получить ачивки пользователя
        return getUserInfoService.getUserAchievements(userId, rqUid);
    }

    @PostMapping("complete-module")
    public ResponseEntity<String> completeModule(@RequestBody @Valid CompleteModuleRq completeModuleRq,
                                                 @RequestHeader String rqUid) {
        return completionService.completeModule(completeModuleRq, rqUid); // отметить пройденный модуль для пользователя
    }
}