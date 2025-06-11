package com.example.demo.service.achievements;

import com.example.demo.database.entity.Achievement;
import com.example.demo.database.entity.Course;
import com.example.demo.database.entity.Tag;
import com.example.demo.database.repository.AchievementsRepository;
import com.example.demo.database.repository.CourseRepository;
import com.example.demo.database.repository.TagRepository;
import com.example.demo.dto.UploadRs;
import com.example.demo.dto.achievement.AchievementUploadRq;
import com.example.demo.dto.course.CourseUploadRq;
import com.example.demo.service.minio.FileUploadService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class UploadAchievementsService {

    private final AchievementsRepository achievementsRepository;

    private final ObjectMapper objectMapping;

    private final FileUploadService fileUploadService;

    public ResponseEntity<String> upload(AchievementUploadRq achievementUploadRq, MultipartFile photo, String rqUid) {
        try {
            log.info(String.format("Принят запрос для сохранения карточки достижений, тело запроса: %s , rqUid = %s", objectMapping.writeValueAsString(achievementUploadRq), rqUid));
            Optional<Achievement> achievementOptional = achievementsRepository.findByName(achievementUploadRq.getName());
            Achievement achievement;
            if (achievementOptional.isPresent()){
                log.info(String.format("Данная награда уже есть на платформе, rqUid = %s", rqUid));
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            } else{
                log.info(String.format("Данная награда новая, rqUid = %s", rqUid));
                String linkToPhoto = fileUploadService.uploadPhoto(photo);
                log.info(String.format("Происходит создание награды, rqUid = %s", rqUid));
                achievement = buildAchievement(achievementUploadRq, linkToPhoto);
                achievementsRepository.save(achievement);
                log.info(String.format("Награда сохранена, rqUid = %s", rqUid));
            }
            String uploadRs = objectMapping.writeValueAsString(buildRs(achievement));
            log.info(String.format("Отправлен ответ на запрос для создания награды, тело ответа: %s, rqUid = %s", uploadRs, rqUid));
            return new ResponseEntity<>(uploadRs, HttpStatus.OK);
        } catch (Exception e) {
            log.error(String.format("Награда не может быть создана, rqUid = %s", rqUid));
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private Achievement buildAchievement(AchievementUploadRq achievementUploadRq, String linkToPhoto){
        return Achievement.builder()
                .name(achievementUploadRq.getName())
                .linkToPhoto(linkToPhoto)
                .build();
    }

    private UploadRs buildRs(Achievement achievement){
        return UploadRs.builder()
                .id(achievement.getId())
                .build();
    }
}
