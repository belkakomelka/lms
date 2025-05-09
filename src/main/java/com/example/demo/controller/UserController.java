package com.example.demo.controller;

import com.example.demo.dto.user.UserGetRq;
import com.example.demo.dto.user.UserRegistrationRq;
import com.example.demo.service.user.AddUserService;
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
@RequestMapping("/lms/v1")
@Validated
public class UserController {

    private final AddUserService addUserService;

    private final GetUserInfoService getUserInfoService;

    @PostMapping("/user")
    public ResponseEntity<String> addUser(@RequestBody @Valid UserRegistrationRq userRegistrationRq,
                                          @RequestHeader String rqUid) {
        return addUserService.addUser(userRegistrationRq, rqUid);
    }


    @GetMapping("/user/achievements/{userId}")
    public ResponseEntity<String> getUser(@PathVariable("userId") String userId,
                                          @RequestHeader String rqUid) {
        return getUserInfoService.getUserAchievements(userId, rqUid);
    }
}