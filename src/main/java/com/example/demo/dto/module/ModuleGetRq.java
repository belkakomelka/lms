package com.example.demo.dto.module;

import com.example.demo.dto.PaginationRq;
import com.example.demo.dto.course.CourseGetFiltersRq;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ModuleGetRq {
    Long courseId;
}
