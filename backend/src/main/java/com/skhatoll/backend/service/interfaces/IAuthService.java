package com.skhatoll.backend.service.interfaces;

import com.skhatoll.backend.dto.AuthResponse;
import com.skhatoll.backend.dto.LoginRequest;
import com.skhatoll.backend.dto.RegistroRequest;

public interface IAuthService {
    AuthResponse registro(RegistroRequest request);

    AuthResponse login(LoginRequest request);
}
