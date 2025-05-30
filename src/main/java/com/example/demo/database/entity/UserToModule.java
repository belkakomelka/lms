package com.example.demo.database.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Entity
@Table(name = "users_to_module")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserToModule {
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "sequence_users_to_module")
    @SequenceGenerator(name = "sequence_users_to_module", sequenceName = "sequence_users_to_module", allocationSize = 1)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "module_id")
    ModuleCourse module;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id")
    User user;

    @Column(name = "is_complete")
    Boolean isComplete;
}
