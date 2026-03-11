package com.skhatoll.backend.service.interfaces.auth;

import com.skhatoll.backend.dto.auth.AuthResponse;
import com.skhatoll.backend.dto.auth.LoginRequest;
import com.skhatoll.backend.dto.auth.RegistroRequest;

public interface IAuthService {
    AuthResponse registro(RegistroRequest request);

    AuthResponse login(LoginRequest request);
}
