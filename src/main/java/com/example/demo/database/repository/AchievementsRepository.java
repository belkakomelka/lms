package com.example.demo.database.repository;

import com.example.demo.database.entity.Achievement;
import com.example.demo.database.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface AchievementsRepository extends JpaRepository<Achievement, Long> {
    Optional<Achievement> findByName(String name);
}
