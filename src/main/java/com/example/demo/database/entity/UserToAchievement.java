package com.example.demo.database.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Entity
@Table(name = "users_to_achievement")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserToAchievement {
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "sequence_users_to_achievement")
    @SequenceGenerator(name = "sequence_users_to_achievement", sequenceName = "sequence_users_to_achievement", allocationSize = 1)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "achievement_id")
    Achievement achievement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id")
    User user;
}
