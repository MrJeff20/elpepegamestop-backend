package com.backend.elpepegamestop.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class UsuarioDTO {
    private Long id;
    private String nombre;
    private String email;
    private String telefono;
    private String direccion;
    private String ciudad;
    private String codigoPostal;
    private Boolean emailVerificado;
    private Boolean activo;
    private LocalDateTime fechaRegistro;
    private List<String> roles;
    private String xanoId;
}
