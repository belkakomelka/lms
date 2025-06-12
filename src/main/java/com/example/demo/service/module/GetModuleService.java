package com.example.demo.service.module;

import com.example.demo.database.entity.*;
import com.example.demo.database.repository.CourseRepository;
import com.example.demo.database.repository.ModuleRepository;
import com.example.demo.database.repository.UserRepository;
import com.example.demo.dto.achievement.Achievement;
import com.example.demo.dto.course.CourseGetFiltersRq;
import com.example.demo.dto.course.CourseGetRq;
import com.example.demo.dto.course.CourseGetRs;
import com.example.demo.dto.module.Module;
import com.example.demo.dto.module.ModuleGetRq;
import com.example.demo.dto.module.ModuleGetRs;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class GetModuleService {

    private final ModuleRepository moduleRepository;

    private final ObjectMapper objectMapping;

    @Transactional
    public ResponseEntity<String> getCourse(ModuleGetRq moduleGetRq, String rqUid) {
        try{
            log.info(String.format("Принят запрос для получения модулей внутри курса, rqUid = %s", objectMapping.writeValueAsString(moduleGetRq), rqUid));
            List<ModuleCourse> modules = moduleRepository.findByCourseIdOrderByModuleOrderAsc(moduleGetRq.getCourseId());

            if (modules == null || modules.isEmpty()) {
                log.info(String.format("У данного курса %d нет ни одного модуля, rqUid = %s", moduleGetRq.getCourseId(), rqUid));
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(objectMapping.writeValueAsString(buildRs(modules)), HttpStatus.OK);
        } catch (JsonProcessingException e){
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ModuleGetRs buildRs(List<ModuleCourse> modules) {
        List<com.example.demo.dto.module.Module> moduleDtos = modules.stream()
                .map(m -> Module.builder()
                        .id(m.getId())
                        .courseId(m.getCourse().getId())
                        .name(m.getName())
                        .description(m.getDescription())
                        .linkToVideo(m.getLinkToVideo())
                        .moduleOrder(m.getModuleOrder())
                        .achievement(m.getAchievement() != null ?
                                Achievement.builder()
                                .id(m.getAchievement().getId())
                                .name(m.getAchievement().getName())
                                .linkToPhoto(m.getAchievement().getLinkToPhoto()).build() : null)
                        .build())
                .toList();

        return ModuleGetRs.builder()
                .modules(moduleDtos)
                .build();
    }
}
