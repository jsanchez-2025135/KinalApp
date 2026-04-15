package com.jesussanchez.kinalapp.service;

import com.jesussanchez.kinalapp.entity.Producto;
import com.jesussanchez.kinalapp.entity.Usuario;

import java.util.List;
import java.util.Optional;

public interface IProductoService {

    List<Producto> ListarTodos();

    List<Producto> listarActivos();

    Producto guardar(Producto producto);

    Optional<Producto> buscarPorCodigo(Long codigo);

    Producto actualizar(Long codigo, Producto producto);

    void eliminar(Long codigo);

    boolean existePorCodigo(Long codigo);
}
