package com.example.catgameweb.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@Entity
public class Producto {
@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //@NotBlank(message = "El título es obligatorio")
    private String titulo;

    //@NotBlank(message = "La descripción es obligatoria")
    private String descripcion;

    //@NotNull(message = "El precio es obligatorio")
    //@Positive(message = "El precio debe ser positivo")
    private Double precio;

    //@NotNull(message = "La valoración es obligatoria")
    //@Min(value = 0, message = "La valoración debe ser al menos 0")
    //@Max(value = 5, message = "La valoración debe ser como máximo 5")
    private Double valoracion;

    //@NotBlank(message = "La URL de la imagen es obligatoria")
    private String imagenUrl;

    @ManyToOne
    private Categoria categoria;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Double getValoracion() {
        return valoracion;
    }

    public void setValoracion(Double valoracion) {
        this.valoracion = valoracion;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    // Getters y Setters...


    
}