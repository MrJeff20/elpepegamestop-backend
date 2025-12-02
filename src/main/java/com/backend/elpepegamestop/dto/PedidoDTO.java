package com.backend.elpepegamestop.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PedidoDTO {
    private Long id;
    private String numeroPedido;
    private Long usuarioId;
    private String estado;
    private BigDecimal total;
    private String direccionEnvio;
    private String metodoPago;
    private LocalDateTime fechaPedido;
    private List<ItemPedidoDTO> items;
    private String xanoId;

    @Data
    public static class ItemPedidoDTO {
        private Long productoId;
        private String nombreProducto;
        private Integer cantidad;
        private BigDecimal precioUnitario;
        private BigDecimal subtotal;
    }
}
