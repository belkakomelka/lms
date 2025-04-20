package com.example.demo.service.user;

import com.example.demo.database.entity.User;
import com.example.demo.database.repository.UserRepository;
import com.example.demo.dto.user.UserGetRs;
import com.example.demo.dto.user.UserRegistrationRq;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AddUserService {

    private final UserRepository userRepository;

    private final ObjectMapper objectMapping;

    @Transactional
    public ResponseEntity<String> addUser(UserRegistrationRq userRegistrationRq, String rqUid){
        try {
            log.info("Принят запрос для сохранения нового участника " + objectMapping.writeValueAsString(userRegistrationRq) + ", rqUid = " + rqUid);

            Optional<User> userOptional = userRepository.findUserByUserToken(userRegistrationRq.getUserToken());
            User user;
            if (userOptional.isPresent()){
                user = userOptional.get();
                log.info("Данный пользователь уже зарегистрирован в системе " + userRegistrationRq.getUserToken() + ", rqUid = " + rqUid);
            } else{
                log.info("Пользователь отсутствует, rqUid = " + rqUid);
                user = buildUser(userRegistrationRq);
                userRepository.save(user);
            }
            return new ResponseEntity<>(objectMapping.writeValueAsString(buildRs(user)), HttpStatus.OK);
        } catch (JsonProcessingException | RuntimeException | NoSuchAlgorithmException e) {
            log.error("Внутрення ошибка сервиса " + e.getMessage() + ", rqUid = " + rqUid);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private User buildUser(UserRegistrationRq userRegistrationRq) throws NoSuchAlgorithmException {
        return  User.builder()
                .userToken(userRegistrationRq.getUserToken())
                .build();
    }

    private UserGetRs buildRs(User user){
        return UserGetRs.builder()
                .id(user.getId())
                .build();
    }
}
