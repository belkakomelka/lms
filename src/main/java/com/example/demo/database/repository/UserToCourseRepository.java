package com.example.demo.database.repository;

import com.example.demo.database.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserToCourseRepository extends JpaRepository<UserToCourse, Long> {
    Optional<UserToCourse> findByUserAndCourse(User user, Course course);

}
