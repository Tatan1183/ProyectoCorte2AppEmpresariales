# VetSys Pro - Sistema de Gestión de Clínica Veterinaria

Este proyecto es un sistema web completo para la gestión integral de una clínica veterinaria, desarrollado con Spring Boot para el backend y una interfaz web interactiva con HTML, CSS y JavaScript para el frontend.

## Características Principales

*   Gestión Centralizada: Administra citas, clientes, mascotas (con soporte para imágenes), servicios y personal veterinario.
*   Interfaz Web Moderna: Diseño limpio y responsive utilizando Bootstrap 5, facilitando su uso en diferentes dispositivos.
*   Seguridad Robusta: Sistema de autenticación y autorización basado en JSON Web Tokens (JWT) para proteger el acceso a la API REST.
*   API RESTful: Backend bien estructurado siguiendo las mejores prácticas de Spring Boot (Controllers, Services, Repositories, DTOs).
*   Base de Datos Relacional: Persistencia de datos utilizando Spring Data JPA y MySQL, con un esquema optimizado.
*   Subida de Archivos: Funcionalidad para cargar imágenes de mascotas y veterinarios.

## Tecnologías Utilizadas

*   Backend:
    *   Java
    *   Spring Boot 3.x (Asegúrate de poner la versión exacta que usas si la sabes, ej. 3.2.5)
    *   Spring Security (con JWT)
    *   Spring Data JPA / Hibernate
    *   MySQL Connector/J
    *   Lombok
    *   Maven (o Gradle)
*   Frontend:
    *   HTML5
    *   CSS3
    *   JavaScript (ES6+)
    *   Bootstrap 5
    *   Bootstrap Icons
*   Base de Datos:
    *   MySQL

## Estructura del Proyecto

.
├── src
│ ├── main
│ │ ├── java
│ │ │ └── com
│ │ │ └── app
│ │ │ └── veterinaria
│ │ │ ├── config # Configuración (Jackson, etc.)
│ │ │ ├── controller # Controladores REST API
│ │ │ ├── dto # Data Transfer Objects
│ │ │ ├── model # Entidades JPA (Modelos de Datos)
│ │ │ ├── repository # Repositorios Spring Data JPA
│ │ │ ├── security # Clases relacionadas con JWT y Spring Security
│ │ │ └── service # Lógica de negocio
│ │ └── resources
│ │ ├── static # Archivos estáticos (CSS, JS, Imágenes)
│ │ │ ├── css/
│ │ │ ├── images/
│ │ │ └── js/
│ │ ├── templates # 
│ │ └── application.properties # Configuración de la aplicación
│ └── test # (Opcional: Pruebas unitarias/integración)
├── pom.xml # Dependencias y configuración de Maven
└── README.md



*(Nota: Ajusta la estructura si difiere ligeramente, por ejemplo, si tienes `templates` o la ubicación de `app.js` y `auth.js`)*

## Configuración y Ejecución

1.  Clonar Repositorio:
    ```bash
    git clone <URL_DEL_REPOSITORIO>
    cd <NOMBRE_DEL_DIRECTORIO>
    ```
2.  Base de Datos:
    *   Asegúrate de tener MySQL instalado y corriendo.
    *   Crea la base de datos:
        ```sql
        CREATE DATABASE IF NOT EXISTS appveterinaria;
        ```
    *   Configura la conexión en `src/main/resources/application.properties` (URL, usuario, contraseña, puerto si es diferente al 3306).
    *   Las tablas se crearán o actualizarán automáticamente al iniciar la aplicación gracias a `spring.jpa.hibernate.ddl-auto=update`.
    *   (Opcional) Ejecuta el script SQL proporcionado (`base_de_datos.sql` o similar si lo tienes) para poblar con datos iniciales si `ddl-auto` no es `create` o si quieres datos específicos. *Nota: El script proporcionado ya incluye inserts de ejemplo.
3.  Backend:
    *   Abre el proyecto en tu IDE (NetBeans, IntelliJ, VSCode con soporte Java/Maven).
    *   Asegúrate de que las dependencias de Maven se descarguen.
    *   Verifica la configuración de JWT (`jwt.secret`, `jwt.expiration`) en `application.properties`.
    *   Ejecuta la aplicación Spring Boot (desde el IDE o usando Maven):
        ```bash
        mvn spring-boot:run
        ```
4.  Frontend:
    *   La aplicación web estará disponible en `http://localhost:8095` (o el puerto que hayas configurado en `application.properties`).
    *   Accede primero a `http://localhost:8095/login.html` para iniciar sesión.
    *   Credenciales de Ejemplo: Puedes usar un usuario creado por el script SQL o registrar uno nuevo si la funcionalidad está habilitada. Por ejemplo, para el usuario insertado `Ana Gómez`, el email es `ana@veterinaria.com` y la contraseña *antes de hashear* era `password123`. Importante: La contraseña en la BD está hasheada. Necesitarás registrar un usuario nuevo o asegurarte de que el hash en la BD corresponda a una contraseña conocida si `ddl-auto` es `update`.*

## Seguridad con JWT

El sistema utiliza JSON Web Tokens para la autenticación stateless:

1.  El usuario envía credenciales (email/password) al endpoint `/api/auth/login`.
2.  Si son válidas, el servidor genera un JWT firmado con una clave secreta (`jwt.secret`) y con un tiempo de expiración (`jwt.expiration`).
3.  El token se devuelve al cliente, que lo almacena (en `localStorage`).
4.  Para acceder a endpoints protegidos (`/api/citas`, `/api/mascotas`, etc.), el cliente debe incluir el token en la cabecera `Authorization` como `Bearer <token>`.
5.  Un filtro (`JwtTokenFilter`) en el backend intercepta cada solicitud, valida el token (firma, expiración) y establece el contexto de seguridad de Spring si es válido.
6.  Las rutas públicas (`/login.html`, `/api/auth/login`, archivos estáticos) no requieren token.

## API Endpoints Principales

*   Autenticación:
    *   `POST /api/auth/login`: Iniciar sesión y obtener token JWT.
*   **Citas:**
    *   `GET /api/citas`: Listar todas.
    *   `GET /api/citas/{id}`: Obtener una cita.
    *   `POST /api/citas`: Crear nueva.
    *   `PUT /api/citas/{id}`: Actualizar existente.
    *   `DELETE /api/citas/{id}`: Eliminar.
*   Clientes:
    *   `GET /api/clientes`: Listar todos.
    *   `GET /api/clientes/{id}`: Obtener un cliente.
    *   `POST /api/clientes`: Crear nuevo.
    *   `PUT /api/clientes/{id}`: Actualizar existente.
    *   `DELETE /api/clientes/{id}`: Eliminar.
*   Mascotas:
    *   `GET /api/mascotas`: Listar todas.
    *   `GET /api/mascotas/{id}`: Obtener una mascota.
    *   `POST /api/mascotas`: Crear nueva.
    *   `POST /api/mascotas/upload`: Subir imagen para mascota (devuelve nombre del archivo).
    *   `PUT /api/mascotas/{id}`: Actualizar existente.
    *   `DELETE /api/mascotas/{id}`: Eliminar.
*   Servicios:
    *   `GET /api/servicios`: Listar todos.
    *   `GET /api/servicios/{id}`: Obtener un servicio.
    *   `POST /api/servicios`: Crear nuevo.
    *   `PUT /api/servicios/{id}`: Actualizar existente.
    *   `DELETE /api/servicios/{id}`: Eliminar.
*   Veterinarios:
    *   `GET /api/veterinarios`: Listar todos.
    *   `GET /api/veterinarios/{id}`: Obtener un veterinario.
    *   `POST /api/veterinarios`: Crear nuevo.
    *   `POST /api/veterinarios/upload`: Subir imagen para veterinario (devuelve nombre del archivo).
    *   `PUT /api/veterinarios/{id}`: Actualizar existente.
    *   `DELETE /api/veterinarios/{id}`: Eliminar.

## Contribuir

¡Las contribuciones son bienvenidas!

1.  Haz un Fork del proyecto.
2.  Crea tu rama de feature (`git checkout -b feature/NuevaFuncionalidad`).
3.  Haz commit de tus cambios (`git commit -m 'Agrega NuevaFuncionalidad'`).
4.  Haz Push a la rama (`git push origin feature/NuevaFuncionalidad`).
5.  Abre un Pull Request.

## Licencia

Este proyecto está bajo la Licencia MIT. Consulta el archivo `LICENSE` (si existe) para más detalles.
