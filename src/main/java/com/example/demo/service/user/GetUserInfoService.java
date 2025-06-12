package com.example.demo.service.user;

import com.example.demo.database.entity.Achievement;
import com.example.demo.database.entity.User;
import com.example.demo.database.repository.UserRepository;
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
public class GetUserInfoService {

    private final UserRepository userRepository;

    private final ObjectMapper objectMapping;

    @Transactional
    public ResponseEntity<String> getUserAchievements(String id, String rqUid){
        try {
            log.info(String.format("Принят запрос для получения информации о достижениях пользователе с userId = %s, rqUid = %s", id, rqUid));

            Optional<User> userOptional = userRepository.findUserByUserToken(id);
            User user;
            if (userOptional.isPresent()){
                user = userOptional.get();
                log.info(String.format("Пользователь был найден, rqUid = %s", rqUid));
                if (user.getUserRelationToAchievement() == null || user.getUserRelationToAchievement().isEmpty()){
                    log.info(String.format("У пользователя нет наград, rqUid = %s", rqUid));
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }
            } else{
                log.info(String.format("Пользователь отсутствует или информация не найдена, rqUid = %s", rqUid));
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(objectMapping.writeValueAsString(buildRs(user)), HttpStatus.OK);
        } catch (JsonProcessingException | RuntimeException e) {
            log.error(String.format("Внутрення ошибка сервиса %s, rqUid = %s", e.getMessage(), rqUid));
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private UserGetAchievementsRs buildRs(User user){
        return UserGetAchievementsRs.builder()
                .userId(user.getId())
                .userToken(user.getUserToken())
                .achievements(user
                        .getUserRelationToAchievement()
                        .stream()
                        .map(relation -> buildAchievement(relation.getAchievement()))
                        .collect(Collectors.toSet()))
                .build();

    }

    private UserGetAchievementsRs.Achievements buildAchievement(Achievement achievement){
        return UserGetAchievementsRs.Achievements.builder()
                .id(achievement.getId())
                .name(achievement.getName())
                .build();
    }
}
