package com.backend.elpepegamestop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarritoDTO {
    private String usuarioId;
    private List<ItemCarritoDTO> items = new ArrayList<>();
    private BigDecimal total = BigDecimal.ZERO;
    private Integer cantidadTotal = 0;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItemCarritoDTO {
        private ProductoDTO producto;
        private Integer cantidad;
        private BigDecimal precioUnitario;
        private BigDecimal subtotal;
    }
}
