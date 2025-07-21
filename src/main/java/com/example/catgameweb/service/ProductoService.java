package com.example.catgameweb.service;

import com.example.catgameweb.model.Producto;
import com.example.catgameweb.repository.ProductoRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Validated  // Habilita validation en service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }

    // Nuevo método para filtrar
    public List<Producto> filtrar(String search, Long categoriaId) {
        List<Producto> productos = productoRepository.findAll();
        if (search != null && !search.isEmpty()) {
            productos = productos.stream()
                    .filter(p -> p.getTitulo().toLowerCase().contains(search.toLowerCase()))
                    .collect(Collectors.toList());
        }
        if (categoriaId != null) {
            productos = productos.stream()
                    .filter(p -> p.getCategoria() != null && p.getCategoria().getId().equals(categoriaId))
                    .collect(Collectors.toList());
        }
        return productos;
    }

    public Producto crear(@Valid Producto producto) {
        // Verifica si ya existe un producto con el mismo título
        Optional<Producto> existingProduct = productoRepository.findAll().stream()
                .filter(p -> p.getTitulo().equalsIgnoreCase(producto.getTitulo()))
                .findFirst();
        if (existingProduct.isPresent()) {
            throw new IllegalStateException("Ya existe un producto con el título: " + producto.getTitulo());
        }
        return productoRepository.save(producto);
    }

    public Optional<Producto> obtenerPorId(Long id) {
        return productoRepository.findById(id);
    }

    public Producto actualizar(Long id, @Valid Producto productoActualizado) {
        Optional<Producto> productoExistente = productoRepository.findById(id);
        if (productoExistente.isPresent()) {
            Producto producto = productoExistente.get();
            producto.setTitulo(productoActualizado.getTitulo());
            producto.setDescripcion(productoActualizado.getDescripcion());
            producto.setPrecio(productoActualizado.getPrecio());
            producto.setValoracion(productoActualizado.getValoracion());
            producto.setImagenUrl(productoActualizado.getImagenUrl());
            producto.setCategoria(productoActualizado.getCategoria());
            return productoRepository.save(producto);
        } else {
            throw new RuntimeException("Producto no encontrado");
        }
    }

    public void eliminar(Long id) {
        productoRepository.deleteById(id);
    }


}