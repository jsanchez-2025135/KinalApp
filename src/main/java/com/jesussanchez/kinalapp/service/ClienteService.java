package com.jesussanchez.kinalapp.service;

import com.jesussanchez.kinalapp.entity.Cliente;
import com.jesussanchez.kinalapp.entity.Venta;
import com.jesussanchez.kinalapp.repository.ClienteRepository;
import com.jesussanchez.kinalapp.repository.VentaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ClienteService implements IClienteService {

    private final ClienteRepository clienteRepository;
    private final VentaRepository ventaRepository;

    public ClienteService(ClienteRepository clienteRepository, VentaRepository ventaRepository) {
        this.clienteRepository = clienteRepository;
        this.ventaRepository = ventaRepository;
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
    public Cliente guardar(Cliente cliente) {
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
    public Cliente actualizar(String dpi, Cliente cliente) {
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

        List<Venta> ventasCliente = ventaRepository.findByClienteDpiCliente(dpi);

        if (!ventasCliente.isEmpty()) {

            Cliente cliente = clienteRepository.findById(dpi).get();
            cliente.setEstado(0);
            clienteRepository.save(cliente);
            throw new RuntimeException("El cliente tiene ventas asociadas. No se puede eliminar, solo se ha desactivado.");
        }

        clienteRepository.deleteById(dpi);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorDPI(String dpi) {
        return clienteRepository.existsById(dpi);
    }

    @Override
    public Cliente cambiarEstado(String dpi) {
        Cliente cliente = clienteRepository.findById(dpi)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con DPI: " + dpi));

        if (cliente.getEstado() == 1) {
            cliente.setEstado(0);
        } else {
            cliente.setEstado(1);
        }

        return clienteRepository.save(cliente);
    }
}