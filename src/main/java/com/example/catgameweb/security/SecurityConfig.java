package com.example.catgameweb.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtFilter) throws Exception {
        http
            .csrf(csrf -> csrf.disable())  // Deshabilitar CSRF para APIs
            .formLogin(form -> form.disable())  // Deshabilitar form login predeterminado
            .httpBasic(httpBasic -> httpBasic.disable())  // Deshabilitar HTTP Basic
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.GET, "/").permitAll()  // Permite acceso inicial a index.html
                .requestMatchers(HttpMethod.POST, "/login", "/registro").permitAll()
                .requestMatchers("/login", "/registro", "/css/**", "/js/**", "/img/**", "/webjars/**").permitAll()
                .requestMatchers("/productos", "/nosotros", "/admin/categorias").permitAll()  // Permite páginas públicas y admin categorias página (contenido protegido via JS)
                .requestMatchers(HttpMethod.GET, "/api/productos/**", "/api/categorias/**").permitAll()  // Permite GET a APIs para visualización pública
                .requestMatchers(HttpMethod.POST, "/api/productos/**", "/api/categorias/**").hasRole("ADMIN")  // Protege creación
                .requestMatchers(HttpMethod.PUT, "/api/productos/**", "/api/categorias/**").hasRole("ADMIN")  // Protege actualización
                .requestMatchers(HttpMethod.DELETE, "/api/productos/**", "/api/categorias/**").hasRole("ADMIN")  // Protege eliminación
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/api/carrito/**").hasAnyRole("ADMIN", "USUARIO")  // Protege carrito
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);  // Filtro JWT

        return http.build();
    }
}