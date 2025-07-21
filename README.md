CATGAMER - Tienda de Videojuegos
CATGAMER es una aplicación web para una tienda de videojuegos desarrollada con Spring Boot. Incluye funcionalidades como autenticación con JWT, gestión de productos, categorías, carrito de compras y pedidos. La interfaz usa Thymeleaf para vistas básicas y AJAX para interacciones dinámicas.

Requisitos
Java 21
Maven (para build y dependencias)
MySQL 8.0+ (base de datos)
VS Code o IntelliJ como IDE (opcional, pero recomendado)
Dependencias clave (en pom.xml):
Spring Boot Starter Web, Security, Data JPA, Thymeleaf
MySQL Connector
JJWT para JWT
Bootstrap y jQuery via WebJars
Lombok 
Configuración de la Base de Datos
Instala MySQL y crea una base de datos llamada tienda_videojuegos:


CREATE DATABASE tienda_videojuegos;
En src/main/resources/application.properties, configura la conexión:

spring.datasource.url=jdbc:mysql://localhost:3306/tienda_videojuegos?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=your_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

jwt.secret=yourSuperLongSecretKeyThatIsAtLeast64CharactersForHS512LikeThisOneWithRandomChars1234567890!@#$%^&*()
jwt.expiration=3600000  # 1 hora


La app crea tablas automáticamente (entidades: Usuario, Role, Producto, Categoria, Carrito, CarritoItem, Pedido, PedidoProducto).
Datos iniciales: En CatgamewebApplication.java, un CommandLineRunner crea roles ("ADMIN", "USUARIO") y un admin predeterminado (email: "admin@tienda.com", pass: "admin123").


Cómo Funciona la Seguridad (Spring Security y JWT)
Spring Security: Configurado en SecurityConfig.java.
Autenticación stateless con JWT (no sesiones).
Filtro JwtAuthenticationFilter valida token en headers o cookies para cada request.
Rutas protegidas:
Públicas: /, /productos, /nosotros, /login, /registro, GET /api/productos, GET /api/categorias.
Admin: /admin/**, POST/PUT/DELETE /api/productos, /api/categorias.
Usuarios: /api/carrito/**, /api/pedidos.
Roles: "ADMIN" para gestión, "USUARIO" para carrito/pedidos.
JWT: Generado en login/registro (AuthController.java).
Usamos JJWT para creación/validación.
Token almacenado en localStorage (frontend) y cookie HttpOnly (para requests sin JS).
Expiración y secret en properties.
CustomUserDetailsService carga user por email para auth.
Flujo:
User loguea en /login (POST con email/pass).
Recibe JWT, almacenado en localStorage/cookie.
Requests protegidos incluyen Authorization: Bearer [token] o cookie.
Filtro valida y setea SecurityContext.
Cómo Interactúan los Componentes
Frontend (Thymeleaf + JS/AJAX):
Vistas en templates/ (e.g., index.html, products/list.html).
Navbar y footer en fragments (reutilizables).
JS en static/js/ (e.g., navbar.js para auth check, productos.js para load/filter, carrito.js para cart).
AJAX calls a APIs con JWT para datos dinámicos (e.g., fetch('/api/productos')).
Backend:
Controladores (@RestController para APIs, @Controller para vistas).
AuthController: Login/registro, genera JWT.
ProductoController: CRUD productos.
CarritoController: Agregar/ver/vaciar carrito.
PedidoController: Finalizar compra.
Services: Lógica de negocio (e.g., ProductoService para filtrar/validar, CarritoService para cantidades).
Repositories: JPA para DB (e.g., ProductoRepository).
Security: Filtro JWT autentica requests.
Interacción Ejemplo (Agregar al Carrito):
Frontend (productos.js): Click en "Agregar" -> AJAX POST /api/carrito/{id} con token.
Filtro JWT valida token, setea user.
CarritoController: Obtiene user ID de Principal, llama CarritoService.agregarProducto.
Service: Get/create carrito, agrega item, save a DB.
Response: JSON carrito actualizado.
Para Pago: Similar, POST /api/pedidos -> Service crea Pedido from Carrito, guarda, vacía carrito.
Despliegue del Proyecto
Local (Desarrollo):
Clona repo: git clone [url].
Configura DB y properties.
Build: mvn clean install.
Run: mvn spring-boot:run o en VS Code (Run Java).
Accede: http://localhost:8080.
Producción (e.g., Heroku/Jar):
Build JAR: mvn clean package.
Run JAR: java -jar target/catgameweb-0.0.1-SNAPSHOT.jar.
Para Heroku: Crea app, configura MySQL add-on, push git, set env vars for properties.
Docker (opcional): Crea Dockerfile:




FROM openjdk:21-jdk-slim
 target/catgameweb-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
Build: docker build -t catgamer . Run: docker run -p 8080:8080 -e SPRING_DATASOURCE_URL=jdbc:mysql://host/db catgamer
Config Prod:
Cambia ddl-auto = validate.
Usa HTTPS for JWT (secure=true in cookie).
Secret JWT en env var.
Tests:
Unit: Usa @Test en services/controllers.
Integration: Postman for APIs with JWT.
Problemas comunes:

JWT invalid: Verifica secret/length.
DB: Asegura constraints (e.g., FK) si ddl-auto=update.
Contacto: [tu email] for issues.
¡Disfruta CATGAMER! 🚀