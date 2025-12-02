package com.backend.elpepegamestop.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ProductoDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private BigDecimal precioOriginal;
    private Integer stock;
    private String categoria;
    private String plataforma;
    private String desarrollador;
    private String imagenUrl;
    private List<String> imagenes;
    private Double rating;
    private Integer cantidadResenas;
    private Boolean destacado;
    private Boolean enOferta;
    private BigDecimal descuento;
    private LocalDateTime fechaLanzamiento;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @JsonProperty("xano_id")
    private String xanoId;
}
