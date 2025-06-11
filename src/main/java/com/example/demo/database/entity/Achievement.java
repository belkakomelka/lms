package com.example.demo.database.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "achievement")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class Achievement {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_achievement")
    @SequenceGenerator(
            name = "sequence_achievement",
            sequenceName = "sequence_achievement",
            allocationSize = 1
    )
    Long id;

    @Column(name = "name", nullable = false)
    String name;

    @Column(name = "link_to_photo")
    String linkToPhoto;

    @Builder.Default
    @OneToMany(mappedBy = "achievement")
    Set<UserToAchievement> moduleRelationToAchievement = new HashSet<>();
}
