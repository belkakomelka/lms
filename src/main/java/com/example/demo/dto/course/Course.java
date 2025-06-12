package com.example.demo.dto.course;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Course {
    Long id;

    String name;

    String description;

    Set<String> tags;

    Integer completion_percentage;

    // todo ачивки? модули?
}
