package com.example.catgameweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Model model) {
        // Agrega lógica para mostrar bienvenida si autenticado
        return "index";  // index.html en templates
    }

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("/registro")
    public String registroPage() {
        return "auth/registro";
    }

    @GetMapping("/productos")
    public String productosPage() {
        return "products/list";
    }

    @GetMapping("/admin/gestionar-productos")
    public String gestionarProductos() {
        return "products/manage";
    }

    @GetMapping("/nosotros")
    public String nosotrosPage() {
        return "about/us";
    }

    @GetMapping("/admin/categorias")
    public String gestionarCategorias() {
        return "categories/gestionar-categorias";  // Asegúrate de crear la carpeta templates/categories
    }

    @GetMapping("/carrito")
    public String carritoPage() {
        return "carrito";  // Crea templates/carrito.html
    }


    
    @GetMapping("/admin/reportes/ventas") 
    public String ventas() { 
        return "reportes/ventas";
     }
}