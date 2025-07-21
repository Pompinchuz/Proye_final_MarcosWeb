package com.example.catgameweb.service;

import com.example.catgameweb.model.Carrito;
import com.example.catgameweb.model.CarritoItem;
import com.example.catgameweb.model.Pedido;
import com.example.catgameweb.model.PedidoProducto;
import com.example.catgameweb.model.Usuario;
import com.example.catgameweb.repository.PedidoRepository;
import com.example.catgameweb.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CarritoService carritoService;

    public Pedido finalizarCompra(Long usuarioId, String datosPago) {
        // Valida datosPago (ej: tarjeta, etc.) - Simula por ahora
        if (datosPago == null || datosPago.isEmpty()) {
            throw new RuntimeException("Datos de pago inválidos");
        }

        Usuario usuario = usuarioRepository.findById(usuarioId).orElseThrow();
        Carrito carrito = carritoService.getCarrito(usuarioId);

        if (carrito.getItems().isEmpty()) {
            throw new RuntimeException("Carrito vacío");  // Mantén o cambia a return null / mensaje
        }

        Pedido pedido = new Pedido();
        pedido.setUsuario(usuario);
        pedido.setTotal(carrito.getTotal());
        pedido.setEstado("PAGADO");  // Asume pago exitoso

        List<PedidoProducto> pedidoProductos = new ArrayList<>();
        for (CarritoItem item : carrito.getItems()) {
            PedidoProducto pp = new PedidoProducto();
            pp.setPedido(pedido);
            pp.setProducto(item.getProducto());
            pp.setCantidad(item.getCantidad());
            pp.setPrecioUnitario(item.getProducto().getPrecio());
            pedidoProductos.add(pp);
        }
        pedido.setProductos(pedidoProductos);

        // Vaciar carrito después de pedido
        carrito.getItems().clear();
        carrito.setTotal(0.0);
        carritoService.save(carrito);

        return pedidoRepository.save(pedido);
    }
}