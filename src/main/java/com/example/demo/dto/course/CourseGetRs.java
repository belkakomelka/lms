package com.example.demo.dto.course;

import com.example.demo.dto.PaginationRq;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CourseGetRs {
    List<Course> courses;
    PaginationRq paginationRq;
}
