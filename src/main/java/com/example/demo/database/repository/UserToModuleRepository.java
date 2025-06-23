package com.example.demo.database.repository;

import com.example.demo.database.entity.ModuleCourse;
import com.example.demo.database.entity.User;
import com.example.demo.database.entity.UserToModule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserToModuleRepository extends JpaRepository<UserToModule, Long> {

    List<UserToModule> findByUserAndModuleCourseIn(User user, List<ModuleCourse> modules);
    Optional<UserToModule> findByUserAndModule(User user, ModuleCourse module);

}
