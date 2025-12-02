package com.backend.elpepegamestop.controller;

import com.backend.elpepegamestop.service.CarritoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/carrito")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CarritoController {

    private final CarritoService carritoService;

    @GetMapping("/{usuarioId}")
    public ResponseEntity<Map<String, Object>> getCarrito(
            @PathVariable String usuarioId) {

        Map<String, Object> carrito = carritoService.getCarrito(usuarioId);
        return ResponseEntity.ok(carrito);
    }

    @PostMapping("/{usuarioId}/agregar")
    public ResponseEntity<Void> agregarAlCarrito(
            @PathVariable String usuarioId,
            @RequestParam String productoId,
            @RequestParam(defaultValue = "1") Integer cantidad) {

        carritoService.agregarAlCarrito(usuarioId, productoId, cantidad);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{usuarioId}/eliminar/{productoId}")
    public ResponseEntity<Void> eliminarDelCarrito(
            @PathVariable String usuarioId,
            @PathVariable String productoId) {

        carritoService.eliminarDelCarrito(usuarioId, productoId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{usuarioId}/vaciar")
    public ResponseEntity<Void> vaciarCarrito(
            @PathVariable String usuarioId) {

        carritoService.vaciarCarrito(usuarioId);
        return ResponseEntity.ok().build();
    }
}
