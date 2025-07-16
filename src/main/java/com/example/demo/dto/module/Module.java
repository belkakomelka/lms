package com.example.demo.dto.module;

import com.example.demo.dto.achievement.Achievement;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Module {
    Long id;

    Long courseId;

    String name;

    String description;

    Achievement achievement; // ачивки для этого модуля, todo чтобы узнать есть ли она у пользователя, нужно идти в связь UserToModule

    Integer moduleOrder;

    String content;
}
