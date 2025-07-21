package com.example.catgameweb.dto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;

public class CategoriaDTO {
    
       @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    
    private Long id;
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;  

    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
