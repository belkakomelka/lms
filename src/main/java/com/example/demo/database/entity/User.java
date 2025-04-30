package com.example.demo.database.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_users")
    @SequenceGenerator(
            name = "sequence_users",
            sequenceName = "sequence_users",
            allocationSize = 1
    )
    Long id;

    @Column(name = "user_token", nullable = false)
    String userToken;

    @Builder.Default
    @OneToMany(mappedBy = "user")
    Set<UserToModule> userRelationToModule = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "user")
    Set<UserToAchievement> userRelationToAchievement = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "user")
    Set<UserToCourse> userRelationToCourse = new HashSet<>();
}
