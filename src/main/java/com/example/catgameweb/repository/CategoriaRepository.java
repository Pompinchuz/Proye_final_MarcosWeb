package com.example.catgameweb.repository;

import com.example.catgameweb.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
   
    List<Categoria> findByNombre(String nombre);
    
    // Métodos personalizados si necesitas, ej: findByNombre
}