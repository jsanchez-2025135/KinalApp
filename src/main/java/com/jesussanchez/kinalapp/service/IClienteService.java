package com.jesussanchez.kinalapp.service;

import com.jesussanchez.kinalapp.entity.Cliente;

import java.util.List;
import java.util.Optional;

public interface IClienteService {

    List<Cliente> ListarTodos();

    List<Cliente> listarActivos();

    Cliente guardar(Cliente cliente);

    Optional<Cliente>buscarPorDPI(String dpi);

    Cliente actualizar(String dpi, Cliente cliente);

    void eliminar(String dpi);

    boolean existePorDPI(String dpi);



}
