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
            log.info(String.format("Принят запрос для сохранения нового участника %s, rqUid = %s", objectMapping.writeValueAsString(userRegistrationRq), rqUid));

            Optional<User> userOptional = userRepository.findUserByUserToken(userRegistrationRq.getUserId());
            User user;
            if (userOptional.isPresent()){
                user = userOptional.get();
                log.info(String.format("Данный пользователь уже зарегистрирован в системе %s, rqUid = %s", userRegistrationRq.getUserId(), rqUid));
            } else{
                log.info(String.format("Пользователь отсутствует, rqUid = %s", rqUid));
                user = buildUser(userRegistrationRq);
                userRepository.save(user);
            }
            return new ResponseEntity<>(objectMapping.writeValueAsString(buildRs(user)), HttpStatus.OK);
        } catch (JsonProcessingException | RuntimeException | NoSuchAlgorithmException e) {
            log.error(String.format("Внутрення ошибка сервиса  %s, rqUid = %s", e.getMessage(), rqUid));
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private User buildUser(UserRegistrationRq userRegistrationRq) throws NoSuchAlgorithmException {
        return  User.builder()
                .userToken(userRegistrationRq.getUserId())
                .build();
    }

    private UserGetRs buildRs(User user){
        return UserGetRs.builder()
                .id(user.getId())
                .build();
    }
}
