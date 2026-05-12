package com.jesussanchez.kinalapp.controller;

import com.jesussanchez.kinalapp.entity.Producto;
import com.jesussanchez.kinalapp.service.IProductoService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/productos")
public class ProductoController {

    private final IProductoService productoService;

    public ProductoController(IProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    public String listar(Model model, Authentication authentication) {
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        model.addAttribute("productos", productoService.ListarTodos());
        model.addAttribute("isAdmin", isAdmin);
        return "productos/lista-productos";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model, Authentication authentication) {
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        model.addAttribute("producto", new Producto());
        model.addAttribute("titulo", "Registrar Nuevo Producto");
        model.addAttribute("isAdmin", isAdmin);
        return "productos/formulario-producto";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model, Authentication authentication) {
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        // Solo ADMIN puede editar
        if (!isAdmin) {
            return "redirect:/productos?error=No tienes permisos para editar";
        }

        productoService.buscarPorCodigo(id).ifPresent(producto -> {
            model.addAttribute("producto", producto);
            model.addAttribute("titulo", "Editar Producto");
            model.addAttribute("isAdmin", isAdmin);
        });
        return "productos/formulario-producto";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Producto producto) {
        productoService.guardar(producto);
        return "redirect:/productos";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, Authentication authentication) {
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        // Solo ADMIN puede eliminar
        if (!isAdmin) {
            return "redirect:/productos?error=No tienes permisos para eliminar";
        }

        productoService.eliminar(id);
        return "redirect:/productos";
    }
}