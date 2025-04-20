package com.example.demo.service.user;

import com.example.demo.database.entity.User;
import com.example.demo.database.repository.UserRepository;
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
public class GetUserInfoService {

    private final UserRepository userRepository;

    private final ObjectMapper objectMapping;

    @Transactional
    public ResponseEntity<String> getUser(Long id, String rqUid){
        try {
            log.info("Принят запрос для получения информации о пользователе с id = " + id + ", rqUid = " + rqUid);

            Optional<User> userOptional = userRepository.findUserById(id);
            User user = null;
            if (userOptional.isPresent()){
                user = userOptional.get();
                log.info("Пользователь был найден, rqUid = " + rqUid);
            } else{
                log.info("Пользователь отсутствует или информация не найдена, rqUid = " + rqUid);
            }
            return new ResponseEntity<>(objectMapping.writeValueAsString(user), HttpStatus.OK);
        } catch (JsonProcessingException | RuntimeException e) {
            log.error("Внутрення ошибка сервиса " + e.getMessage() + ", rqUid = " + rqUid);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
