package com.jesussanchez.kinalapp.service;

import com.jesussanchez.kinalapp.entity.Producto;
import com.jesussanchez.kinalapp.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductoService implements IProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Producto> ListarTodos() {
        return productoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Producto> listarActivos() {
        return productoRepository.findByEstado(1);
    }

    @Override
    public Producto guardar(Producto producto) {
        validarProducto(producto);
        if (producto.getEstado() == 0) {
            producto.setEstado(1);
        }
        return productoRepository.save(producto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Producto> buscarPorCodigo(Long codigo) {
        return productoRepository.findById(codigo);
    }

    @Override
    public Producto actualizar(Long codigo, Producto producto) {
        if (!productoRepository.existsById(codigo)) {
            throw new RuntimeException("El producto con código " + codigo + " no existe.");
        }

        producto.setCodigoProducto(codigo);
        validarProducto(producto);
        return productoRepository.save(producto);
    }

    @Override
    public void eliminar(Long codigo) {
        if (!productoRepository.existsById(codigo)) {
            throw new RuntimeException("No se puede eliminar, el producto no existe.");
        }
        productoRepository.deleteById(codigo);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorCodigo(Long codigo) {
        return productoRepository.existsById(codigo);
    }

    private void validarProducto(Producto producto) {
        if (producto.getNombreProducto() == null || producto.getNombreProducto().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto es obligatorio.");
        }

        if (producto.getPrecio() == null || producto.getPrecio().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a cero.");
        }

        if (producto.getStock() < 0) {
            throw new IllegalArgumentException("El stock no puede ser un número negativo.");
        }
    }
}