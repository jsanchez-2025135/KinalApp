package com.jesussanchez.kinalapp.repository;

import com.jesussanchez.kinalapp.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional; // Importante para manejar nulos de forma elegante

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    List<Usuario> findByEstado(int estado);

    Optional<Usuario> findByEmail(String email);
}