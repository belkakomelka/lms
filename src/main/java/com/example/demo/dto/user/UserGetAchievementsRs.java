package com.example.demo.dto.user;

import com.example.demo.dto.BaseGetRs;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class UserGetAchievementsRs extends BaseGetRs {
    @NotNull
    String userToken;

    Set<Achievements> achievements;

    @Data
    @Builder
    public static class Achievements{
        Long id;
        String name;
        Long score;
    }
}
