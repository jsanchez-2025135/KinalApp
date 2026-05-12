package com.jesussanchez.kinalapp.service;

import com.jesussanchez.kinalapp.entity.Cliente;
import com.jesussanchez.kinalapp.entity.Usuario;
import com.jesussanchez.kinalapp.repository.ClienteRepository;
import com.jesussanchez.kinalapp.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service

@Transactional
public class UsuarioService implements IUsuarioService{

    private final UsuarioRepository usuarioRepository;


    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override

    @Transactional
    public List<Usuario> ListarTodos() {
        return usuarioRepository.findAll();
    }

    @Override
    public List<Usuario> listarActivos() {
        return usuarioRepository.findByEstado(1);
    }

    @Override
    public Usuario guardar(Usuario usuario) {

        validarUsuario(usuario);
        if (usuario.getEstado()==0)
            usuario.setEstado(1);
        return usuarioRepository.save(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorCodigo(Long codigo) {
        return usuarioRepository.findById(codigo);

    }

    @Override
    public Usuario actualizar(Long codigo, Usuario usuario) {
        if(!usuarioRepository.existsById(codigo)){
            throw new RuntimeException("USuario no se encontro con codigo " + codigo);

        }

        usuario.setCodigoUsuario(codigo);
        validarUsuario(usuario);

        return usuarioRepository.save(usuario);
    }

    @Override
    public void eliminar(Long codigo) {

        if(!usuarioRepository.existsById(codigo))
            throw new RuntimeException("El usuario no se encontro con el codigo " +codigo);

        usuarioRepository.deleteById(codigo);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorCodigo(Long codigo) {
        return usuarioRepository.existsById(codigo);

    }

    private void validarUsuario(Usuario usuario) {

        if (usuario.getUsername() == null || usuario.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es un dato obligatorio");
        }

        if (usuario.getPassword() == null || usuario.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("La contraseña es obligatoria.");
        }

        if (usuario.getEmail() == null || usuario.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("El correo electrónico es obligatorio.");
        }

        if (usuario.getRol() == null || usuario.getRol().trim().isEmpty()) {
            throw new IllegalArgumentException("El rol del usuario  es obligatorio.");
        }

        if (usuario.getEstado() < 0 || usuario.getEstado() > 1) {
            throw new IllegalArgumentException("El estado debe ser 1 o 0 .");
        }
    }
}
