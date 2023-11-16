package com.mocad.ecommerce.security;

import com.mocad.ecommerce.ApplicationContextLoad;
import com.mocad.ecommerce.model.Usuario;
import com.mocad.ecommerce.repository.UsuarioRepository;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;


/* Criar autenticaćão e retornar a autenticacao JWT */
@Service
@Component
public class JWTTokenAutenticacaoService {

    /* */
    public static final long UM_SEGUNDO = 1000;
    public static final long UM_MINUTO  = 60 * UM_SEGUNDO;
    public static final long UMA_HORA   = 60 * UM_MINUTO;
    public static final long UM_DIA     = 24 * UMA_HORA;
    public static final long UMA_SEMANA = 7 * UM_DIA;

    private static final String SECRET = "ss/-*-*sds565dsd-s/d-s*dsds";

    private static final String TOKEN_PREFIX = "Bearer";

    private static final String HEADER_STRING = "Authorization";

    /* Gera o token e envia a resposta para o cliente com o JWT*/
    public void addAuthentication(HttpServletResponse response, String username) throws Exception {
        /*Montagem do Token*/

       String JWT = Jwts.builder() /*Chama o gerador de token*/
                    .setSubject(username) /*Adiciona o usuário*/
                    .setExpiration(new Date(System.currentTimeMillis() + UMA_SEMANA)) /*Tempo de expiração*/
                    .signWith(SignatureAlgorithm.HS512, SECRET).compact(); /*Compactação e algoritmos de geração de senha*/

       String token = TOKEN_PREFIX + " " + JWT; /*Junta o token com o prefixo*/

       response.addHeader(HEADER_STRING, token); /*Adiciona no cabeçalho http*/

        liberacaoCors(response); /*Liberação de CORS*/

       /*Usado para ver no Insomnia para efeito de teste*/
       response.getWriter().write("{\"Authorization\": \""+token+"\"}"); /*Escreve token como resposta no corpo http*/
    }

    /* Retorna o usuario validado com token ou caso não seka válido retorna null */
    public Authentication getAuthentication(HttpServletRequest request, HttpServletResponse response) {
        /*Pega o token enviado no cabeçalho http*/
        String token = request.getHeader(HEADER_STRING);

        if (token != null) {
            String tokenLimpo = token.replace(TOKEN_PREFIX, "").trim(); /*Remove o prefixo do token*/

            /*Faz a validação do token do usuário na requisicão*/
            String user = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(tokenLimpo).getBody().getSubject();

            if (user != null) {
                Usuario usuario = ApplicationContextLoad.
                        getApplicationContext().
                        getBean(UsuarioRepository.class).findUserByLogin(user);
                if (usuario != null) {
                    return new UsernamePasswordAuthenticationToken(
                            usuario.getLogin(),
                            usuario.getSenha(),
                            usuario.getAuthorities()
                    ); /*Retorna as autorizações do usuário*/
                }
            }
        }


        liberacaoCors(response);
        return null; /*Não autorizado*/

    }

    /* Fazendo a liberaćão contra erro de CORS no navegadpor */
    private void liberacaoCors(HttpServletResponse response) {
        if (response.getHeader("Access-Control-Allow-Origin") == null) {
            response.addHeader("Access-Control-Allow-Origin", "*");
        }

        if (response.getHeader("Access-Control-Allow-Headers") == null) {
            response.addHeader("Access-Control-Allow-Headers", "*");
        }

        if (response.getHeaders("Access-Control-Requested-Headers") == null) {
            response.addHeader("Access-Control-Requested-Headers", "*");
        }

        if (response.getHeader("Access-Control-Allow-Methods") == null) {
            response.addHeader("Access-Control-Allow-Methods", "*");
        }
    }
}
