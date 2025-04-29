package com.example.demo.dto.course;

import com.example.demo.dto.PaginationRq;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CourseGetRq {
    @NotNull
    CourseGetFiltersRq courseGetFiltersRq;

    @NotNull
    PaginationRq paginationRq;
}
