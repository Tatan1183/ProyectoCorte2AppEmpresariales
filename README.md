# Sistema de Gestión de Clínica Veterinaria

Este proyecto es un sistema completo para la gestión de una clínica veterinaria, desarrollado con Spring Boot y MySQL.

## Características

- Gestión de citas médicas
- Registro de mascotas y sus dueños
- Control de servicios veterinarios
- Administración de personal veterinario
- Sistema de autenticación JWT
- Interfaz de usuario intuitiva

## Tecnologías Utilizadas

- Backend:
  - Spring Boot 3.5.0
  - Spring Data JPA
  - MySQL
  - JWT para autenticación
  - Lombok

- Frontend:
  - HTML5
  - CSS3
  - JavaScript
  - Bootstrap 5
  - Bootstrap Icons

## Estructura del Proyecto

```
src/
├── main/
│   ├── java/
│   │   └── com/app/veterinaria/
│   │       ├── config/
│   │       ├── controller/
│   │       ├── dto/
│   │       ├── model/
│   │       ├── repository/
│   │       ├── security/
│   │       └── service/
│   └── resources/
│       ├── static/
│       └── application.properties
```

## Configuración del Proyecto

1. Clonar el repositorio
2. Configurar la base de datos MySQL en `application.properties`
3. Ejecutar el script SQL para crear la base de datos y tablas
4. Ejecutar el proyecto con Maven: `mvn spring-boot:run`

## API Endpoints

### Autenticación
- POST `/api/auth/login` - Iniciar sesión

### Citas
- GET `/api/citas` - Listar todas las citas
- POST `/api/citas` - Crear nueva cita
- PUT `/api/citas/{id}` - Actualizar cita
- DELETE `/api/citas/{id}` - Eliminar cita

### Clientes
- GET `/api/clientes` - Listar todos los clientes
- POST `/api/clientes` - Registrar nuevo cliente
- PUT `/api/clientes/{id}` - Actualizar cliente
- DELETE `/api/clientes/{id}` - Eliminar cliente

### Mascotas
- GET `/api/mascotas` - Listar todas las mascotas
- POST `/api/mascotas` - Registrar nueva mascota
- PUT `/api/mascotas/{id}` - Actualizar mascota
- DELETE `/api/mascotas/{id}` - Eliminar mascota

### Servicios
- GET `/api/servicios` - Listar todos los servicios
- POST `/api/servicios` - Crear nuevo servicio
- PUT `/api/servicios/{id}` - Actualizar servicio
- DELETE `/api/servicios/{id}` - Eliminar servicio

### Veterinarios
- GET `/api/veterinarios` - Listar todos los veterinarios
- POST `/api/veterinarios` - Registrar nuevo veterinario
- PUT `/api/veterinarios/{id}` - Actualizar veterinario
- DELETE `/api/veterinarios/{id}` - Eliminar veterinario

## Seguridad

El sistema utiliza JWT (JSON Web Tokens) para la autenticación:
- Tokens con expiración de 24 horas
- Rutas protegidas que requieren autenticación
- Almacenamiento seguro de contraseñas

## Capturas de Pantalla

[Aquí puedes agregar capturas de pantalla de tu aplicación]

## Contribuir

1. Fork el proyecto
2. Crear una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abrir un Pull Request

## Licencia

Este proyecto está bajo la Licencia MIT - ver el archivo [LICENSE.md](LICENSE.md) para más detalles.
