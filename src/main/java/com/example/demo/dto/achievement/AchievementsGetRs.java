package com.example.demo.dto.achievement;

import com.example.demo.dto.tag.Tag;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AchievementsGetRs {
    List<Achievement> achievements;
}
