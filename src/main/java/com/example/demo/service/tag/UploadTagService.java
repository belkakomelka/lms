package com.example.demo.service.tag;

import com.example.demo.database.entity.Tag;
import com.example.demo.database.repository.TagRepository;
import com.example.demo.dto.UploadRs;
import com.example.demo.dto.tag.TagUploadRq;
import com.example.demo.exception.DuplicateException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UploadTagService {

    private final TagRepository tagRepository;

    private final ObjectMapper objectMapper;

    public ResponseEntity<String> upload(TagUploadRq tagUploadRq, String rqUid) {
        try {
            log.info("Принят запрос для сохранения тега, тело запроса: {}, rqUid = {}",
                    objectMapper.writeValueAsString(tagUploadRq), rqUid);

            Optional<Tag> tagOptional = tagRepository.findByName(tagUploadRq.getName());

            if (tagOptional.isPresent()) {
                throw new DuplicateException(String.format("Данный тег %s уже загружен на платформу", tagOptional.get().getName()));
            }

            Tag tag = buildTag(tagUploadRq);
            tag = tagRepository.save(tag);

            String tagUploadRs = objectMapper.writeValueAsString(buildRs(tag));
            log.info("Отправлен ответ на запрос для создания тега, тело ответа: {}, rqUid = {}", tagUploadRs, rqUid);
            return new ResponseEntity<>(tagUploadRs, HttpStatus.OK);
        } catch (DuplicateException e){
            log.warn(String.format("Данный тег уже загружен на платформу, rqUid = %s", rqUid));
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }catch (Exception e) {
            log.error(String.format("Тег не может быть создана, rqUid = %s", rqUid));
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private UploadRs buildRs(Tag tag){
        return UploadRs.builder()
                .id(tag.getId())
                .build();
    }

    private Tag buildTag(TagUploadRq tagUploadRq){
        return Tag.builder()
                .name(tagUploadRq.getName())
                .build();
    }
}
