package com.example.user.model.dto;

import com.example.user.model.entity.User;

public interface RequestDTO {
    boolean isValid();
    User toEntity();
}
