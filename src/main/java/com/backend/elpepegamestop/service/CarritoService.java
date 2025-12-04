package com.backend.elpepegamestop.service;

import com.backend.elpepegamestop.dto.ProductoDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class CarritoService {

    private final ProductoService productoService;
    
    // Cache en memoria para carritos (en producción considerar Redis o base de datos)
    private final Map<String, Map<String, Object>> carritosCache = new ConcurrentHashMap<>();

    private static final String CART_PREFIX = "cart:";

    /**
     * Agregar producto al carrito
     */
    public void agregarAlCarrito(String usuarioId, String productoId, Integer cantidad) {
        String key = CART_PREFIX + usuarioId;

        ProductoDTO producto = productoService.getProductoById(productoId);
        if (producto == null) {
            throw new RuntimeException("Producto no encontrado");
        }

        Map<String, Object> carrito = carritosCache.get(key);

        if (carrito == null) {
            carrito = new HashMap<>();
            carrito.put("items", new HashMap<String, Map<String, Object>>());
            carrito.put("total", BigDecimal.ZERO);
        }

        @SuppressWarnings("unchecked")
        Map<String, Map<String, Object>> items = (Map<String, Map<String, Object>>) carrito.get("items");

        if (items.containsKey(productoId)) {
            Map<String, Object> item = items.get(productoId);
            int nuevaCantidad = (int) item.get("cantidad") + cantidad;
            item.put("cantidad", nuevaCantidad);
            item.put("subtotal", BigDecimal.valueOf(producto.getPrice()).multiply(BigDecimal.valueOf(nuevaCantidad)));
        } else {
            Map<String, Object> nuevoItem = new HashMap<>();
            nuevoItem.put("producto", producto);
            nuevoItem.put("cantidad", cantidad);
            nuevoItem.put("precioUnitario", BigDecimal.valueOf(producto.getPrice()));
            nuevoItem.put("subtotal", BigDecimal.valueOf(producto.getPrice()).multiply(BigDecimal.valueOf(cantidad)));
            items.put(productoId, nuevoItem);
        }

        // Actualizar total
        BigDecimal total = items.values().stream()
                .map(item -> (BigDecimal) item.get("subtotal"))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        carrito.put("total", total);
        carrito.put("updatedAt", new Date());

        carritosCache.put(key, carrito);

        log.info("Producto {} agregado al carrito del usuario {}", productoId, usuarioId);
    }

    /**
     * Obtener carrito de usuario
     */
    public Map<String, Object> getCarrito(String usuarioId) {
        String key = CART_PREFIX + usuarioId;

        Map<String, Object> carrito = carritosCache.get(key);

        if (carrito == null) {
            carrito = new HashMap<>();
            carrito.put("items", new HashMap<>());
            carrito.put("total", BigDecimal.ZERO);
        }

        return carrito;
    }

    /**
     * Eliminar producto del carrito
     */
    public void eliminarDelCarrito(String usuarioId, String productoId) {
        String key = CART_PREFIX + usuarioId;

        Map<String, Object> carrito = carritosCache.get(key);

        if (carrito != null) {
            @SuppressWarnings("unchecked")
            Map<String, Map<String, Object>> items = (Map<String, Map<String, Object>>) carrito.get("items");

            if (items != null && items.containsKey(productoId)) {
                items.remove(productoId);

                // Actualizar total
                BigDecimal total = items.values().stream()
                        .map(item -> (BigDecimal) item.get("subtotal"))
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                carrito.put("total", total);
                carrito.put("updatedAt", new Date());

                carritosCache.put(key, carrito);

                log.info("Producto {} eliminado del carrito del usuario {}", productoId, usuarioId);
            }
        }
    }

    /**
     * Vaciar carrito
     */
    public void vaciarCarrito(String usuarioId) {
        String key = CART_PREFIX + usuarioId;
        carritosCache.remove(key);
        log.info("Carrito vaciado para el usuario {}", usuarioId);
    }
}
