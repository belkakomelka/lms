package com.example.demo.service.course;

import com.example.demo.database.entity.Course;
import com.example.demo.database.entity.Tag;
import com.example.demo.database.repository.CourseRepository;
import com.example.demo.dto.course.CourseGetFiltersRq;
import com.example.demo.dto.course.CourseGetRq;
import com.example.demo.dto.course.CourseGetRs;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class GetCourseService {

    private final CourseRepository courseRepository;

    private final ObjectMapper objectMapping;

    @Transactional
    public ResponseEntity<String> getCourse(CourseGetRq courseGetRq) {
        try{
            CourseGetFiltersRq filters = courseGetRq.getCourseGetFiltersRq();
            List<Course> courses = courseRepository.findCoursesByFilters(filters.getUserId(), filters.getTags());
            return new ResponseEntity<>(objectMapping.writeValueAsString(buildRs(courses)), HttpStatus.OK);
        } catch (JsonProcessingException e){
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public CourseGetRs buildRs(List<Course> courses) {
        List<com.example.demo.dto.course.Course> courseDtos = courses.stream()
                .map(c -> com.example.demo.dto.course.Course.builder()
                        .name(c.getName())
                        .tags(c.getTags().stream()
                                .map(Tag::getName)
                                .collect(Collectors.toSet()))
                        .description(c.getDescription())
                        .build())
                .collect(Collectors.toList());

        return CourseGetRs.builder()
                .courses(courseDtos)
                .build();
    }
}
