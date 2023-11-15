package com.mocad.ecommerce.service;


import com.mocad.ecommerce.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.mocad.ecommerce.model.Usuario;
import org.springframework.stereotype.Service;

@Service
public class ImplementacaoUserDetailsService implements UserDetailsService{

    private UsuarioRepository usuarioRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findUserByLogin(username);

        if (usuario == null) {
            throw new UsernameNotFoundException("Usuário não foi encontrado");
        }

        return new User(usuario.getLogin(), usuario.getPassword(), usuario.getAuthorities());
    }
}