package com.jesussanchez.kinalapp.controller;

import com.jesussanchez.kinalapp.entity.Venta;
import com.jesussanchez.kinalapp.service.IVentaService;
import com.jesussanchez.kinalapp.service.IClienteService;
import com.jesussanchez.kinalapp.service.IUsuarioService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/ventas")
public class VentaController {

    private final IVentaService ventaService;
    private final IClienteService clienteService;
    private final IUsuarioService usuarioService;

    public VentaController(IVentaService ventaService, IClienteService clienteService, IUsuarioService usuarioService) {
        this.ventaService = ventaService;
        this.clienteService = clienteService;
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public String listar(Model model, Authentication authentication) {
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        List<Venta> ventas = ventaService.ListarTodos();
        model.addAttribute("ventas", ventas);
        model.addAttribute("isAdmin", isAdmin);
        return "ventas/lista-ventas";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model, Authentication authentication) {
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        model.addAttribute("venta", new Venta());
        model.addAttribute("clientes", clienteService.ListarTodos());
        model.addAttribute("usuarios", usuarioService.ListarTodos());
        model.addAttribute("titulo", "Registrar Nueva Venta");
        model.addAttribute("isAdmin", isAdmin);
        return "ventas/formulario-venta";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Venta venta) {
        Venta ventaGuardada = ventaService.guardar(venta);
        return "redirect:/detalles/venta/" + ventaGuardada.getCodigoVenta();
    }

    @GetMapping("/detalle/{id}")
    public String verDetalle(@PathVariable Long id, Model model) {
        return ventaService.buscarPorCodigo(id)
                .map(venta -> {
                    model.addAttribute("venta", venta);
                    return "redirect:/detalles/venta/" + id;
                })
                .orElse("redirect:/ventas");
    }

}