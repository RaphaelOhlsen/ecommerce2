package com.mocad.ecommerce.service;

import com.mocad.ecommerce.model.PessoaJuridica;
import com.mocad.ecommerce.model.Usuario;
import com.mocad.ecommerce.repository.PessoaRepository;
import com.mocad.ecommerce.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;

@Service
public class PessoaUserService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

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

            usuarioRepository.inserirAcessoUsuarioPJ(usuarioPJ.getId());
        }

        return pessoaJuridica;
    }

}
