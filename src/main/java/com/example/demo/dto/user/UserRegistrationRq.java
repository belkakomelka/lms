package com.example.demo.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRegistrationRq {
    @NotBlank
    private String userToken;
}
