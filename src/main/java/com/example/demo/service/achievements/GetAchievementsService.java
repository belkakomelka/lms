package com.example.demo.service.achievements;

import com.example.demo.database.entity.Achievement;
import com.example.demo.database.entity.Tag;
import com.example.demo.database.repository.AchievementsRepository;
import com.example.demo.database.repository.TagRepository;
import com.example.demo.dto.achievement.AchievementsGetRs;
import com.example.demo.dto.tag.TagGetRs;
import com.example.demo.exception.DuplicateException;
import com.example.demo.service.minio.FileGetService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class GetAchievementsService {

    private final AchievementsRepository achievementsRepository;

    private final ObjectMapper objectMapper;

    private final FileGetService fileGetService;

    public ResponseEntity<String> getAchievements(String rqUid){
        try {
            log.info("Принят запрос для получения всех доступных достижений, rqUid = {}", rqUid);
            List<Achievement> allAchievements = achievementsRepository.findAll();
            String achievementRs = objectMapper.writeValueAsString(buildRs(allAchievements));
            log.info("Отправлен ответ для получения всех доступных наград, тело ответа: {}, rqUid = {}", achievementRs, rqUid);
            return new ResponseEntity<>(achievementRs, HttpStatus.OK);
        } catch (Exception e) {
            log.error(String.format("Награды не могут быть получены, rqUid = %s", rqUid));
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public AchievementsGetRs buildRs(List<Achievement> allAchievements) {
        List<com.example.demo.dto.achievement.Achievement> achievements = allAchievements.stream()
                .map(a -> com.example.demo.dto.achievement.Achievement.builder()
                        .id(a.getId())
                        .name(a.getName())
                        .image(fileGetService.getContent(a.getLinkToPhoto()))
                        .build()
                ).toList();

        return AchievementsGetRs.builder()
                .achievements(achievements)
                .build();
    }

}
