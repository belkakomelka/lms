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
import org.springframework.http.*;
import org.springframework.stereotype.Service;
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
            String rs = objectMapping.writeValueAsString(buildRs(modules));
            log.info(String.format("Отправлен ответ для получения модулей внутри курса %s, rqUid = %s", objectMapping.writeValueAsString(rs), rqUid));
            return new ResponseEntity<>(rs, HttpStatus.OK);
        } catch (JsonProcessingException e){
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<String> getModule(ModuleGetRq moduleGetRq, String rqUid) {
        try{
            log.info(String.format("Принят запрос для получения модуля %s, rqUid = %s", objectMapping.writeValueAsString(moduleGetRq), rqUid));
            Optional<ModuleCourse> moduleOpt = moduleRepository.findById(moduleGetRq.getModuleId());
            if (moduleOpt.isEmpty()) {
                log.info(String.format("Данного модуля нет в базе, id = %s, rqUid = %s", moduleGetRq.getModuleId(), rqUid));
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            String rs = objectMapping.writeValueAsString(buildModuleWithContent(moduleOpt.get()));
            log.info(String.format("Отправлен запрос для получения модуля %s, rqUid = %s", rs, rqUid));
            return new ResponseEntity<>(rs, HttpStatus.OK);
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
                .build();
    }

    private Module buildModuleWithContent(ModuleCourse module){
        return Module.builder()
                .id(module.getId())
                .courseId(module.getCourse().getId())
                .name(module.getName())
                .description(module.getDescription())
                .moduleOrder(module.getModuleOrder())
                .content(fileGetService.getContent(module.getLinkToVideo()))
                .achievement(module.getAchievement() != null ?
                        Achievement.builder()
                                .id(module.getAchievement().getId())
                                .name(module.getAchievement().getName())
                                .image(module.getAchievement().getLinkToPhoto()).build() : null)
                .build();
    }
}
