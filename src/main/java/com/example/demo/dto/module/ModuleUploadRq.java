package com.example.demo.dto.module;

import jakarta.validation.constraints.NotNull;
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

    @NotNull
    Long courseId;       // курс, которому принадлежит модуль

    @NotNull
    Integer moduleOrder; //  порядок модуля в курсе
}
