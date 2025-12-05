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
    private Object imageUrlRaw;
    
    private Integer stock;
    
    private String category;
    
    @JsonProperty("created_at")
    private Long createdAt;
    
    // Getter personalizado para obtener la URL de la imagen
    @SuppressWarnings("unchecked")
    @JsonProperty("imageUrl")
    public String getImageUrl() {
        if (imageUrlRaw instanceof Map) {
            Map<String, Object> imageMap = (Map<String, Object>) imageUrlRaw;
            
            // Intentar obtener la URL completa primero
            Object urlObj = imageMap.get("url");
            if (urlObj != null && !urlObj.toString().isEmpty()) {
                return urlObj.toString();
            }
            
            // Si no hay URL, intentar construirla desde el path
            String path = (String) imageMap.get("path");
            if (path != null && !path.isEmpty()) {
                // Si el path ya es una URL completa, devolverla tal cual
                if (path.startsWith("http")) {
                    return path;
                }
                // Si es solo un nombre de archivo, construir la URL de Xano
                // Formato: https://x8ki-letl-twmt.n7.xano.io/vault/{workspace_id}/{path}
                return "https://x8ki-letl-twmt.n7.xano.io/vault/RhHXFYRP/" + path;
            }
        } else if (imageUrlRaw instanceof String) {
            String urlStr = (String) imageUrlRaw;
            if (urlStr.startsWith("http")) {
                return urlStr;
            }
            return "https://x8ki-letl-twmt.n7.xano.io/vault/RhHXFYRP/" + urlStr;
        }
        return "";
    }
    
    // Setter para imageUrlRaw desde image_url de Xano
    public void setImageUrl(Object imageUrl) {
        this.imageUrlRaw = imageUrl;
    }
    
    // Getter personalizado para disponible basado en stock
    public Boolean getDisponible() {
        return stock != null && stock > 0;
    }
}
