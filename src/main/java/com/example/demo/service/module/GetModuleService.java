package com.example.demo.service.module;

import com.example.demo.database.entity.*;
import com.example.demo.database.repository.ModuleRepository;
import com.example.demo.dto.achievement.Achievement;
import com.example.demo.dto.module.Module;
import com.example.demo.dto.module.ModuleGetRq;
import com.example.demo.dto.module.ModulesGetRq;
import com.example.demo.dto.module.ModuleGetRs;
import com.example.demo.service.minio.FileGetService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class GetModuleService {

    private final FileGetService fileGetService;

    private final ModuleRepository moduleRepository;

    private final ObjectMapper objectMapping;

    public ResponseEntity<String> getModules(ModulesGetRq modulesGetRq, String rqUid) {
        try{
            log.info(String.format("Принят запрос для получения модулей внутри курса %s, rqUid = %s", objectMapping.writeValueAsString(modulesGetRq), rqUid));
            List<ModuleCourse> modules = moduleRepository.findByCourseIdOrderByModuleOrderAsc(modulesGetRq.getCourseId());

            if (modules == null || modules.isEmpty()) {
                log.info(String.format("У данного курса %d нет ни одного модуля, rqUid = %s", modulesGetRq.getCourseId(), rqUid));
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(objectMapping.writeValueAsString(buildRs(modules)), HttpStatus.OK);
        } catch (JsonProcessingException e){
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<MultiValueMap<String, Object>> getModule(ModuleGetRq moduleGetRq, String rqUid) {
        try{
            log.info(String.format("Принят запрос для получения модуля %s, rqUid = %s", objectMapping.writeValueAsString(moduleGetRq), rqUid));
            Optional<ModuleCourse> module = moduleRepository.findById(moduleGetRq.getModuleId());
            if (module.isEmpty()) {
                log.info(String.format("Данного модуля нет в базе, id = %s, rqUid = %s", moduleGetRq.getModuleId(), rqUid));
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

//            InputStream videoStream = fileGetService.getContent(module.get().getLinkToVideo());
//            byte[] videoBytes = videoStream.readAllBytes();
//            String fileName = Paths.get(module.get().getLinkToVideo()).getFileName().toString();
//            String contentType = Files.probeContentType(Paths.get(fileName));
//            if (contentType == null) contentType = "video/mp4";
//
//            // Видео как ресурс
//            ByteArrayResource videoResource = new ByteArrayResource(videoBytes) {
//                @Override
//                public String getFilename() {
//                    return fileName;
//                }
//            };
//
//            HttpHeaders videoPartHeaders = new HttpHeaders();
//            videoPartHeaders.setContentDisposition(
//                    ContentDisposition.builder("inline").name("video").filename(fileName).build()
//            );
//            videoPartHeaders.setContentType(MediaType.parseMediaType(contentType));
//            HttpEntity<ByteArrayResource> videoPart = new HttpEntity<>(videoResource, videoPartHeaders);
//
            HttpHeaders jsonPartHeaders = new HttpHeaders();
            jsonPartHeaders.setContentDisposition(
                    ContentDisposition.builder("inline").name("moduleInfo").build()
            );
            jsonPartHeaders.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Object> moduleInfoPart = new HttpEntity<>(buildModule(module.get()), jsonPartHeaders);

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("moduleInfo", moduleInfoPart);
 //           body.add("video", videoPart);

            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.setContentType(MediaType.MULTIPART_MIXED);

            return new ResponseEntity<>(body, responseHeaders, HttpStatus.OK);
        } catch (JsonProcessingException e){
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private ModuleGetRs buildRs(List<ModuleCourse> modules) {
        List<com.example.demo.dto.module.Module> moduleDtos = modules.stream()
                .map(this::buildModule)
                .toList();

        return ModuleGetRs.builder()
                .modules(moduleDtos)
                .build();
    }

    private Module buildModule(ModuleCourse module){
        return Module.builder()
                .id(module.getId())
                .courseId(module.getCourse().getId())
                .name(module.getName())
                .description(module.getDescription())
                .moduleOrder(module.getModuleOrder())
                .achievement(module.getAchievement() != null ?
                        Achievement.builder()
                                .id(module.getAchievement().getId())
                                .name(module.getAchievement().getName())
                                .image(module.getAchievement().getLinkToPhoto()).build() : null) // todo переделать тут
                .build();
    }
}
