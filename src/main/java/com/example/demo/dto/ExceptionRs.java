package com.example.demo.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExceptionRs {
    String text;
    String description;
}
