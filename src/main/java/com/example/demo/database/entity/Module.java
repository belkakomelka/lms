package com.example.demo.database.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "module")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Module {
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "sequence_module")
    @SequenceGenerator(name = "sequence_module", sequenceName = "sequence_module", allocationSize = 1)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @Column(name = "course_id")
    Course course;

    @Column(name = "name", nullable=false)
    String name;

    @Column(name = "description")
    String description;

    @Column(name = "link_to_video")
    String linkToVideo;

    @Builder.Default
    @OneToMany(mappedBy = "module")
    Set<UserToModule> moduleRelationToUser = new HashSet<>();

    @OneToOne
    @Column(name = "achievement_id")
    Achievement achievement;
}
