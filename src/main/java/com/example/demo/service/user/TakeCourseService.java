package com.example.demo.service.user;

import com.example.demo.database.entity.*;
import com.example.demo.database.repository.*;
import com.example.demo.dto.user.CompleteCourseRq;
import com.example.demo.dto.user.CompleteModuleRq;
import com.example.demo.dto.user.TakeCourseRq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class TakeCourseService {

    private final UserToModuleRepository userToModuleRepository;
    private final UserRepository userRepository;
    private final ModuleRepository moduleRepository;
    private final CourseRepository courseRepository;
    private final UserToCourseRepository userToCourseRepository;

    public ResponseEntity<String> takeCourse(TakeCourseRq takeCourseRq, String rqUid){
        try {
            log.info(String.format("Принят запрос для userId для выбора курса = %s, rqUid = %s", takeCourseRq.getUserId(), rqUid));

            Optional<User> userOptional = userRepository.findUserByUserToken(takeCourseRq.getUserId());
            Optional<Course> courseOptional = courseRepository.findById(takeCourseRq.getCourseId());

            if (userOptional.isEmpty() || courseOptional.isEmpty()) {
                log.info(String.format("Пользователь отсутствует или информация о курсе не найдена, rqUid = %s", rqUid));
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            Optional<UserToCourse> userToCourseOptional = userToCourseRepository.findByUserAndCourse(userOptional.get(), courseOptional.get());
            if (userToCourseOptional.isPresent()){
                log.info(String.format("Данный курс уже есть у пользователя, rqUid = %s", rqUid));
                return new ResponseEntity<>(HttpStatus.OK);
            }
            userToCourseRepository.save(buildUserToCourse(userOptional.get(), courseOptional.get()));

            List<ModuleCourse> moduleList = moduleRepository.findByCourseIdOrderByModuleOrderAsc(takeCourseRq.getCourseId());

            if (!moduleList.isEmpty()) {
                List<UserToModule> userToModules = moduleList.stream()
                        .map(module -> buildUserToModule(userOptional.get(), module))
                        .collect(Collectors.toList());
                userToModuleRepository.saveAll(userToModules);
            } else {
                log.info("Курс не содержит модулей, courseId = {}, rqUid = {}", userOptional.get().getId(), rqUid);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (RuntimeException e) {
            log.error(String.format("Внутрення ошибка сервиса %s, rqUid = %s", e.getMessage(), rqUid));
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private UserToCourse buildUserToCourse(User user, Course course){
        return UserToCourse.builder()
                .user(user)
                .course(course)
                .percentageOfCompletion(0)
                .build();
    }

    private UserToModule buildUserToModule(User user, ModuleCourse module){
        return UserToModule.builder()
                .user(user)
                .module(module)
                .isComplete(false)
                .build();
    }
}
