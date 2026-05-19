package com.jesussanchez.kinalapp.service;

import com.jesussanchez.kinalapp.entity.Cliente;
import com.jesussanchez.kinalapp.entity.Usuario;
import com.jesussanchez.kinalapp.entity.Venta;
import com.jesussanchez.kinalapp.repository.ClienteRepository;
import com.jesussanchez.kinalapp.repository.UsuarioRepository;
import com.jesussanchez.kinalapp.repository.VentaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service

@Transactional
public class VentaService implements IVentaService{

    private final VentaRepository ventaRepository;
    private final ClienteRepository clienteRepository;
    private final UsuarioRepository usuarioRepository;

    public VentaService(VentaRepository ventaRepository, ClienteRepository clienteRepository, UsuarioRepository usuarioRepository) {
        this.ventaRepository = ventaRepository;
        this.clienteRepository = clienteRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override

    @Transactional
    public List<Venta> ListarTodos() {
        return ventaRepository.findAll();
    }

    @Override
    public List<Venta> listarActivos() {
        return ventaRepository.findByEstado(1);
    }

    @Override
    public Venta guardar(Venta venta) {

        Cliente cliente = clienteRepository.findById(venta.getCliente().getDpiCliente())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        Usuario usuario = usuarioRepository.findById(venta.getUsuario().getCodigoUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        venta.setCliente(cliente);
        venta.setUsuario(usuario);

        validarVenta(venta);

        if (venta.getEstado() == 0) {
            venta.setEstado(1);
        }

        return ventaRepository.save(venta);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorCodigo(Long codigo) {
        return ventaRepository.existsById(codigo);

    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Venta> buscarPorCodigo(Long codigo) {
        return ventaRepository.findById(codigo);

    }

    @Override
    public Venta actualizar(Long codigo, Venta venta) {
        if(!ventaRepository.existsById(codigo)){
            throw new RuntimeException("La Venta no se encontro con codigo " + codigo);

        }

        venta.setCodigoVenta(codigo);
        validarVenta(venta);

        return ventaRepository.save(venta);
    }

    @Override
    public void eliminar(Long codigo) {

        if(!ventaRepository.existsById(codigo))
            throw new RuntimeException("La Venta no se encontro con el codigo " +codigo);

        ventaRepository.deleteById(codigo);

    }

    @Override
    @Transactional
    public Venta anular(Long codigo) {
        Venta venta = ventaRepository.findById(codigo)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada con código " + codigo));

        venta.setEstado(0);


        return ventaRepository.save(venta);
    }

    private void validarVenta(Venta venta) {
        if (venta.getFechaVenta() == null) {
            throw new IllegalArgumentException("La fecha es obligatoria.");
        }

        if (venta.getTotal() == null || venta.getTotal().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El total debe ser mayor a cero");
        }

        if (venta.getCliente() == null || venta.getCliente().getDpiCliente() == null || venta.getCliente().getDpiCliente().trim().isEmpty()) {
            throw new IllegalArgumentException("Debe asignar un cliente con un DPI válido.");
        }

        if (venta.getUsuario() == null || venta.getUsuario().getCodigoUsuario() == 0) {
            throw new IllegalArgumentException("El usuario es obligatorio.");
        }
    }
}