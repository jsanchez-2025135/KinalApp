package com.jesussanchez.kinalapp.service;

import com.jesussanchez.kinalapp.entity.DetalleVenta;
import java.util.List;
import java.util.Optional;

public interface IDetalleVentaService {

    List<DetalleVenta> listarTodos();

    List<DetalleVenta> listarPorVenta(Long codigoVenta);

    DetalleVenta guardar(DetalleVenta detalleVenta);

    Optional<DetalleVenta> buscarPorCodigo(Long codigo);

    DetalleVenta actualizar(Long codigo, DetalleVenta detalleVenta);

    void eliminar(Long codigo);

    boolean existePorCodigo(Long codigo);
}