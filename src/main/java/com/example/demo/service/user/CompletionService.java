package com.example.demo.service.user;

import com.example.demo.database.entity.*;
import com.example.demo.database.repository.*;
import com.example.demo.dto.user.CompleteCourseRq;
import com.example.demo.dto.user.CompleteModuleRq;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CompletionService {

    private final UserToModuleRepository userToModuleRepository;

    private final UserRepository userRepository;
    private final ModuleRepository moduleRepository;
    private final CourseRepository courseRepository;
    private final UserToCourseRepository userToCourseRepository;

    public ResponseEntity<String> completeModule(CompleteModuleRq completeModuleRq, String rqUid){
        try {
            log.info(String.format("Принят запрос для отметки пользователя об окончании модуля с userId = %s, rqUid = %s", completeModuleRq.getUserId(), rqUid));

            Optional<User> userOptional = userRepository.findUserByUserToken(completeModuleRq.getUserId());
            Optional<ModuleCourse> moduleCourseOptional = moduleRepository.findById(completeModuleRq.getModuleId());

            if (userOptional.isEmpty() || moduleCourseOptional.isEmpty()){
                log.info(String.format("Пользователь отсутствует или информация о модуле не найдена, rqUid = %s", rqUid));
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            userToModuleRepository.save(buildUserToModuleEntity(userOptional.get(), moduleCourseOptional.get()));
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (RuntimeException e) {
            log.error(String.format("Внутрення ошибка сервиса %s, rqUid = %s", e.getMessage(), rqUid));
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<String> completeCourse(CompleteCourseRq completeCourseRq, String rqUid){
        try {
            log.info(String.format("Принят запрос для отметки пользователя об окончании курса с userId = %s, rqUid = %s", completeCourseRq.getUserId(), rqUid));

            Optional<User> userOptional = userRepository.findUserByUserToken(completeCourseRq.getUserId());
            Optional<Course> courseOptional = courseRepository.findById(completeCourseRq.getCourseId());
            List<ModuleCourse> moduleCourseList = moduleRepository.findByCourseIdOrderByModuleOrderAsc(completeCourseRq.getCourseId());

            if (userOptional.isEmpty() || courseOptional.isEmpty() || moduleCourseList.isEmpty()){
                log.info(String.format("Пользователь отсутствует или информация о модуле не найдена, rqUid = %s", rqUid));
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            List<UserToModule> userModules = userToModuleRepository.findByUserAndModuleCourseIn(userOptional.get(), moduleCourseList);

            boolean allModulesCompleted = moduleCourseList.stream()
                    .allMatch(module ->
                            userModules.stream()
                                    .anyMatch(userModule ->
                                            userModule.getModule().equals(module) &&
                                                    Boolean.TRUE.equals(userModule.getIsComplete())
                                    )
                    );

            if (!allModulesCompleted) {
                log.info(String.format("Не все модули курса завершены пользователем, userId = %s, courseId = %s, rqUid = %s",
                        completeCourseRq.getUserId(), completeCourseRq.getCourseId(), rqUid));
                return new ResponseEntity<>("Not all modules are completed", HttpStatus.BAD_REQUEST);
            }

            userToCourseRepository.save(buildUserToCourse(userOptional.get(), courseOptional.get()));
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (RuntimeException e) {
            log.error(String.format("Внутрення ошибка сервиса %s, rqUid = %s", e.getMessage(), rqUid));
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    private UserToCourse buildUserToCourse(User user, Course course){
        return UserToCourse.builder()
                .course(course)
                .percentageOfCompletion(100)
                .user(user)
                .build();
    }
    private UserToModule buildUserToModuleEntity(User user, ModuleCourse moduleCourse){
        return UserToModule.builder()
                .isComplete(true)
                .module(moduleCourse)
                .user(user)
                .build();
    }
}
