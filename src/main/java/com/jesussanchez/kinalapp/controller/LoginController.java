package com.jesussanchez.kinalapp.controller;

import com.jesussanchez.kinalapp.entity.Usuario;
import com.jesussanchez.kinalapp.service.IUsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

    private final IUsuarioService usuarioService;

    public LoginController(IUsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/")
    public String mostrarLogin() {
        return "login";
    }

    @GetMapping("/registro")
    public String mostrarRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registro";
    }

    @PostMapping("/registro")
    public String registrarUsuario(@ModelAttribute Usuario usuario) {
        usuario.setRol("USER"); // Asignar rol por defecto
        usuarioService.guardar(usuario);
        return "redirect:/?registrado=true";
    }

    @GetMapping("/menu")
    public String mostrarMenu() {
        return "menu";
    }

}