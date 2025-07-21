package com.example.catgameweb.repository;



import com.example.catgameweb.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByNombre(String nombre);  // Método personalizado para buscar por nombre (ej: "ADMIN")
}