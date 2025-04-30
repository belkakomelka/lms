package com.example.demo.database.entity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Entity
@Table(name = "users_to_course")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserToCourse {
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "sequence_users_to_course")
    @SequenceGenerator(name = "sequence_users_to_course", sequenceName = "sequence_users_to_course", allocationSize = 1)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id")
    User user;

    @Column(name = "completion_percentage")
    Integer percentageOfCompletion;
}
