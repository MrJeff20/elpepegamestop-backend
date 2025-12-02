package com.backend.elpepegamestop.controller;

import com.backend.elpepegamestop.dto.UsuarioDTO;
import com.backend.elpepegamestop.service.XanoApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UsuarioController {

    private final XanoApiService xanoApiService;

    @PostMapping("/registro")
    public ResponseEntity<UsuarioDTO> registrarUsuario(@RequestBody Map<String, Object> usuarioData) {
        UsuarioDTO usuario = xanoApiService.post(
                "/usuarios",
                usuarioData,
                UsuarioDTO.class
        );
        return ResponseEntity.ok(usuario);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> credentials) {
        // Este endpoint sería para autenticación con Xano
        // Ajustar según la API de autenticación de Xano
        Map<String, Object> response = xanoApiService.post(
                "/auth/login",
                credentials,
                Map.class
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> getUsuario(@PathVariable String id) {
        UsuarioDTO usuario = xanoApiService.get(
                "/usuarios/" + id,
                UsuarioDTO.class,
                null
        );
        return ResponseEntity.ok(usuario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> actualizarUsuario(
            @PathVariable String id,
            @RequestBody Map<String, Object> usuarioData) {

        UsuarioDTO usuario = xanoApiService.put(
                "/usuarios/" + id,
                usuarioData,
                UsuarioDTO.class
        );
        return ResponseEntity.ok(usuario);
    }
}
