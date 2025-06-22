package com.example.demo.database.repository;

import com.example.demo.database.entity.ModuleCourse;
import com.example.demo.database.entity.User;
import com.example.demo.database.entity.UserToCourse;
import com.example.demo.database.entity.UserToModule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserToCourseRepository extends JpaRepository<UserToCourse, Long> {

}
