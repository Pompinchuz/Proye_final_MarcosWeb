package com.example.catgameweb.controller;

import com.example.catgameweb.model.Pedido;
import com.example.catgameweb.service.PedidoService;
import com.example.catgameweb.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping
    public ResponseEntity<Pedido> finalizarCompra(@RequestBody String datosPago, Principal principal) {
        String email = principal.getName();
        Long usuarioId = usuarioRepository.findByEmail(email).get().getId();
        Pedido pedido = pedidoService.finalizarCompra(usuarioId, datosPago);
        return ResponseEntity.ok(pedido);
    }
}