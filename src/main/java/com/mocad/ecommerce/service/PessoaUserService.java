package com.mocad.ecommerce.service;

import com.mocad.ecommerce.model.PessoaFisica;
import com.mocad.ecommerce.model.PessoaJuridica;
import com.mocad.ecommerce.model.Usuario;
import com.mocad.ecommerce.model.dto.CepDTO;
import com.mocad.ecommerce.repository.PessoaFisicaRepository;
import com.mocad.ecommerce.repository.PessoaRepository;
import com.mocad.ecommerce.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Calendar;

@Service
public class PessoaUserService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private PessoaFisicaRepository pessoaFisicaRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ServiceSendEmail serviceSendEmail;

    public PessoaJuridica salvarPessoaJuridica(PessoaJuridica pessoaJuridica) {

        for (int i = 0; i < pessoaJuridica.getEnderecos().size(); i++) {
            pessoaJuridica.getEnderecos().get(i).setPessoa(pessoaJuridica);
            pessoaJuridica.getEnderecos().get(i).setEmpresa(pessoaJuridica);
        }

        pessoaJuridica = pessoaRepository.save(pessoaJuridica);

        Usuario usuarioPJ = usuarioRepository.findUserByPessoa(pessoaJuridica.getId(), pessoaJuridica.getEmail());

        if (usuarioPJ == null) {
            String constraint = usuarioRepository.consultaContraintAcesso();
            if (constraint != null) {
                jdbcTemplate.execute("begin; alter table usuarios_acesso drop constraint " + constraint + "; commit;");
            }
            usuarioPJ = new Usuario();
            usuarioPJ.setDataAtualSenha(Calendar.getInstance().getTime());
            usuarioPJ.setEmpresa(pessoaJuridica);
            usuarioPJ.setPessoa(pessoaJuridica);
            usuarioPJ.setLogin(pessoaJuridica.getEmail());

            String senha = "" + Calendar.getInstance().getTimeInMillis();

            String senhaCript = new BCryptPasswordEncoder().encode(senha);

            usuarioPJ.setSenha(senhaCript);

            usuarioPJ = usuarioRepository.save(usuarioPJ);

            usuarioRepository.inserirAcessoUsuario(usuarioPJ.getId());
            usuarioRepository.inserirAcessoUsuarioPJ(usuarioPJ.getId(), "ROLE_ADMIN");

            StringBuilder menssagemHtml = new StringBuilder();

            menssagemHtml.append("<b>Segue abaixo seus dados de acesso para a loja virtual</b><br/>");
            menssagemHtml.append("<b>Login: </b>"+pessoaJuridica.getEmail()+"<br/>");
//            menssagemHtml.append("<b>Login: </b>").append(pessoaJuridica.getEmail()).append("<br/>");
            menssagemHtml.append("<b>Senha: </b>").append(senha).append("<br/><br/>");
            menssagemHtml.append("Obrigado!");

            try {
                serviceSendEmail.enviarEmailHtml("Acesso Gerado para Loja Virtual", menssagemHtml.toString() , pessoaJuridica.getEmail());
            }catch (Exception e) {
                e.printStackTrace();
            }
        }

        return pessoaJuridica;
    }

    public PessoaFisica salvarPessoaFisica (PessoaFisica pessoaFisica) {
        for (int i = 0; i < pessoaFisica.getEnderecos().size(); i++) {
            pessoaFisica.getEnderecos().get(i).setPessoa(pessoaFisica);
//            pessoaFisica.getEnderecos().get(i).setEmpresa(pessoaFisica);
        }

        pessoaFisica = pessoaFisicaRepository.save(pessoaFisica);

        Usuario usuarioPF = usuarioRepository.findUserByPessoa(pessoaFisica.getId(), pessoaFisica.getEmail());

        usuarioPF = new Usuario();
        usuarioPF.setDataAtualSenha(Calendar.getInstance().getTime());
        usuarioPF.setEmpresa(pessoaFisica.getEmpresa());
        usuarioPF.setPessoa(pessoaFisica);
        usuarioPF.setLogin(pessoaFisica.getEmail());

        String senha = "" + Calendar.getInstance().getTimeInMillis();
        String senhaCript = new BCryptPasswordEncoder().encode(senha);

        usuarioPF.setSenha(senhaCript);

        usuarioPF = usuarioRepository.save(usuarioPF);

        usuarioRepository.inserirAcessoUsuario(usuarioPF.getId());

        StringBuilder menssagemHtml = new StringBuilder();

        menssagemHtml.append("<b>Segue abaixo seus dados de acesso para a loja virtual</b><br/>");
        menssagemHtml.append("<b>Login: </b>").append(pessoaFisica.getEmail()).append("<br/>");
        menssagemHtml.append("<b>Senha: </b>").append(senha).append("<br/><br/>");
        menssagemHtml.append("Obrigado!");

        try {
            serviceSendEmail.enviarEmailHtml("Acesso Gerado para Loja Virtual", menssagemHtml.toString() , pessoaFisica.getEmail());
        }catch (Exception e) {
            e.printStackTrace();
        }

        return pessoaFisica;
    }

    public CepDTO consultaCep(String cep) {
        return new RestTemplate().getForEntity("https://viacep.com.br/ws/" + cep + "/json/", CepDTO.class).getBody();
    }


}
