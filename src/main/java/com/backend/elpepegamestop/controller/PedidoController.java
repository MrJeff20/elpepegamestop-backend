package com.backend.elpepegamestop.controller;

import com.backend.elpepegamestop.dto.PedidoDTO;
import com.backend.elpepegamestop.service.XanoApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/pedidos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PedidoController {

    private final XanoApiService xanoApiService;

    @PostMapping
    public ResponseEntity<PedidoDTO> crearPedido(@RequestBody Map<String, Object> pedidoData) {
        PedidoDTO pedido = xanoApiService.post(
                "/pedidos",
                pedidoData,
                PedidoDTO.class
        );
        return ResponseEntity.ok(pedido);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoDTO> getPedido(@PathVariable String id) {
        PedidoDTO pedido = xanoApiService.get(
                "/pedidos/" + id,
                PedidoDTO.class,
                null
        );
        return ResponseEntity.ok(pedido);
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<PedidoDTO>> getPedidosByUsuario(@PathVariable String usuarioId) {
        PedidoDTO[] pedidos = xanoApiService.get(
                "/pedidos/usuario/" + usuarioId,
                PedidoDTO[].class,
                null
        );
        return ResponseEntity.ok(List.of(pedidos));
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<PedidoDTO> actualizarEstado(
            @PathVariable String id,
            @RequestBody Map<String, String> estadoData) {
        
        PedidoDTO pedido = xanoApiService.put(
                "/pedidos/" + id + "/estado",
                estadoData,
                PedidoDTO.class
        );
        return ResponseEntity.ok(pedido);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelarPedido(@PathVariable String id) {
        xanoApiService.delete("/pedidos/" + id, Void.class);
        return ResponseEntity.ok().build();
    }
}
