package com.example.catgameweb.controller;

import com.example.catgameweb.model.Usuario;
import com.example.catgameweb.service.UsuarioService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/registro")
    public ResponseEntity<String> registro(@RequestBody Usuario usuario, HttpServletResponse response) {
        log.info("Request de registro recibida para email: {}", usuario.getEmail());
        try {
            String token = usuarioService.registrar(usuario);
            addJwtCookie(response, token);
            log.info("Registro exitoso para: {}", usuario.getEmail());
            return ResponseEntity.ok(token);
        } catch (Exception e) {
            log.error("Error en registro: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Usuario usuario, HttpServletResponse response) {
        log.info("Request de login recibida para email: {}", usuario.getEmail());
        try {
            String token = usuarioService.login(usuario.getEmail(), usuario.getPassword());
            addJwtCookie(response, token);
            log.info("Login exitoso para: {}", usuario.getEmail());
            return ResponseEntity.ok(token);
        } catch (Exception e) {
            log.error("Error en login: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private void addJwtCookie(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie("jwt", token);
        cookie.setHttpOnly(true);  // Seguro contra JS access
        cookie.setSecure(false);  // Cambia a true en prod con HTTPS
        cookie.setPath("/");
        cookie.setMaxAge(3600);  
        response.addCookie(cookie);
    }
}