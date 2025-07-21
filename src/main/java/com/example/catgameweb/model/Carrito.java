package com.example.catgameweb.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class Carrito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Usuario usuario;

    @OneToMany(mappedBy = "carrito", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CarritoItem> items = new ArrayList<>();

    private Double total = 0.0;

    public void agregarProducto(Producto producto, int cantidad) {
        // Buscar si ya existe el item
        for (CarritoItem item : items) {
            if (item.getProducto().getId().equals(producto.getId())) {
                item.setCantidad(item.getCantidad() + cantidad);
                total += producto.getPrecio() * cantidad;
                return;
            }
        }
        // Nuevo item
        CarritoItem newItem = new CarritoItem();
        newItem.setCarrito(this);
        newItem.setProducto(producto);
        newItem.setCantidad(cantidad);
        items.add(newItem);
        total += producto.getPrecio() * cantidad;
    }

    public void removerProducto(Producto producto, int cantidad) {
        for (CarritoItem item : items) {
            if (item.getProducto().getId().equals(producto.getId())) {
                if (item.getCantidad() > cantidad) {
                    item.setCantidad(item.getCantidad() - cantidad);
                    total -= producto.getPrecio() * cantidad;
                } else {
                    total -= producto.getPrecio() * item.getCantidad();
                    items.remove(item);
                }
                return;
            }
        }
    }
}