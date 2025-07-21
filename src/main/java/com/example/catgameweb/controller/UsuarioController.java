package com.example.catgameweb.controller;

import com.example.catgameweb.model.Usuario;
import com.example.catgameweb.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/me")
    public ResponseEntity<Usuario> getCurrentUser(Principal principal) {
        // Usa principal o SecurityContext para obtener user
        Usuario usuario = usuarioRepository.findByEmail(principal.getName()).orElseThrow();
        return ResponseEntity.ok(usuario);
    }
}