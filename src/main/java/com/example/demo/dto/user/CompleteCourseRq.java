package com.example.demo.dto.user;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompleteCourseRq {
    @NotNull
    String userId;

    @NotNull
    Long courseId;
}
