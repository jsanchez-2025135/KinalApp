package com.jesussanchez.kinalapp.service;

import com.jesussanchez.kinalapp.entity.Producto;
import com.jesussanchez.kinalapp.repository.ProductoRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Validated
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
    public Producto guardar(@Valid Producto producto) {
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
    public Producto actualizar(Long codigo, @Valid Producto producto) {
        if (!productoRepository.existsById(codigo)) {
            throw new RuntimeException("El producto con código " + codigo + " no existe.");
        }
        producto.setCodigoProducto(codigo);
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
}