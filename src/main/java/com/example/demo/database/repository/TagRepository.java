package com.example.demo.database.repository;

import com.example.demo.database.entity.Course;
import com.example.demo.database.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByName(String name);
}
