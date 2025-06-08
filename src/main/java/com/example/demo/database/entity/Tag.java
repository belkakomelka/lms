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
@Table(name = "tags")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_tags")
    @SequenceGenerator(
            name = "sequence_tags",
            sequenceName = "sequence_tags",
            allocationSize = 1
    )
    Long id;

    @Column(name = "name", nullable = false)
    String name;

    @Builder.Default
    @ManyToMany(mappedBy = "tags")
    private Set<Course> courses = new HashSet<>();
}
