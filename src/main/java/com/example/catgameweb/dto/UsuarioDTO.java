package com.example.catgameweb.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
public class UsuarioDTO {
    private Long id;
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;
    @NotBlank(message = "El email es obligatorio")
    private String email;
    @NotBlank(message = "Contraseña obligatoria")
    
    private String password;  // Encriptada
    private Long rolId;  // Para referencia a rol
}
