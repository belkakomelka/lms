package com.example.demo.service.user;

import com.example.demo.database.entity.User;
import com.example.demo.database.repository.UserRepository;
import com.example.demo.dto.user.UserGetRq;
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
            log.info(String.format("Принят запрос для получения информации о пользователе с id = %d, rqUid = %s", id, rqUid));

            Optional<User> userOptional = userRepository.findUserById(id);
            User user = null;
            if (userOptional.isPresent()){
                user = userOptional.get();
                log.info(String.format("Пользователь был найден, rqUid = %s", rqUid));
            } else{
                log.info(String.format("Пользователь отсутствует или информация не найдена, rqUid = %s", rqUid));
            }
            return new ResponseEntity<>(objectMapping.writeValueAsString(user), HttpStatus.OK);
        } catch (JsonProcessingException | RuntimeException e) {
            log.error(String.format("Внутрення ошибка сервиса %s, rqUid = %s", e.getMessage(), rqUid));
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
