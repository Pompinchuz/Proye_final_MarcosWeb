package com.example.catgameweb.repository;



import com.example.catgameweb.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByCategoriaId(Long categoriaId);  // Ejemplo de método personalizado
}