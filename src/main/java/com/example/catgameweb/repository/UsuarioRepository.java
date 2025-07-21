package com.example.catgameweb.repository;



import com.example.catgameweb.model.Role;
import com.example.catgameweb.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    boolean existsByNombre(String nombre);
    boolean existsByEmail(String email);
    List<Usuario> findByRol(Role rol);  // Método adicional para listar por rol (útil para admin)
}