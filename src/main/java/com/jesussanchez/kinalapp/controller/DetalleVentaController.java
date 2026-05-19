package com.jesussanchez.kinalapp.controller;

import com.jesussanchez.kinalapp.entity.DetalleVenta;
import com.jesussanchez.kinalapp.service.IDetalleVentaService;
import com.jesussanchez.kinalapp.service.IVentaService;
import com.jesussanchez.kinalapp.service.IProductoService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/detalles")
public class DetalleVentaController {

    private final IDetalleVentaService detalleService;
    private final IVentaService ventaService;
    private final IProductoService productoService;

    public DetalleVentaController(IDetalleVentaService detalleService, IVentaService ventaService, IProductoService productoService) {
        this.detalleService = detalleService;
        this.ventaService = ventaService;
        this.productoService = productoService;
    }

    @GetMapping("/venta/{codigoVenta}")
    public String listarPorVenta(@PathVariable Long codigoVenta, Model model, Authentication authentication) {
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));


        List<DetalleVenta> detalles = detalleService.listarPorVenta(codigoVenta);


        BigDecimal totalVenta = detalles.stream()
                .map(DetalleVenta::getSubtotal)
                .filter(subtotal -> subtotal != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        model.addAttribute("detalles", detalles);
        model.addAttribute("codigoVenta", codigoVenta);
        model.addAttribute("totalVenta", totalVenta);
        model.addAttribute("isAdmin", isAdmin);
        return "detalles/lista-detalles";
    }

    @GetMapping("/nuevo/{codigoVenta}")
    public String mostrarFormulario(@PathVariable Long codigoVenta, Model model, Authentication authentication) {
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        model.addAttribute("detalle", new DetalleVenta());
        model.addAttribute("codigoVenta", codigoVenta);
        model.addAttribute("productos", productoService.ListarTodos());
        model.addAttribute("isAdmin", isAdmin);
        return "detalles/formulario-detalle";
    }

    @PostMapping("/guardar/{codigoVenta}")
    public String guardar(@PathVariable Long codigoVenta, @ModelAttribute DetalleVenta detalle) {
        ventaService.buscarPorCodigo(codigoVenta).ifPresent(detalle::setVenta);
        detalleService.guardar(detalle);
        return "redirect:/detalles/venta/" + codigoVenta;
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model, Authentication authentication) {
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            return "redirect:/detalles/venta/" + id + "?error=No tienes permisos para editar";
        }

        return detalleService.buscarPorCodigo(id)
                .map(detalle -> {
                    model.addAttribute("detalle", detalle);
                    model.addAttribute("productos", productoService.ListarTodos());
                    model.addAttribute("codigoVenta", detalle.getVenta().getCodigoVenta());
                    model.addAttribute("isAdmin", isAdmin);
                    return "detalles/formulario-detalle";
                })
                .orElse("redirect:/ventas");
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, Authentication authentication) {
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            return "redirect:/ventas?error=No tienes permisos para eliminar";
        }

        try {
            Long codigoVenta = detalleService.buscarPorCodigo(id)
                    .map(d -> d.getVenta().getCodigoVenta())
                    .orElse(null);
            detalleService.eliminar(id);
            return "redirect:/detalles/venta/" + codigoVenta;
        } catch (Exception e) {
            return "redirect:/ventas";
        }
    }
}