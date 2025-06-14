package com.example.demo.database.repository;

import com.example.demo.database.entity.User;
import com.example.demo.database.entity.UserToModule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserToModuleRepository extends JpaRepository<UserToModule, Long> {

}
