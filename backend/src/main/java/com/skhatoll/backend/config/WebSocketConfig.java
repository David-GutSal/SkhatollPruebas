package com.skhatoll.backend.config;

import com.skhatoll.backend.security.JwtUtil;
import com.skhatoll.backend.security.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    // -------------------------------------------------------
    // Punto de entrada WebSocket al que se conectan los clientes
    // -------------------------------------------------------
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")  // En producción limitar al dominio del frontend
                .withSockJS();
    }

    // -------------------------------------------------------
    // Configuración del broker de mensajes
    // -------------------------------------------------------
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // Prefijo para mensajes que el cliente envía al servidor
        registry.setApplicationDestinationPrefixes("/");

        // Prefijos de canales gestionados por el broker interno de Spring
        // /topic → mensajes broadcast (todos en la sala)
        // /queue → mensajes privados (un jugador concreto)
        registry.enableSimpleBroker("/topic", "/queue");

        // Prefijo para mensajes privados dirigidos a un usuario concreto
        registry.setUserDestinationPrefix("/user");
    }

    // -------------------------------------------------------
    // Valida el token JWT en la conexión STOMP
    // -------------------------------------------------------
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor =
                        MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

                // Solo validar en el momento de CONNECT
                if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
                    String authHeader = accessor.getFirstNativeHeader("Authorization");

                    if (authHeader != null && authHeader.startsWith("Bearer ")) {
                        String token = authHeader.substring(7);

                        try {
                            String nombre = jwtUtil.extraerNombre(token);
                            UserDetails userDetails = userDetailsService.loadUserByUsername(nombre);

                            if (jwtUtil.esValido(token, userDetails.getUsername())) {

                                UsernamePasswordAuthenticationToken auth =
                                        new UsernamePasswordAuthenticationToken(
                                                userDetails,
                                                null,
                                                userDetails.getAuthorities());

                                accessor.setUser(auth);
                            }
                        } catch (Exception e) {
                            // Token inválido
                        }
                    }
                }

                return message;
            }
        });
    }
}
