package com.mocad.ecommerce;

import com.mocad.ecommerce.controller.PessoaController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

import com.mocad.ecommerce.model.PessoaJuridica;
import com.mocad.ecommerce.repository.PessoaRepository;
import com.mocad.ecommerce.service.PessoaUserService;

import junit.framework.TestCase;

import java.util.Calendar;

@Profile("test")
@SpringBootTest(classes = EcommerceApplication.class)
public class TestePessoaUsuario extends TestCase{
       @Autowired
    private PessoaController pessoaController;

    @Test
    public void testCadPessoaFisica() throws ExceptionEcommerce {

        PessoaJuridica pessoaJuridica = new PessoaJuridica();

        pessoaJuridica.setCnpj(""+ Calendar.getInstance().getTimeInMillis());
        pessoaJuridica.setNome("Raphael Ohlsen");
        pessoaJuridica.setEmail("admin@teste.com");
        pessoaJuridica.setTelefone("119999934994");
        pessoaJuridica.setInscEstadual("12345678904");
        pessoaJuridica.setInscMunicipal("12345678904");
        pessoaJuridica.setNomeFantasia("Armazem Raphael");
        pessoaJuridica.setRazaoSocial("Armazem Raphael LTDA");
        pessoaJuridica.setTipoPessoa("pessoaJuridica");

        pessoaController.salvarPJ(pessoaJuridica);
    }
}
