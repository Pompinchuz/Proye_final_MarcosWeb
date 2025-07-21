package com.example.catgameweb.service;

import com.example.catgameweb.model.Role;
import com.example.catgameweb.model.Usuario;
import com.example.catgameweb.repository.RoleRepository;
import com.example.catgameweb.repository.UsuarioRepository;
import com.example.catgameweb.security.JwtUtil;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public String registrar(Usuario usuario) {
        // Validaciones
        if (usuario.getNombre().equals(usuario.getEmail())) {
            throw new RuntimeException("Nombre y email no pueden ser iguales");
        }
        if (usuarioRepository.existsByEmail(usuario.getEmail()) || usuarioRepository.existsByNombre(usuario.getNombre())) {
            throw new RuntimeException("Usuario o email ya existe");
        }

        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        Role rolUsuario = roleRepository.findByNombre("USUARIO").orElseThrow();
        usuario.setRol(rolUsuario);
        usuarioRepository.save(usuario);

        // Generar JWT
        org.springframework.security.core.userdetails.User userDetails = new org.springframework.security.core.userdetails.User(
                usuario.getEmail(), usuario.getPassword(), Collections.emptyList());
        return jwtUtil.generateToken(userDetails);
    }

    public String login(String email, String password) {
        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow();
        if (!passwordEncoder.matches(password, usuario.getPassword())) {
            throw new RuntimeException("Credenciales inválidas");
        }

        org.springframework.security.core.userdetails.User userDetails = new org.springframework.security.core.userdetails.User(
                usuario.getEmail(), usuario.getPassword(), Collections.singleton(new SimpleGrantedAuthority("ROLE_" + usuario.getRol().getNombre())));
        return jwtUtil.generateToken(userDetails);
    }

    // Método nuevo: listarTodos (usa el repositorio)
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }
}