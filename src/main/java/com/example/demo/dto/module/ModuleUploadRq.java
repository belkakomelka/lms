package com.example.demo.dto.module;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ModuleUploadRq {
    String name;

    String description;

    Long courseId; // курс, которому принадлежит модуль

    Integer moduleOrder; //  порядок модуля в курсе
}
