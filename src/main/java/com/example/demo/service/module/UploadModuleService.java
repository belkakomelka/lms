package com.example.demo.service.module;

import com.example.demo.database.entity.ModuleCourse;
import com.example.demo.database.repository.ModuleRepository;
import com.example.demo.dto.UploadRs;
import com.example.demo.dto.module.ModuleUploadRq;
import com.example.demo.service.minio.FileUploadService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UploadModuleService {

    private final ModuleRepository moduleRepository;

    private final ObjectMapper objectMapping;

    private final FileUploadService fileUploadService;

    public ResponseEntity<String> upload(ModuleUploadRq moduleUploadRq, MultipartFile video, String rqUid) {
        try {
            log.info(String.format("Принят запрос для сохранения модуля, тело запроса: %s , rqUid = %s", objectMapping.writeValueAsString(moduleUploadRq), rqUid));
            Optional<ModuleCourse> moduleCourseOptional = moduleRepository.findByName(moduleUploadRq.getName());
            ModuleCourse moduleCourse;
            if (moduleCourseOptional.isPresent()){
                log.info(String.format("Данный модуль уже есть на платформе, rqUid = %s", rqUid));
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            } else{
                log.info(String.format("Данный модуль новый, rqUid = %s", rqUid));
                String linkToVideo = fileUploadService.uploadVideo(video);
                log.info(String.format("Происходит создание модуля, rqUid = %s", rqUid));
                moduleCourse = buildModule(moduleUploadRq, linkToVideo);
                moduleRepository.save(moduleCourse);
                log.info(String.format("Модуль сохранен, rqUid = %s", rqUid));
            }
            String uploadRs = objectMapping.writeValueAsString(buildRs(moduleCourse));
            log.info(String.format("Отправлен ответ на запрос для создания модуля, тело ответа: %s, rqUid = %s", uploadRs, rqUid));
            return new ResponseEntity<>(uploadRs, HttpStatus.OK);
        } catch (Exception e) {
            log.error(String.format("Модуль не может быть создан, rqUid = %s", rqUid));
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ModuleCourse buildModule(ModuleUploadRq moduleUploadRq, String linkToVideo){
        return ModuleCourse.builder()
                .name(moduleUploadRq.getName())
                .moduleOrder(moduleUploadRq.getModuleOrder())
                .linkToVideo(linkToVideo)
                .description(moduleUploadRq.getDescription())
                .build();
    }

    private UploadRs buildRs(ModuleCourse moduleCourse){
        return UploadRs.builder()
                .id(moduleCourse.getId())
                .build();
    }
}
