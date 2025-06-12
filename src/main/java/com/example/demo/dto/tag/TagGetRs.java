package com.example.demo.dto.tag;

import com.example.demo.dto.PaginationRq;
import com.example.demo.dto.course.Course;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TagGetRs {
    List<Tag> tags;
}
