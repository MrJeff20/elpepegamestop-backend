package com.backend.elpepegamestop.service;

import com.backend.elpepegamestop.dto.ProductoDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductoService {

    private final XanoApiService xanoApiService;

    /**
     * Obtener todos los productos con filtros opcionales
     */
    public List<ProductoDTO> getAllProductos(
            String categoria,
            String plataforma,
            Boolean destacado,
            Boolean enOferta) {

        log.info("Fetching productos from Xano with filters - categoria: {}, plataforma: {}",
                categoria, plataforma);

        // Ajustar según la estructura real de la respuesta de Xano
        Map<String, Object> params = Map.of();

        ProductoDTO[] productos = xanoApiService.get(
                "/productos",
                ProductoDTO[].class,
                params
        );

        List<ProductoDTO> filteredProductos = Arrays.stream(productos)
                .filter(p -> categoria == null || p.getCategoria().equalsIgnoreCase(categoria))
                .filter(p -> plataforma == null || p.getPlataforma().equalsIgnoreCase(plataforma))
                .filter(p -> destacado == null || p.getDestacado().equals(destacado))
                .filter(p -> enOferta == null || p.getEnOferta().equals(enOferta))
                .collect(Collectors.toList());

        log.info("Found {} productos", filteredProductos.size());
        return filteredProductos;
    }

    /**
     * Obtener producto por ID
     */
    public ProductoDTO getProductoById(String id) {
        log.info("Fetching producto by ID: {}", id);

        return xanoApiService.get(
                "/productos/" + id,
                ProductoDTO.class,
                null
        );
    }

    /**
     * Buscar productos por término
     */
    public List<ProductoDTO> searchProductos(String termino) {
        log.info("Searching productos with term: {}", termino);

        ProductoDTO[] productos = xanoApiService.get(
                "/productos",
                ProductoDTO[].class,
                null
        );

        return Arrays.stream(productos)
                .filter(p -> p.getNombre().toLowerCase().contains(termino.toLowerCase()) ||
                        p.getDescripcion().toLowerCase().contains(termino.toLowerCase()))
                .collect(Collectors.toList());
    }

    /**
     * Obtener productos destacados
     */
    public List<ProductoDTO> getProductosDestacados() {
        return getAllProductos(null, null, true, null);
    }

    /**
     * Obtener productos en oferta
     */
    public List<ProductoDTO> getProductosOferta() {
        return getAllProductos(null, null, null, true);
    }

    /**
     * Obtener productos por categoría
     */
    public List<ProductoDTO> getProductosPorCategoria(String categoria) {
        return getAllProductos(categoria, null, null, null);
    }
}
