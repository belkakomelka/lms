package com.example.demo.dto;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public abstract class BaseGetRs {
    private Long id;
}
