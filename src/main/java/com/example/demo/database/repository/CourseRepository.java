package com.example.demo.database.repository;

import com.example.demo.database.entity.Course;
import org.hibernate.query.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Set;

public interface CourseRepository extends JpaRepository<Course, Long> {
    @Query("""
    SELECT DISTINCT c FROM Course c
    LEFT JOIN c.tags t
    LEFT JOIN UserCourseProgress ucp ON ucp.course.id = c.id
    WHERE (:userId IS NULL OR ucp.user.id = :userId)
    AND (:tags IS NULL OR t.name IN :tags)
    GROUP BY c
    """)
    List<Course> findCoursesByFilters(
            @Param("userId") String userId,
            @Param("tags") Set<String> tags);
}
