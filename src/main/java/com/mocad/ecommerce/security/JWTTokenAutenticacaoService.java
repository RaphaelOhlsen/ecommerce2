package com.mocad.ecommerce.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

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

       /*Usado para ver no Insomnia para efeito de teste*/
       response.getWriter().write("{\"Authorization\": \""+token+"\"}"); /*Escreve token como resposta no corpo http*/
    }
}
