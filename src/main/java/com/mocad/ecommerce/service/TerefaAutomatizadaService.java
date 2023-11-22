package com.mocad.ecommerce.service;

import com.mocad.ecommerce.model.Usuario;
import com.mocad.ecommerce.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.List;

@Service
public class TerefaAutomatizadaService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ServiceSendEmail serviceSendEmail;

    @Scheduled(initialDelay = 2000,  fixedDelay =  86400000) /* Roda a cada 24 horas */
//    @Scheduled(cron = "0 0 11 * * *", zone = "America/Sao_Paulo") /* Roda todo dia as 11:00 */
    public void notificarUserTrocaSenha() throws UnsupportedEncodingException, MessagingException, InterruptedException {

        List<Usuario> usuarios = usuarioRepository.usuarioSenhaVencida();

        for (Usuario usuario : usuarios) {

            StringBuilder msg = new StringBuilder();
            msg.append("Olá, ").append(usuario.getPessoa().getNome()).append("<br/>");
            msg.append("Está na hora de trocar sua senha, já passou 90 dias de validade.").append("<br/>");
            msg.append("Troque sua senha a loja virtual do Alex - JDEV treinamento");

            serviceSendEmail.enviarEmailHtml("Troca de senha", msg.toString(), usuario.getLogin());

            Thread.sleep(3000);

        }
    }
}
