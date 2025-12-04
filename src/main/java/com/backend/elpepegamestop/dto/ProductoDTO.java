package com.backend.elpepegamestop.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoDTO {
    private Long id;
    
    private String name;
    
    private String description;
    
    private Integer price;
    
    @JsonProperty("image_url")
    private Object imageUrl;
    
    private Integer stock;
    
    private String category;
    
    @JsonProperty("created_at")
    private Long createdAt;
    
    // Getter personalizado para obtener la URL de la imagen
    @SuppressWarnings("unchecked")
    public String getImageUrl() {
        if (imageUrl instanceof Map) {
            Map<String, Object> imageMap = (Map<String, Object>) imageUrl;
            String path = (String) imageMap.get("path");
            if (path != null && !path.isEmpty()) {
                return path;
            }
        } else if (imageUrl instanceof String) {
            return (String) imageUrl;
        }
        return "";
    }
    
    // Getter personalizado para disponible basado en stock
    public Boolean getDisponible() {
        return stock != null && stock > 0;
    }
}
