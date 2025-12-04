package com.backend.elpepegamestop.service;

import com.backend.elpepegamestop.dto.LoginRequest;
import com.backend.elpepegamestop.dto.LoginResponse;
import com.backend.elpepegamestop.dto.RegisterRequest;
import com.backend.elpepegamestop.dto.UsuarioDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class UsuarioService {

    private final XanoApiService xanoApiService;

    /**
     * Registrar nuevo usuario - Usa el endpoint de signup de Xano
     */
    public Map<String, Object> registrarUsuario(RegisterRequest registerRequest) {
        log.info("Registrando nuevo usuario: {}", registerRequest.getEmail());
        
        // Crear el body con la estructura que espera Xano signup
        Map<String, Object> body = new HashMap<>();
        body.put("name", registerRequest.getName());
        body.put("email", registerRequest.getEmail());
        body.put("password", registerRequest.getPassword());
        
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> response = xanoApiService.post(
                    "/auth/signup",
                    body,
                    Map.class
            );
            
            log.info("Usuario registrado exitosamente: {}", registerRequest.getEmail());
            return response;
            
        } catch (Exception e) {
            log.error("Error al registrar usuario {}: {}", registerRequest.getEmail(), e.getMessage());
            throw new RuntimeException("Error al registrar usuario: " + e.getMessage(), e);
        }
    }

    /**
     * Login de usuario - Autentica con Xano y devuelve token y datos de usuario
     */
    public Map<String, Object> login(LoginRequest credentials) {
        log.info("Intento de login para: {}", credentials.getEmail());
        
        try {
            // Llamar al endpoint de autenticación de Xano
            @SuppressWarnings("unchecked")
            Map<String, Object> response = xanoApiService.post(
                    "/auth/login",
                    credentials,
                    Map.class
            );
            
            log.info("Login exitoso para: {}", credentials.getEmail());
            return response;
            
        } catch (Exception e) {
            log.error("Error en login para {}: {}", credentials.getEmail(), e.getMessage());
            throw new RuntimeException("Credenciales inválidas o error en el servidor", e);
        }
    }

    /**
     * Login con respuesta formateada (alternativa)
     */
    public LoginResponse loginFormatted(LoginRequest credentials) {
        Map<String, Object> response = login(credentials);
        
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken((String) response.get("authToken"));
        
        // Convertir datos del usuario si existen
        if (response.containsKey("id")) {
            UsuarioDTO usuario = mapToUsuarioDTO(response);
            loginResponse.setUsuario(usuario);
        }
        
        loginResponse.setMessage("Login exitoso");
        return loginResponse;
    }

    /**
     * Obtener usuario por ID
     */
    public UsuarioDTO getUsuario(String id) {
        log.info("Obteniendo usuario con ID: {}", id);
        
        return xanoApiService.get(
                "/usuarios/" + id,
                UsuarioDTO.class,
                null
        );
    }

    /**
     * Actualizar usuario
     */
    public UsuarioDTO actualizarUsuario(String id, Map<String, Object> usuarioData) {
        log.info("Actualizando usuario con ID: {}", id);
        
        return xanoApiService.put(
                "/usuarios/" + id,
                usuarioData,
                UsuarioDTO.class
        );
    }

    /**
     * Convertir Map a UsuarioDTO
     */
    private UsuarioDTO mapToUsuarioDTO(Map<String, Object> map) {
        UsuarioDTO usuario = new UsuarioDTO();
        if (map.containsKey("id")) usuario.setId(Long.valueOf(map.get("id").toString()));
        if (map.containsKey("nombre")) usuario.setNombre((String) map.get("nombre"));
        if (map.containsKey("email")) usuario.setEmail((String) map.get("email"));
        if (map.containsKey("telefono")) usuario.setTelefono((String) map.get("telefono"));
        if (map.containsKey("direccion")) usuario.setDireccion((String) map.get("direccion"));
        if (map.containsKey("ciudad")) usuario.setCiudad((String) map.get("ciudad"));
        if (map.containsKey("codigoPostal")) usuario.setCodigoPostal((String) map.get("codigoPostal"));
        return usuario;
    }
}
