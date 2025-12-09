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
        
        log.info("Respuesta de Xano login: {}", response);
        
        LoginResponse loginResponse = new LoginResponse();
        String authToken = (String) response.get("authToken");
        loginResponse.setToken(authToken);
        
        // Si la respuesta de Xano incluye directamente los datos del usuario
        if (response.containsKey("id") || response.containsKey("name") || response.containsKey("email")) {
            UsuarioDTO usuario = mapToUsuarioDTO(response);
            loginResponse.setUsuario(usuario);
            log.info("Usuario mapeado desde respuesta directa: {}", usuario);
        } 
        // Si no, intentar obtener los datos del usuario usando el endpoint /auth/me
        else if (authToken != null) {
            try {
                log.info("Obteniendo datos de usuario autenticado con token");
                @SuppressWarnings("unchecked")
                Map<String, Object> userResponse = xanoApiService.getWithAuth(
                    "/auth/me",
                    Map.class,
                    authToken
                );
                log.info("Datos de usuario obtenidos: {}", userResponse);
                UsuarioDTO usuario = mapToUsuarioDTO(userResponse);
                loginResponse.setUsuario(usuario);
                log.info("Usuario mapeado desde /auth/me: {}", usuario);
            } catch (Exception e) {
                log.error("Error al obtener datos de usuario: {}", e.getMessage());
            }
        } else {
            log.warn("No se encontraron datos de usuario ni token en la respuesta");
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
    public UsuarioDTO actualizarUsuario(String id, Map<String, Object> usuarioData, String authToken) {
        log.info("Actualizando usuario con ID: {}", id);
        
        try {
            // Usar el endpoint correcto de Xano para actualizar perfil (POST, no PATCH)
            @SuppressWarnings("unchecked")
            Map<String, Object> response = xanoApiService.postWithAuth(
                    "/user/edit_profile",
                    usuarioData,
                    Map.class,
                    authToken
            );
            
            log.info("Usuario actualizado exitosamente: {}", response);
            return mapToUsuarioDTO(response);
            
        } catch (Exception e) {
            log.error("Error al actualizar usuario {}: {}", id, e.getMessage());
            throw new RuntimeException("Error al actualizar usuario: " + e.getMessage(), e);
        }
    }
    
    /**
     * Actualizar perfil del usuario autenticado
     */
    public UsuarioDTO actualizarPerfil(Map<String, Object> usuarioData, String authToken) {
        log.info("Actualizando perfil de usuario autenticado");
        
        try {
            // Primero obtener los datos del usuario autenticado para obtener su ID
            @SuppressWarnings("unchecked")
            Map<String, Object> userData = xanoApiService.getWithAuth("/auth/me", Map.class, authToken);
            
            Object userId = userData.get("id");
            if (userId == null) {
                throw new RuntimeException("No se pudo obtener el ID del usuario");
            }
            
            log.info("ID de usuario obtenido: {}", userId);
            
            // Usar el endpoint PUT /user/{user_id} de Xano
            @SuppressWarnings("unchecked")
            Map<String, Object> response = xanoApiService.putWithAuth(
                    "/user/" + userId,
                    usuarioData,
                    Map.class,
                    authToken
            );
            
            log.info("Perfil actualizado exitosamente: {}", response);
            return mapToUsuarioDTO(response);
            
        } catch (Exception e) {
            log.error("Error al actualizar perfil: {}", e.getMessage());
            throw new RuntimeException("Error al actualizar perfil: " + e.getMessage(), e);
        }
    }

    /**
     * Convertir Map a UsuarioDTO
     */
    private UsuarioDTO mapToUsuarioDTO(Map<String, Object> map) {
        UsuarioDTO usuario = new UsuarioDTO();
        
        // Intentar mapear ID
        if (map.containsKey("id")) {
            usuario.setId(Long.valueOf(map.get("id").toString()));
        }
        
        // Intentar mapear nombre (puede venir como "name" o "nombre")
        if (map.containsKey("nombre")) {
            usuario.setNombre((String) map.get("nombre"));
        } else if (map.containsKey("name")) {
            usuario.setNombre((String) map.get("name"));
        }
        
        // Mapear email
        if (map.containsKey("email")) {
            usuario.setEmail((String) map.get("email"));
        }
        
        // Mapear campos opcionales
        if (map.containsKey("telefono")) usuario.setTelefono((String) map.get("telefono"));
        if (map.containsKey("direccion")) usuario.setDireccion((String) map.get("direccion"));
        if (map.containsKey("ciudad")) usuario.setCiudad((String) map.get("ciudad"));
        if (map.containsKey("codigoPostal")) usuario.setCodigoPostal((String) map.get("codigoPostal"));
        
        log.info("Usuario mapeado desde Map: {}", usuario);
        return usuario;
    }
}
