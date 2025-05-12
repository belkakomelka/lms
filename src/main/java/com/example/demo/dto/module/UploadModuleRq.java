package com.example.demo.dto.module;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UploadModuleRq {
    String name;

    String description;

    Set<String> tags;
}
