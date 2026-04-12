package com.example.model.dto;

import com.example.model.entity.User;

public interface RequestDTO {
    boolean isValid();
    User toEntity();
}
