package com.example.demo.service.user;

import com.example.demo.database.entity.Achievement;
import com.example.demo.database.entity.ModuleCourse;
import com.example.demo.database.entity.User;
import com.example.demo.database.entity.UserToModule;
import com.example.demo.database.repository.ModuleRepository;
import com.example.demo.database.repository.UserRepository;
import com.example.demo.database.repository.UserToModuleRepository;
import com.example.demo.dto.user.CompleteModuleRq;
import com.example.demo.dto.user.UserGetAchievementsRs;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CompletionService {

    private final UserToModuleRepository userToModuleRepository;

    private final UserRepository userRepository;
    private final ModuleRepository moduleRepository;

    private final ObjectMapper objectMapping;

    @Transactional
    public ResponseEntity<String> completeModule(CompleteModuleRq completeModuleRq, String rqUid){
        try {
            log.info(String.format("Принят запрос для отметки пользователя об окончании курса с userId = %s, rqUid = %s", completeModuleRq.getUserId(), rqUid));

            Optional<User> userOptional = userRepository.findUserByUserToken(completeModuleRq.getUserId());
            Optional<ModuleCourse> moduleCourseOptional = moduleRepository.findById(completeModuleRq.getModule());

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

    private UserToModule buildUserToModuleEntity(User user, ModuleCourse moduleCourse){
        return UserToModule.builder()
                .isComplete(true)
                .module(moduleCourse)
                .user(user)
                .build();
    }
}
