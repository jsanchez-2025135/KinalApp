package com.jesussanchez.kinalapp.controller;

import com.jesussanchez.kinalapp.entity.Cliente;
import com.jesussanchez.kinalapp.service.IClienteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/clientes")
public class ClienteController {

    private final IClienteService clienteService;

    public ClienteController(IClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public String listar(Model model) {
        List<Cliente> clientes = clienteService.ListarTodos();
        model.addAttribute("clientes", clientes);
        return "clientes/lista-clientes"; // Retorna el HTML
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("cliente", new Cliente());
        model.addAttribute("titulo", "Nuevo Cliente");
        return "clientes/formulario-cliente";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Cliente cliente) {
        clienteService.guardar(cliente);
        return "redirect:/clientes";
    }

    @GetMapping("/editar/{dpi}")
    public String mostrarFormularioEditar(@PathVariable String dpi, Model model) {
        return clienteService.buscarPorDPI(dpi)
                .map(cliente -> {
                    model.addAttribute("cliente", cliente);
                    model.addAttribute("titulo", "Editar Cliente");
                    return "clientes/formulario-cliente";
                })
                .orElse("redirect:/clientes");
    }

    @GetMapping("/eliminar/{dpi}")
    public String eliminar(@PathVariable String dpi) {
        try {
            clienteService.eliminar(dpi);
        } catch (Exception e) {

        }
        return "redirect:/clientes";
    }
}