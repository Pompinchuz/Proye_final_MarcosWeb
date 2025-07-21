package com.example.catgameweb.controller;

import com.example.catgameweb.model.Carrito;
import com.example.catgameweb.model.Usuario;
import com.example.catgameweb.repository.UsuarioRepository;
import com.example.catgameweb.service.CarritoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/api/carrito")
public class CarritoController {

    @Autowired
    private CarritoService carritoService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping("/{productoId}")
    public ResponseEntity<Carrito> agregarAlCarrito(@PathVariable Long productoId, @RequestParam(defaultValue = "1") int cantidad, Principal principal) {
        String email = principal.getName();
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.badRequest().build();  // O maneja error
        }
        Long usuarioId = usuarioOpt.get().getId();
        Carrito carrito = carritoService.agregarProducto(usuarioId, productoId, cantidad);
        return ResponseEntity.ok(carrito);
    }

    @GetMapping
    public ResponseEntity<Carrito> getCarrito(Principal principal) {
        String email = principal.getName();
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.badRequest().build();  // O maneja error
        }
        Long usuarioId = usuarioOpt.get().getId();
        Carrito carrito = carritoService.getCarrito(usuarioId);
        return ResponseEntity.ok(carrito);
    }

    // Añade DELETE para remover, etc.


    @DeleteMapping("/vaciar")
    public ResponseEntity<Void> vaciarCarrito(Principal principal) {
        String email = principal.getName();
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        Long usuarioId = usuarioOpt.get().getId();
        Carrito carrito = carritoService.getCarrito(usuarioId);
        carrito.getItems().clear();
        carrito.setTotal(0.0);
        carritoService.save(carrito);
        return ResponseEntity.ok().build();
    }
}