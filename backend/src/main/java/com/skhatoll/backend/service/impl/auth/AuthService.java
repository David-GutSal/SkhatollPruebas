package com.skhatoll.backend.service.impl.auth;

import com.skhatoll.backend.dto.auth.AuthResponse;
import com.skhatoll.backend.dto.auth.LoginRequest;
import com.skhatoll.backend.dto.auth.RegistroRequest;
import com.skhatoll.backend.security.JwtUtil;
import com.skhatoll.backend.entities.Usuario;
import com.skhatoll.backend.repository.UsuarioRepository;
import com.skhatoll.backend.service.interfaces.auth.IAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AuthResponse registro(RegistroRequest request) {

        if (usuarioRepository.existsByNombre(request.getNombre())) {
            throw new IllegalArgumentException("El nombre de usuario ya está en uso");
        }

        Usuario usuario = Usuario.builder()
                .nombre(request.getNombre())
                .codigoUuid(UUID.randomUUID().toString())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        usuarioRepository.save(usuario);

        String token = jwtUtil.generarToken(usuario.getNombre());
        return new AuthResponse(token, usuario.getNombre(), usuario.getCodigoUuid());
    }

    public AuthResponse login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getNombre(),
                        request.getPassword()));

        Usuario usuario = usuarioRepository.findByNombre(request.getNombre())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        String token = jwtUtil.generarToken(usuario.getNombre());
        return new AuthResponse(token, usuario.getNombre(), usuario.getCodigoUuid());
    }
}