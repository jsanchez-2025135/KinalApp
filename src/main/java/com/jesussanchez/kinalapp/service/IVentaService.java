package com.jesussanchez.kinalapp.service;

import com.jesussanchez.kinalapp.entity.Venta;
import java.util.List;
import java.util.Optional;

public interface IVentaService {

    List<Venta> ListarTodos();

    List<Venta> listarActivos();

    Venta guardar(Venta venta);

    Optional<Venta> buscarPorCodigo(Long codigo);

    Venta actualizar(Long codigo, Venta venta);

    void eliminar(Long codigo);

    Venta anular(Long codigo);

    boolean existePorCodigo(Long codigo);
}