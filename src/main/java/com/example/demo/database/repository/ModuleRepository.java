package com.example.demo.database.repository;

import com.example.demo.database.entity.ModuleCourse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ModuleRepository extends JpaRepository<ModuleCourse, Long> {
    Optional<ModuleCourse> findByName(String name);

    Optional<ModuleCourse> findById(Long id);

    List<ModuleCourse> findByCourseIdOrderByModuleOrderAsc(Long courseId);

    Long countByCourseId(Long courseId);

}
