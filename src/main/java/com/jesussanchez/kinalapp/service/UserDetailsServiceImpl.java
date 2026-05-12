package com.jesussanchez.kinalapp.service;

import com.jesussanchez.kinalapp.entity.Usuario;
import com.jesussanchez.kinalapp.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("Buscando usuario con email: " + email);

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + email));

        System.out.println("Usuario encontrado: " + usuario.getEmail());
        System.out.println("Rol: " + usuario.getRol());
        System.out.println("Contraseña almacenada (hash): " + usuario.getPassword());
        System.out.println("Estado: " + usuario.getEstado());

        return usuario;
    }
}