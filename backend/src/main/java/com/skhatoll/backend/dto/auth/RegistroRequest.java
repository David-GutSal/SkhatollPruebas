package com.skhatoll.backend.dto.auth;

import lombok.Data;

@Data
public class RegistroRequest {
    private String nombre;
    private String password;
}