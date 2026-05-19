package com.jesussanchez.kinalapp.service;

import com.jesussanchez.kinalapp.entity.Cliente;
import com.jesussanchez.kinalapp.repository.ClienteRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Validated
public class ClienteService implements IClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> ListarTodos() {
        return clienteRepository.findAll();
    }

    @Override
    public List<Cliente> listarActivos() {
        return clienteRepository.findByEstado(1);
    }

    @Override
    public Cliente guardar(@Valid Cliente cliente) {
        if (cliente.getEstado() == 0) {
            cliente.setEstado(1);
        }
        return clienteRepository.save(cliente);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Cliente> buscarPorDPI(String dpi) {
        return clienteRepository.findById(dpi);
    }

    @Override
    public Cliente actualizar(String dpi, @Valid Cliente cliente) {
        if (!clienteRepository.existsById(dpi)) {
            throw new RuntimeException("Cliente no se encontro con DPI " + dpi);
        }
        cliente.setDpiCliente(dpi);
        return clienteRepository.save(cliente);
    }

    @Override
    public void eliminar(String dpi) {
        if (!clienteRepository.existsById(dpi)) {
            throw new RuntimeException("El cliente no se encontro con el DPI " + dpi);
        }
        clienteRepository.deleteById(dpi);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorDPI(String dpi) {
        return clienteRepository.existsById(dpi);
    }
}