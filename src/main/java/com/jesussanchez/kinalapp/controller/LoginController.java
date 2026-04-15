package com.jesussanchez.kinalapp.controller;

import com.jesussanchez.kinalapp.entity.Usuario;
import com.jesussanchez.kinalapp.service.IUsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class LoginController {

    private final IUsuarioService usuarioService;

    public LoginController(IUsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // Mostrar el formulario de Login
    @GetMapping("/")
    public String mostrarLogin() {
        return "login";
    }

    // Procesar el Login
    @PostMapping("/login")
    public String procesarLogin(@RequestParam String email,
                                @RequestParam String password,
                                HttpSession session,
                                Model model) {

        Optional<Usuario> usuarioOpt = usuarioService.ListarTodos().stream()
                .filter(u -> u.getEmail().equals(email) && u.getPassword().equals(password))
                .findFirst();

        if (usuarioOpt.isPresent()) {
            session.setAttribute("usuarioLogueado", usuarioOpt.get());
            return "redirect:/menu";
        } else {
            model.addAttribute("error", "Credenciales incorrectas o usuario no encontrado");
            return "login";
        }
    }

    @GetMapping("/registro")
    public String mostrarRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registro";
    }

    @PostMapping("/registro")
    public String registrarUsuario(@ModelAttribute Usuario usuario) {
        usuario.setEstado(1); // Usuario activo por defecto
        usuario.setRol("USER"); // Rol por defecto
        usuarioService.guardar(usuario);
        return "redirect:/?registrado=true";
    }

    @GetMapping("/menu")
    public String mostrarMenu(HttpSession session) {
        if (session.getAttribute("usuarioLogueado") == null) {
            return "redirect:/";
        }
        return "menu";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}