🎮 CATGAMER - Tienda de Videojuegos
CATGAMER es una aplicación web desarrollada con Spring Boot para la gestión de una tienda de videojuegos. Incluye funcionalidades como autenticación con JWT, gestión de productos, categorías, carrito de compras y pedidos. La interfaz está basada en Thymeleaf

📋 Requisitos
Java 21

Maven (para construcción y gestión de dependencias)

MySQL 8.0+ (base de datos)

VS Code o IntelliJ IDEA (opcional, pero recomendado)

📦 Dependencias principales (pom.xml)
spring-boot-starter-web

spring-boot-starter-security

spring-boot-starter-data-jpa

spring-boot-starter-thymeleaf

mysql-connector-java

jjwt (para JWT)

lombok

webjars (para Bootstrap y jQuery)



1. 🛠️ Configuración de la Base de Datos
Instala MySQL y crea una base de datos llamada tienda_videojuegos:

CREATE DATABASE tienda_videojuegos;  

2. Configura la conexión en src/main/resources/application.properties

spring.datasource.url=jdbc:mysql://localhost:3306/tienda_videojuegos?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=your_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# Configuración JWT
jwt.secret=yourSuperLongSecretKeyThatIsAtLeast64CharactersForHS512LikeThisOneWithRandomChars1234567890!@#$%^&*()
jwt.expiration=3600000  # 1 hora


🧱 Estructura de Entidades
La aplicación crea automáticamente las siguientes tablas:

Usuario

Role

Producto

Categoria

Carrito

CarritoItem

Pedido

PedidoProducto

Se generan datos iniciales desde CatgamewebApplication.java mediante un CommandLineRunner, incluyendo:

Roles: "ADMIN" y "USUARIO"

Usuario admin por defecto:

Email: admin@tienda.com

Contraseña: admin123

🔒 Seguridad (Spring Security + JWT)
Arquitectura de Seguridad
Spring Security configurado en SecurityConfig.java

Autenticación sin estado (stateless) usando JWT

JwtAuthenticationFilter filtra cada solicitud y valida el token JWT desde header o cookie



🔒RUTAS PROTEGIDAS
| Tipo de acceso          | Rutas                                                                                              |
| ----------------------- | -------------------------------------------------------------------------------------------------- |
| **Público**             | `/`, `/productos`, `/nosotros`, `/login`, `/registro`, `GET /api/productos`, `GET /api/categorias` |
| **Administrador**       | `/admin/**`, `POST/PUT/DELETE /api/productos`, `/api/categorias`                                   |
| **Usuario autenticado** | `/api/carrito/**`, `/api/pedidos`                                                                  |


Roles: "ADMIN" para gestión y "USUARIO" para operaciones de compra

JWT generado en login y registro (AuthController.java)

Token almacenado en localStorage (frontend) y en cookie HttpOnly (para seguridad extra)

Expiración y clave secreta configuradas en application.properties

CustomUserDetailsService carga usuario por email para autenticación

Flujo de autenticación
El usuario inicia sesión en /login (POST con email y contraseña)

Recibe el JWT, que se guarda en localStorage y/o cookie

Las solicitudes protegidas deben incluir:

Authorization: Bearer <token> o

Cookie con el JWT

El filtro valida el token y establece el SecurityContext


🔄 Interacción de Componentes
Frontend (Thymeleaf + JavaScript)
Vistas en src/main/resources/templates/ (index.html, products/list.html, etc.)

Navbar y footer como fragmentos reutilizables (fragments/)

Archivos JS en static/js/:

navbar.js: Verificación de autenticación

productos.js: Carga y filtrado de productos

carrito.js: Funciones de carrito de compras

Llamadas AJAX a la API con JWT en headers para contenido dinámico

Backend (Spring Boot)
Controladores

AuthController: Login y registro, genera JWT

ProductoController: CRUD de productos

CarritoController: Gestión del carrito

PedidoController: Finalización de compras

Servicios: lógica de negocio

Repositories: Acceso a base de datos (JPA)

Filtro JWT: Procesa autenticación en cada request

Ejemplo: Agregar al carrito
Usuario hace clic en "Agregar al carrito"

productos.js realiza POST /api/carrito/{id} con JWT

Filtro valida token y obtiene el usuario

CarritoController obtiene el ID de usuario y llama a CarritoService.agregarProducto

Se actualiza el carrito en la base de datos

Respuesta JSON con el carrito actualizado

Ejemplo: Finalizar pedido
Cliente realiza POST /api/pedidos

Servicio genera un nuevo pedido a partir del carrito y lo guarda en la base de datos

El carrito se vacía automáticamente