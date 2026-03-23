package org.example.model.dto;

import org.example.model.entity.User;

public interface RequestDTO {
    boolean isValid();
    User toEntity();
}
