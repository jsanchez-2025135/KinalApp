package com.jesussanchez.kinalapp.service;

import com.jesussanchez.kinalapp.entity.Usuario;
import com.jesussanchez.kinalapp.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UsuarioService implements IUsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> ListarTodos() {
        return usuarioRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> listarActivos() {
        return usuarioRepository.findByEstado(1);
    }

    @Override
    public Usuario guardar(Usuario usuario) {
        validarUsuario(usuario);

        // Codificar la contraseña
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

        // IMPORTANTE: Verificar si es el primer usuario
        long totalUsuarios = usuarioRepository.count();
        System.out.println("Total de usuarios en la BD: " + totalUsuarios);

        if (totalUsuarios == 0) {
            // El primer usuario es ADMIN
            usuario.setRol("ADMIN");
            System.out.println("Primer usuario registrado como ADMIN");
        } else {
            // Los demás son USER
            if (usuario.getRol() == null || usuario.getRol().isEmpty()) {
                usuario.setRol("USER");
            }
            System.out.println("Nuevo usuario registrado como: " + usuario.getRol());
        }

        // Asegurar que el estado sea activo por defecto
        if (usuario.getEstado() == 0) {
            usuario.setEstado(1);
        }

        System.out.println("Guardando usuario: " + usuario.getEmail() + " con rol: " + usuario.getRol());

        return usuarioRepository.save(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorCodigo(Long codigo) {
        return usuarioRepository.findById(codigo);
    }

    @Override
    public Usuario actualizar(Long codigo, Usuario usuario) {
        if (!usuarioRepository.existsById(codigo)) {
            throw new RuntimeException("Usuario no encontrado");
        }
        usuario.setCodigoUsuario(codigo);

        // Solo codificar si la contraseña no está ya codificada
        if (!usuario.getPassword().startsWith("$2a$")) {
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        }

        validarUsuario(usuario);
        return usuarioRepository.save(usuario);
    }

    @Override
    public void eliminar(Long codigo) {
        if (!usuarioRepository.existsById(codigo)) {
            throw new RuntimeException("El usuario no se encontró con el código " + codigo);
        }
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
    }
}