package com.example.demo.dto.user;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserGetRs{
    Long id;
}
