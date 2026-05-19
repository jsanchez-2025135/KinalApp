package com.jesussanchez.kinalapp.service;

import com.jesussanchez.kinalapp.entity.DetalleVenta;
import com.jesussanchez.kinalapp.entity.Producto;
import com.jesussanchez.kinalapp.entity.Venta;
import com.jesussanchez.kinalapp.repository.DetalleVentaRepository;
import com.jesussanchez.kinalapp.repository.ProductoRepository;
import com.jesussanchez.kinalapp.repository.VentaRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Validated
public class DetalleVentaService implements IDetalleVentaService {

    private final DetalleVentaRepository detalleRepository;
    private final ProductoRepository productoRepository;
    private final VentaRepository ventaRepository;

    public DetalleVentaService(DetalleVentaRepository detalleRepository, ProductoRepository productoRepository, VentaRepository ventaRepository) {
        this.detalleRepository = detalleRepository;
        this.productoRepository = productoRepository;
        this.ventaRepository = ventaRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<DetalleVenta> listarTodos() {
        return detalleRepository.findAll();
    }

    @Override
    @Transactional
    public DetalleVenta guardar(@Valid DetalleVenta detalle) {
        Producto producto = productoRepository.findById(detalle.getProducto().getCodigoProducto())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        if (producto.getStock() < detalle.getCantidad()) {
            throw new RuntimeException("Stock insuficiente. Stock actual: " + producto.getStock());
        }
        Venta venta = ventaRepository.findById(detalle.getVenta().getCodigoVenta())
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));
        detalle.setPrecioUnitario(producto.getPrecio());
        BigDecimal subtotalCalculado = producto.getPrecio().multiply(new BigDecimal(detalle.getCantidad()));
        detalle.setSubtotal(subtotalCalculado);
        detalle.setProducto(producto);
        detalle.setVenta(venta);
        producto.setStock(producto.getStock() - detalle.getCantidad());
        productoRepository.save(producto);
        DetalleVenta detalleGuardado = detalleRepository.save(detalle);
        actualizarTotalVenta(venta.getCodigoVenta());
        return detalleGuardado;
    }

    @Override
    @Transactional(readOnly = true)
    public List<DetalleVenta> listarPorVenta(Long codigoVenta) {
        return detalleRepository.findByVentaCodigoVenta(codigoVenta);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DetalleVenta> buscarPorCodigo(Long codigo) {
        return detalleRepository.findById(codigo);
    }

    @Override
    public boolean existePorCodigo(Long codigo) {
        return detalleRepository.existsById(codigo);
    }

    @Override
    @Transactional
    public void eliminar(Long codigo) {
        DetalleVenta detalle = detalleRepository.findById(codigo)
                .orElseThrow(() -> new RuntimeException("Detalle no encontrado"));
        Long codigoVenta = detalle.getVenta().getCodigoVenta();
        Producto producto = detalle.getProducto();
        producto.setStock(producto.getStock() + detalle.getCantidad());
        productoRepository.save(producto);
        detalleRepository.deleteById(codigo);
        actualizarTotalVenta(codigoVenta);
    }

    @Override
    public DetalleVenta actualizar(Long codigo, DetalleVenta detalleVenta) {
        return null;
    }

    private void actualizarTotalVenta(Long codigoVenta) {
        List<DetalleVenta> detalles = detalleRepository.findByVentaCodigoVenta(codigoVenta);
        BigDecimal total = detalles.stream()
                .map(DetalleVenta::getSubtotal)
                .filter(subtotal -> subtotal != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        Venta venta = ventaRepository.findById(codigoVenta)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada para actualizar total"));
        venta.setTotal(total);
        ventaRepository.save(venta);
    }
}