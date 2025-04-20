package com.example.demo.database.entity;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Type;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "course")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_course")
    @SequenceGenerator(
            name = "sequence_course",
            sequenceName = "sequence_course",
            allocationSize = 1
    )
    Long id;

    @Column(name = "name", nullable = false)
    String name;

    @Column(name = "description")
    String description;

    @Builder.Default
    @Type(value = JsonType.class)
    @Column(name = "tags")
    Set<String> tags = new HashSet<>();

    @OneToOne
    @Column(name = "achievement_id")
    Achievement achievement;

    @Builder.Default
    @OneToMany(mappedBy = "course",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private List<Module> modules = new ArrayList<>();
}
