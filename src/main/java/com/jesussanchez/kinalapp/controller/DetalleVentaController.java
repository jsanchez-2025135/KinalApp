package com.jesussanchez.kinalapp.controller;

import com.jesussanchez.kinalapp.entity.DetalleVenta;
import com.jesussanchez.kinalapp.service.IDetalleVentaService;
import com.jesussanchez.kinalapp.service.IVentaService;
import com.jesussanchez.kinalapp.service.IProductoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    public String listarPorVenta(@PathVariable Long codigoVenta, Model model) {
        model.addAttribute("detalles", detalleService.listarPorVenta(codigoVenta));
        model.addAttribute("codigoVenta", codigoVenta);
        return "detalles/lista-detalles";
    }

    @GetMapping("/nuevo/{codigoVenta}")
    public String mostrarFormulario(@PathVariable Long codigoVenta, Model model) {
        model.addAttribute("detalle", new DetalleVenta());
        model.addAttribute("codigoVenta", codigoVenta);
        model.addAttribute("productos", productoService.ListarTodos());
        return "detalles/formulario-detalle";
    }

    @PostMapping("/guardar/{codigoVenta}")
    public String guardar(@PathVariable Long codigoVenta, @ModelAttribute DetalleVenta detalle) {
        ventaService.buscarPorCodigo(codigoVenta).ifPresent(detalle::setVenta);
        detalleService.guardar(detalle);
        return "redirect:/detalles/venta/" + codigoVenta;
    }
}