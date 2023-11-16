package com.mocad.ecommerce.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mocad.ecommerce.model.Usuario;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JWTLoginFilter extends AbstractAuthenticationProcessingFilter {

    /*Confgurando o gerenciado de autenticacao*/
    public JWTLoginFilter(String url, AuthenticationManager authenticationManager) {

        /*Obriga a autenticar a url*/
        super(new AntPathRequestMatcher(url));

        /*Gerenciador de autenticacao*/
        setAuthenticationManager(authenticationManager);

    }

    /*Retorna o usuário ao processr a autenticacao*/
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {
        /*Obter o usuário*/
        Usuario user = new ObjectMapper().readValue(request.getInputStream(), Usuario.class);

        /*Retorna o user com login e senha*/
        return getAuthenticationManager().
                authenticate(new UsernamePasswordAuthenticationToken(user.getLogin(), user.getSenha()));
    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult)
            throws IOException, ServletException {
        try {
            new JWTTokenAutenticacaoService().addAuthentication(response, authResult.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
