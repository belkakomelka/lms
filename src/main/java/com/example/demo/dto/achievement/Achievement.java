package com.example.demo.dto.achievement;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Achievement {
    Long id;
    String name;
    String linkToPhoto; // todo уточнить у влада отдавать ли ему тут мультипарт сразу с фото? или он сам сходит в s3
}
