package com.jesussanchez.kinalapp.controller;

import com.jesussanchez.kinalapp.entity.Usuario;
import com.jesussanchez.kinalapp.service.IUsuarioService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    private final IUsuarioService usuarioService;

    public UsuarioController(IUsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public String listar(Model model, Authentication authentication) {
        // Verificar si el usuario es ADMIN
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            return "redirect:/menu?error=Acceso denegado";
        }

        List<Usuario> usuarios = usuarioService.ListarTodos();
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("isAdmin", isAdmin);
        return "usuarios/lista-usuarios";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model, Authentication authentication) {
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            return "redirect:/menu?error=Acceso denegado";
        }

        model.addAttribute("usuario", new Usuario());
        model.addAttribute("titulo", "Nuevo Usuario");
        model.addAttribute("isAdmin", isAdmin);
        return "usuarios/formulario-usuario";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Usuario usuario, Authentication authentication) {
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            return "redirect:/menu?error=Acceso denegado";
        }

        usuarioService.guardar(usuario);
        return "redirect:/usuarios";
    }

    @GetMapping("/editar/{codigo}")
    public String mostrarFormularioEditar(@PathVariable Long codigo, Model model, Authentication authentication) {
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            return "redirect:/menu?error=Acceso denegado";
        }

        return usuarioService.buscarPorCodigo(codigo)
                .map(usuario -> {
                    model.addAttribute("usuario", usuario);
                    model.addAttribute("titulo", "Editar Usuario");
                    model.addAttribute("isAdmin", isAdmin);
                    return "usuarios/formulario-usuario";
                })
                .orElse("redirect:/usuarios");
    }

    @GetMapping("/eliminar/{codigo}")
    public String eliminar(@PathVariable Long codigo, Authentication authentication) {
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            return "redirect:/menu?error=Acceso denegado";
        }

        try {
            usuarioService.eliminar(codigo);
        } catch (Exception e) {
            // Log error
        }
        return "redirect:/usuarios";
    }
}