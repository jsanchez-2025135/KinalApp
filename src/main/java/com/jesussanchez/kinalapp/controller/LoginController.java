package com.jesussanchez.kinalapp.controller;

import com.jesussanchez.kinalapp.entity.Usuario;
import com.jesussanchez.kinalapp.service.IUsuarioService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    private final IUsuarioService usuarioService;

    public LoginController(IUsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/login")
    public String mostrarLogin(@RequestParam(value = "error", required = false) String error,
                               @RequestParam(value = "logout", required = false) String logout,
                               @RequestParam(value = "registrado", required = false) String registrado,
                               Model model) {
        if (error != null) {
            model.addAttribute("error", "Credenciales inválidas");
        }
        if (logout != null) {
            model.addAttribute("mensaje", "Sesión cerrada exitosamente");
        }
        if (registrado != null) {
            model.addAttribute("mensaje", "Registro exitoso. Inicia sesión");
        }
        return "login";
    }

    @GetMapping("/registro")
    public String mostrarRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registro";
    }

    @PostMapping("/registro")
    public String registrarUsuario(@ModelAttribute Usuario usuario) {
        // NO asignar rol aquí, el Service lo hará automáticamente
        // El primer usuario será ADMIN, los demás USER
        usuario.setEstado(1); // Solo asignamos estado activo
        usuarioService.guardar(usuario);
        return "redirect:/login?registrado=true";
    }

    @GetMapping("/menu")
    public String mostrarMenu(Authentication authentication, Model model) {
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        model.addAttribute("isAdmin", isAdmin);
        return "menu";
    }
}