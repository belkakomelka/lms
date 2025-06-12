package com.example.demo.service.tag;

import com.example.demo.database.entity.Course;
import com.example.demo.database.entity.Tag;
import com.example.demo.database.repository.TagRepository;
import com.example.demo.dto.UploadRs;
import com.example.demo.dto.course.CourseGetRs;
import com.example.demo.dto.tag.TagGetRs;
import com.example.demo.dto.tag.TagUploadRq;
import com.example.demo.exception.DuplicateException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class GetTagService {

    private final TagRepository tagRepository;

    private final ObjectMapper objectMapper;

    public ResponseEntity<String> getTag(String rqUid) {
        try {
            log.info("Принят запрос для получения всех доступных тегов, rqUid = {}", rqUid);
            List<Tag> allTag = tagRepository.findAll();
            String tagRs = objectMapper.writeValueAsString(buildRs(allTag));
            log.info("Отправлен ответ для получения всех доступных тегов, тело ответа: {}, rqUid = {}", tagRs, rqUid);
            return new ResponseEntity<>(tagRs, HttpStatus.OK);
        }catch (Exception e) {
            log.error(String.format("Теги не могут быть получены, rqUid = %s", rqUid));
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public TagGetRs buildRs(List<Tag> tags) {
        List<com.example.demo.dto.tag.Tag> tag = tags.stream()
                .map(t -> com.example.demo.dto.tag.Tag.builder()
                        .id(t.getId())
                        .name(t.getName())
                        .build()
                ).toList();

        return TagGetRs.builder()
                .tags(tag)
                .build();
    }

}
