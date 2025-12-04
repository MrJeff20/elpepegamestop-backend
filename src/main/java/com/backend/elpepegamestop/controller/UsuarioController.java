package com.backend.elpepegamestop.controller;

import com.backend.elpepegamestop.dto.LoginRequest;
import com.backend.elpepegamestop.dto.LoginResponse;
import com.backend.elpepegamestop.dto.RegisterRequest;
import com.backend.elpepegamestop.dto.UsuarioDTO;
import com.backend.elpepegamestop.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping("/signup")
    public ResponseEntity<Map<String, Object>> registrarUsuario(@RequestBody RegisterRequest registerRequest) {
        Map<String, Object> response = usuarioService.registrarUsuario(registerRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest credentials) {
        Map<String, Object> response = usuarioService.login(credentials);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login/formatted")
    public ResponseEntity<LoginResponse> loginFormatted(@RequestBody LoginRequest credentials) {
        LoginResponse response = usuarioService.loginFormatted(credentials);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> getUsuario(@PathVariable String id) {
        UsuarioDTO usuario = usuarioService.getUsuario(id);
        return ResponseEntity.ok(usuario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> actualizarUsuario(
            @PathVariable String id,
            @RequestBody Map<String, Object> usuarioData) {

        UsuarioDTO usuario = usuarioService.actualizarUsuario(id, usuarioData);
        return ResponseEntity.ok(usuario);
    }
}
