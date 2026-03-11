package com.skhatoll.backend.controller;

import com.skhatoll.backend.dto.auth.AuthResponse;
import com.skhatoll.backend.dto.auth.LoginRequest;
import com.skhatoll.backend.dto.auth.RegistroRequest;
import com.skhatoll.backend.service.interfaces.auth.IAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final IAuthService authService;

    // -------------------------------------------------------
    // POST /auth/registro
    // Body: { "nombre": "...", "password": "..." }
    // -------------------------------------------------------
    @PostMapping("/registro")
    public ResponseEntity<?> registro(@RequestBody RegistroRequest request) {
        try {
            AuthResponse response = authService.registro(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    // -------------------------------------------------------
    // POST /auth/login
    // Body: { "nombre": "...", "password": "..." }
    // -------------------------------------------------------
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            AuthResponse response = authService.login(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales incorrectas");
        }
    }
}