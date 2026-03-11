package com.skhatoll.backend.dto.auth;

import lombok.Data;

@Data
public class LoginRequest {
    private String nombre;
    private String password;
}