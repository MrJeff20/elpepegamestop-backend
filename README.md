# ElPepeGameStop - Backend

Backend Spring Boot para la aplicación ElPepeGameStop 2.0, integrado con la API de Xano.

## 🚀 Características

- **Framework**: Spring Boot 3.1.5
- **Java**: 17
- **Integración**: Xano API
- **Cache**: En memoria (Simple Cache)
- **CORS**: Configurado para desarrollo local y producción

## 📋 Requisitos Previos

- Java 17 o superior
- Maven 3.6+
- Conexión a Internet (para acceder a la API de Xano)

## 🔧 Configuración

### Variables de Entorno

El backend utiliza la siguiente configuración en `application.yaml`:

```yaml
server:
  port: 8080
  servlet:
    context-path: /api

xano:
  api:
    base-url: "https://x8ki-letl-twmt.n7.xano.io/api:k1VDdCRF"
```

### CORS

El backend está configurado para aceptar peticiones desde:
- `http://localhost:3000` (React development)
- `http://localhost:5173` (Vite development)
- `https://mrjeff20.github.io` (GitHub Pages)

## 🏃‍♂️ Ejecución

### Modo Desarrollo

```bash
# Con Maven Wrapper
./mvnw spring-boot:run

# Con Maven instalado
mvn spring-boot:run
```

### Compilar JAR

```bash
./mvnw clean package
java -jar target/gamestop-backend-1.0.0.jar
```

## 📡 Endpoints Disponibles

### Productos

- `GET /api/productos` - Listar todos los productos
  - Query params: `categoria`, `plataforma`, `destacado`, `enOferta`
- `GET /api/productos/{id}` - Obtener producto por ID
- `GET /api/productos/buscar?q={termino}` - Buscar productos
- `GET /api/productos/destacados` - Productos destacados
- `GET /api/productos/ofertas` - Productos en oferta
- `GET /api/productos/categoria/{categoria}` - Productos por categoría

### Usuarios

- `POST /api/usuarios/registro` - Registrar nuevo usuario
- `POST /api/usuarios/login` - Iniciar sesión
- `GET /api/usuarios/{id}` - Obtener usuario por ID
- `PUT /api/usuarios/{id}` - Actualizar usuario

### Carrito

- `GET /api/carrito/{usuarioId}` - Obtener carrito
- `POST /api/carrito/{usuarioId}/agregar` - Agregar producto
  - Params: `productoId`, `cantidad`
- `DELETE /api/carrito/{usuarioId}/eliminar/{productoId}` - Eliminar producto
- `DELETE /api/carrito/{usuarioId}/vaciar` - Vaciar carrito

### Pedidos

- `POST /api/pedidos` - Crear pedido
- `GET /api/pedidos/{id}` - Obtener pedido por ID
- `GET /api/pedidos/usuario/{usuarioId}` - Pedidos de un usuario
- `PUT /api/pedidos/{id}/estado` - Actualizar estado del pedido
- `DELETE /api/pedidos/{id}` - Cancelar pedido

## 🏗️ Estructura del Proyecto

```
src/main/java/com/backend/elpepegamestop/
├── config/              # Configuraciones (CORS, Cache, RestTemplate)
├── controller/          # Controladores REST
├── dto/                 # Data Transfer Objects
├── exception/           # Excepciones personalizadas y manejo global
├── service/             # Lógica de negocio
└── ElpepegamestopApplication.java
```

## 🔌 Integración con Xano

El servicio `XanoApiService` maneja todas las comunicaciones con la API de Xano, incluyendo:

- GET requests con query parameters
- POST requests para creación
- PUT requests para actualizaciones
- DELETE requests para eliminación

Todas las respuestas son mapeadas automáticamente a DTOs usando Jackson.

## 📦 Dependencias Principales

- `spring-boot-starter-web` - Framework web
- `spring-boot-starter-cache` - Soporte para caché
- `spring-boot-starter-validation` - Validaciones
- `lombok` - Reducción de boilerplate
- `jackson-databind` - Serialización JSON

## 🚨 Manejo de Errores

El backend incluye un manejador global de excepciones que captura:
- Recursos no encontrados (404)
- Peticiones incorrectas (400)
- Errores de comunicación con Xano
- Errores internos del servidor (500)

Todas las respuestas de error siguen el formato:

```json
{
  "success": false,
  "message": "Mensaje descriptivo del error",
  "data": null,
  "error": "Detalles del error"
}
```

## 🔐 Seguridad

- CORS habilitado para orígenes específicos
- Headers de seguridad configurados
- Validación de entrada en DTOs

## 📝 Notas Importantes

1. **Cache**: Actualmente usa cache en memoria. Para producción, considerar Redis.
2. **Carrito**: El carrito se almacena en memoria. Los datos se pierden al reiniciar el servidor.
3. **Autenticación**: La autenticación se maneja a través de Xano API.

## 🤝 Contribuir

Para contribuir al proyecto:

1. Fork el repositorio
2. Crea una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

## 📄 Licencia

Este proyecto es parte de un trabajo académico para Full Stack 2 y Taller de Base de Datos.

## 👥 Autores

- MrJeff20
- vicente-arriagada-lost
- SeishiroN

## 🔗 Enlaces

- [Frontend Repository](https://github.com/MrJeff20/elpepegamestop2.0)
- [API Xano](https://x8ki-letl-twmt.n7.xano.io/api:k1VDdCRF)
