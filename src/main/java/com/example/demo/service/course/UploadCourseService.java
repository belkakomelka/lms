package com.example.demo.service.course;

import com.example.demo.database.entity.Course;
import com.example.demo.database.entity.Tag;
import com.example.demo.database.repository.CourseRepository;
import com.example.demo.database.repository.TagRepository;
import com.example.demo.dto.course.CourseUploadRq;
import com.example.demo.dto.course.CourseUploadRs;
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
public class UploadCourseService {

    private final CourseRepository courseRepository;

    private final TagRepository tagRepository;

    private final ObjectMapper objectMapping;

    private final FileUploadService fileUploadService;

    public ResponseEntity<String> upload(CourseUploadRq courseUploadRq, MultipartFile photo, String rqUid) {
        try {
            log.info(String.format("Принят запрос для сохранения карточки курса, тело запроса: %s , rqUid = %s", objectMapping.writeValueAsString(courseUploadRq), rqUid));
            Optional<Course> courseOptional = courseRepository.findByName(courseUploadRq.getName());
            Course course;
            if (courseOptional.isPresent()){
                log.info(String.format("Данный курс уже есть на платформе, rqUid = %s", rqUid)); // todo апдейт отдельной ручкой
                course = courseOptional.get();
            } else{
                log.info(String.format("Данный курс новый, rqUid = %s", rqUid));
                String linkToPhoto = fileUploadService.uploadPhoto(photo);
                course = buildCourse(courseUploadRq, linkToPhoto);
                courseRepository.save(course);
            }
            return new ResponseEntity<>(objectMapping.writeValueAsString(buildRs(course)), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private Course buildCourse(CourseUploadRq courseUploadRq, String linkToPhoto){
        return Course.builder()
                .name(courseUploadRq.getName())
                .tags(tagConverter(courseUploadRq.getTags()))
                .linkToPhoto(linkToPhoto)
                .build();
    }


    private Set<Tag> tagConverter(Set<String> tagNames){
        Set<Tag> tags = new HashSet<>();

        if (tagNames != null) {
            for (String tagName : tagNames) {
                Tag tag = tagRepository.findByName(tagName)
                        .orElseGet(() -> {
                            Tag newTag = new Tag();
                            newTag.setName(tagName);
                            return tagRepository.save(newTag);
                        });
                tags.add(tag);
            }
        }
        return tags;
    }

    private CourseUploadRs buildRs(Course course){
        return CourseUploadRs.builder()
                .id(course.getId())
                .build();
    }
}
