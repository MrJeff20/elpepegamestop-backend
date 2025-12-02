package com.backend.elpepegamestop.controller;

import com.backend.elpepegamestop.dto.ProductoDTO;
import com.backend.elpepegamestop.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/productos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ProductoController {

    private final ProductoService productoService;

    @GetMapping
    public ResponseEntity<List<ProductoDTO>> getAllProductos(
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) String plataforma,
            @RequestParam(required = false) Boolean destacado,
            @RequestParam(required = false) Boolean enOferta) {

        List<ProductoDTO> productos = productoService.getAllProductos(
                categoria, plataforma, destacado, enOferta);

        return ResponseEntity.ok(productos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoDTO> getProductoById(@PathVariable String id) {
        ProductoDTO producto = productoService.getProductoById(id);
        return ResponseEntity.ok(producto);
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<ProductoDTO>> searchProductos(
            @RequestParam String q) {

        List<ProductoDTO> productos = productoService.searchProductos(q);
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/destacados")
    public ResponseEntity<List<ProductoDTO>> getProductosDestacados() {
        List<ProductoDTO> productos = productoService.getProductosDestacados();
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/ofertas")
    public ResponseEntity<List<ProductoDTO>> getProductosOferta() {
        List<ProductoDTO> productos = productoService.getProductosOferta();
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<ProductoDTO>> getProductosPorCategoria(
            @PathVariable String categoria) {

        List<ProductoDTO> productos = productoService.getProductosPorCategoria(categoria);
        return ResponseEntity.ok(productos);
    }
}
