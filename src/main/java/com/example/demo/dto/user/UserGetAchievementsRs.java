package com.example.demo.dto.user;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.Set;


@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserGetAchievementsRs{
    Long userId;

    @NotNull
    String userToken;

    Set<Achievements> achievements;

    @Data
    @Builder
    public static class Achievements{
        Long id;
        String name;
    }
}
