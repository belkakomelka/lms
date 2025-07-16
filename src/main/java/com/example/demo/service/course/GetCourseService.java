package com.example.demo.service.course;

import com.example.demo.database.entity.Course;
import com.example.demo.database.entity.Tag;
import com.example.demo.database.entity.User;
import com.example.demo.database.entity.UserToCourse;
import com.example.demo.database.repository.CourseRepository;
import com.example.demo.database.repository.UserRepository;
import com.example.demo.dto.course.CourseGetFiltersRq;
import com.example.demo.dto.course.CourseGetRq;
import com.example.demo.dto.course.CourseGetRs;
import com.example.demo.service.minio.FileGetService;
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
public class GetCourseService {

    private final CourseRepository courseRepository;

    private final UserRepository userRepository;

    private final ObjectMapper objectMapping;

    private final FileGetService fileGetService;

    @Transactional
    public ResponseEntity<String> getCourse(CourseGetRq courseGetRq, String rqUid) {
        try{
            log.info(String.format("Принят запрос для получения курсов, rqUid = %s", objectMapping.writeValueAsString(courseGetRq), rqUid));
            CourseGetFiltersRq filters = courseGetRq.getCourseGetFiltersRq();
            String userToken = filters.getUserId();
            Optional<User> userOptional = userRepository.findUserByUserToken(userToken); // todo нужно фильтровать по id user тут нужно немного переделать
            List<Course> courses = courseRepository.findCoursesByFilters(userToken, filters.getTags());
            if (filters.getUserId() != null){
                if (courses == null || courses.isEmpty()) { // todo refactor
                    log.info(String.format("У пользователя с userId = %s нет активных курсов, rqUid = %s", filters.getUserId(), rqUid));
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                } else{ // todo добавить пагинацию
                    return new ResponseEntity<>(objectMapping.writeValueAsString(buildRs(courses, courseGetRq.getCourseGetFiltersRq().getUserId())), HttpStatus.OK);
                }
            }
            return new ResponseEntity<>(objectMapping.writeValueAsString(buildRs(courses)), HttpStatus.OK);
        } catch (JsonProcessingException e){
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public CourseGetRs buildRs(List<Course> courses) {
        List<com.example.demo.dto.course.Course> courseDtos = courses.stream()
                .map(c -> com.example.demo.dto.course.Course.builder()
                        .id(c.getId())
                        .name(c.getName())
                        .tags(mapTagNames(c.getTags()))
                        .description(c.getDescription())
                        .image(fileGetService.getContent(c.getLinkToPhoto()))
                        .build())
                .collect(Collectors.toList());

        return CourseGetRs.builder()
                .courses(courseDtos)
                .build();
    }

    public CourseGetRs buildRs(List<Course> courses, String userId) {
        List<com.example.demo.dto.course.Course> courseDtos = courses.stream()
                .map(c -> com.example.demo.dto.course.Course.builder()
                            .name(c.getName())
                            .tags(mapTagNames(c.getTags()))
                            .description(c.getDescription())
                            .completion_percentage(getCompletionPercentage(c, userId))
                            .build()
                ).collect(Collectors.toList());

        return CourseGetRs.builder()
                .courses(courseDtos)
                .build();
    }

    private Set<String> mapTagNames(Set<Tag> tags) {
        return tags.stream()
                .map(Tag::getName)
                .collect(Collectors.toSet());
    }

    private Integer getCompletionPercentage(Course course, String userId) {
        return course.getCourseRelationToUser().stream()
                .filter(relation -> userId.equals(relation.getUser().getUserToken()))
                .findFirst()
                .map(UserToCourse::getPercentageOfCompletion)
                .orElse(0);
    }
}
