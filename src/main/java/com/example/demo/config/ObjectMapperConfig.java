package com.example.demo.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;

public class ObjectMapperConfig {
    @Bean
    ObjectMapper mapping(){
        return new ObjectMapper();
    }
}
