package com.example.demo.database.repository;

import com.example.demo.database.entity.Course;
import org.hibernate.query.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CourseRepository extends JpaRepository<Course, Long> {
    @Query("""
    SELECT DISTINCT c FROM Course c
    LEFT JOIN FETCH c.tags t
    LEFT JOIN c.courseRelationToUser crtu
    LEFT JOIN crtu.user u
    WHERE (:userId IS NULL OR u.userToken = :userId)
    AND (:tags IS NULL OR t.name IN :tags)
    """)
    List<Course> findCoursesByFilters(
            @Param("userId") String userId,
            @Param("tags") Set<String> tags);

    Optional<Course> findByName(String name);
}
