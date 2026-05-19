package com.jesussanchez.kinalapp.controller;

import com.jesussanchez.kinalapp.entity.Cliente;
import com.jesussanchez.kinalapp.service.IClienteService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/clientes")
public class ClienteController {

    private final IClienteService clienteService;

    public ClienteController(IClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public String listar(Model model, Authentication authentication) {
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        List<Cliente> clientes = clienteService.ListarTodos();
        model.addAttribute("clientes", clientes);
        model.addAttribute("isAdmin", isAdmin);
        return "clientes/lista-clientes";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model, Authentication authentication) {
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        model.addAttribute("cliente", new Cliente());
        model.addAttribute("titulo", "Nuevo Cliente");
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("canEdit", true); // Todos pueden crear
        return "clientes/formulario-cliente";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Cliente cliente) {
        clienteService.guardar(cliente);
        return "redirect:/clientes";
    }

    @GetMapping("/editar/{dpi}")
    public String mostrarFormularioEditar(@PathVariable String dpi, Model model, Authentication authentication) {
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            return "redirect:/clientes?error=No tienes permisos para editar";
        }

        return clienteService.buscarPorDPI(dpi)
                .map(cliente -> {
                    model.addAttribute("cliente", cliente);
                    model.addAttribute("titulo", "Editar Cliente");
                    model.addAttribute("isAdmin", isAdmin);
                    model.addAttribute("canEdit", true);
                    return "clientes/formulario-cliente";
                })
                .orElse("redirect:/clientes");
    }

    @GetMapping("/eliminar/{dpi}")
    public String eliminar(@PathVariable String dpi, Authentication authentication, RedirectAttributes redirectAttributes) {
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            return "redirect:/clientes?error=No tienes permisos para eliminar";
        }

        try {
            clienteService.eliminar(dpi);
            redirectAttributes.addFlashAttribute("mensaje", "Cliente eliminado correctamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "warning");
        }
        return "redirect:/clientes";
    }

    @GetMapping("/cambiar-estado/{dpi}")
    public String cambiarEstado(@PathVariable String dpi, Authentication authentication, RedirectAttributes redirectAttributes) {
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            return "redirect:/clientes?error=No tienes permisos para cambiar el estado";
        }

        try {
            Cliente cliente = clienteService.cambiarEstado(dpi);
            String nuevoEstado = cliente.getEstado() == 1 ? "activado" : "desactivado";
            redirectAttributes.addFlashAttribute("mensaje", "Cliente " + nuevoEstado + " correctamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "danger");
        }
        return "redirect:/clientes";
    }
}