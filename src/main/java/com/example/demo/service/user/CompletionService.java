package com.example.demo.service.user;

import com.example.demo.database.entity.*;
import com.example.demo.database.repository.*;
import com.example.demo.dto.ExceptionRs;
import com.example.demo.dto.user.CompleteCourseRq;
import com.example.demo.dto.user.CompleteModuleRq;
import com.fasterxml.jackson.core.JsonProcessingException;
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

    private final ObjectMapper objectMapping;

    // todo тут пока нет присваивания ачивки для пользователя
    public ResponseEntity<String> completeModule(CompleteModuleRq completeModuleRq, String rqUid){
        try {
            log.info(String.format("Принят запрос для отметки пользователя об окончании модуля с userId = %s, rqUid = %s", completeModuleRq.getUserId(), rqUid));

            Optional<User> userOptional = userRepository.findUserByUserToken(completeModuleRq.getUserId());
            Optional<ModuleCourse> moduleCourseOptional = moduleRepository.findById(completeModuleRq.getModuleId());

            if (userOptional.isEmpty() || moduleCourseOptional.isEmpty()){
                log.info(String.format("Пользователь отсутствует или информация о модуле не найдена, rqUid = %s", rqUid));
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            Optional<UserToCourse> userToCourseOptional = userToCourseRepository.findByUserAndCourse(userOptional.get(), moduleCourseOptional.get().getCourse());

            if (userToCourseOptional.isEmpty()){
                log.info(String.format("У пользователя нет активного курса, в начале необходимо выбрать курс, rqUid = %s", rqUid));
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            Optional<UserToModule> userToModuleOptional = userToModuleRepository.findByUserAndModule(userOptional.get(), moduleCourseOptional.get());
            if (userToModuleOptional.isEmpty()){
                log.info(String.format("У пользователя нет активного модуля, rqUid = %s", rqUid));
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            UserToModule userToModule = userToModuleOptional.get();
            userToModule.setIsComplete(true);
            userToModuleRepository.save(userToModule);

            UserToCourse userToCourse = userToCourseOptional.get();
            userToCourse.setPercentageOfCompletion(updateCompletionStage(moduleCourseOptional.get()));
            userToCourseRepository.save(userToCourse);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (RuntimeException e) {
            log.error(String.format("Внутрення ошибка сервиса %s, rqUid = %s", e.getMessage(), rqUid));
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private Integer updateCompletionStage(ModuleCourse moduleCourse){
        Long amountOfModulesInCourse = moduleRepository.countByCourseId(moduleCourse.getCourse().getId());
        double percentage = (moduleCourse.getModuleOrder() * 100.0) / amountOfModulesInCourse;
        return (int) Math.round(percentage);
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
                return new ResponseEntity<>(objectMapping.writeValueAsString(ExceptionRs.builder()
                        .text("Not all modules are completed")
                        .build()),
                        HttpStatus.BAD_REQUEST);
            }

            Optional<UserToCourse> userToCourseOptional = userToCourseRepository.findByUserAndCourse(userOptional.get(), courseOptional.get());
            if (userToCourseOptional.isPresent()){
                if (userToCourseOptional.get().getPercentageOfCompletion() == 100){
                    return new ResponseEntity<>(HttpStatus.OK);
                } else{
                    UserToCourse userToCourse = userToCourseOptional.get();
                    userToCourse.setPercentageOfCompletion(100);
                    return new ResponseEntity<>(HttpStatus.OK);
                }
            } else{
                log.info(String.format("Пользователь не брал данный курс, rqUid = %s", rqUid));
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (RuntimeException | JsonProcessingException e) {
            log.error(String.format("Внутрення ошибка сервиса %s, rqUid = %s", e.getMessage(), rqUid));
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    private UserToCourse buildUserToCourse(User user, Course course, Integer percentageOfCompletion){
        return UserToCourse.builder()
                .course(course)
                .percentageOfCompletion(percentageOfCompletion)
                .user(user)
                .build();
    }
}
