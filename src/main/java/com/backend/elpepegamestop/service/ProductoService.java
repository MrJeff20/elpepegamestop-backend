package com.backend.elpepegamestop.service;

import com.backend.elpepegamestop.dto.ProductoDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductoService {

    private final XanoApiService xanoApiService;
    private final RestTemplate restTemplate;

    /**
     * Obtener todos los productos con filtros opcionales
     */
    public List<ProductoDTO> getAllProductos(
            String categoria,
            String plataforma,
            Boolean destacado,
            Boolean enOferta) {

        log.info("Fetching productos with filters - categoria: {}", categoria);

        try {
            // Llamar al endpoint de Xano usando XanoApiService
            String endpoint = "/query/3240164";
            
            ParameterizedTypeReference<List<ProductoDTO>> typeRef = 
                new ParameterizedTypeReference<List<ProductoDTO>>() {};
            
            // Construir URL completa
            String url = "https://x8ki-letl-twmt.n7.xano.io/api:RhHXFYRP" + endpoint;
            
            ResponseEntity<List<ProductoDTO>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                typeRef
            );

            List<ProductoDTO> productos = response.getBody();
            
            // Aplicar filtros localmente si es necesario
            if (productos != null && categoria != null) {
                productos = productos.stream()
                    .filter(p -> p.getCategory() != null && p.getCategory().equalsIgnoreCase(categoria))
                    .collect(Collectors.toList());
                
                log.info("Found {} productos after filtering", productos.size());
            } else if (productos != null) {
                log.info("Found {} productos", productos.size());
            }

            return productos != null ? productos : List.of();
        } catch (Exception e) {
            log.error("Error fetching productos from Xano API", e);
            throw new RuntimeException("Error al obtener productos de la API: " + e.getMessage(), e);
        }
    }

    /**
     * Obtener producto por ID
     */
    public ProductoDTO getProductoById(String id) {
        log.info("Fetching producto by ID: {}", id);

        try {
            return xanoApiService.get(xanoApiService.getProductosUrl(), "/product/" + id, ProductoDTO.class, null);
        } catch (Exception e) {
            log.error("Error fetching producto by ID: {}", id, e);
            throw new RuntimeException("Producto no encontrado: " + e.getMessage(), e);
        }
    }

    /**
     * Buscar productos por término
     */
    public List<ProductoDTO> searchProductos(String termino) {
        log.info("Searching productos with term: {}", termino);

        try {
            // Obtener todos los productos y filtrar localmente
            List<ProductoDTO> allProductos = getAllProductos(null, null, null, null);
            
            return allProductos.stream()
                .filter(p -> (p.getName() != null && p.getName().toLowerCase().contains(termino.toLowerCase())) ||
                            (p.getDescription() != null && p.getDescription().toLowerCase().contains(termino.toLowerCase())))
                .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error searching productos", e);
            throw new RuntimeException("Error al buscar productos: " + e.getMessage(), e);
        }
    }

    /**
     * Obtener productos destacados
     */
    public List<ProductoDTO> getProductosDestacados() {
        log.info("Fetching productos destacados");
        // Por ahora retornar los primeros 6 productos
        List<ProductoDTO> allProductos = getAllProductos(null, null, null, null);
        return allProductos.stream().limit(6).collect(Collectors.toList());
    }

    /**
     * Obtener productos en oferta
     */
    public List<ProductoDTO> getProductosOferta() {
        log.info("Fetching productos en oferta");
        // Por ahora retornar todos los productos
        return getAllProductos(null, null, null, null);
    }

    /**
     * Obtener productos por categoría
     */
    public List<ProductoDTO> getProductosPorCategoria(String categoria) {
        log.info("Fetching productos por categoria: {}", categoria);
        return getAllProductos(categoria, null, null, null);
    }
}
