package com.mocad.ecommerce;

import com.mocad.ecommerce.model.PessoaFisica;
import com.mocad.ecommerce.model.PessoaJuridica;
import com.mocad.ecommerce.repository.PessoaRepository;
import com.mocad.ecommerce.service.PessoaUserService;
import junit.framework.TestCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

@Profile("test")
@SpringBootTest(classes = EcommerceApplication.class)
public class TestePessoaUsuario extends TestCase {
    @Autowired
    private PessoaUserService pessoaUserService;

    @Autowired
    private PessoaRepository pessoaRepository;

    @Test
    public void testCadPessoaFisica() {

        PessoaJuridica pessoaJuridica = new PessoaJuridica();

        pessoaJuridica.setCnpj("12345678900");
        pessoaJuridica.setNome("Joao da Silva");
        pessoaJuridica.setEmail("joao@teste.com");
        pessoaJuridica.setTelefone("11999999999");
        pessoaJuridica.setInscEstadual("12345678900");
        pessoaJuridica.setInscMunicipal("12345678900");
        pessoaJuridica.setNomeFantasia("Armazem do João");
        pessoaJuridica.setRazaoSocial("Armazem do João LTDA");
        pessoaJuridica.setTipoPessoa("pessoaJuridica");

        pessoaRepository.save(pessoaJuridica);

//        PessoaFisica pessoaFisica = new PessoaFisica();
//
//        pessoaFisica.setNome("João da Silva");
//        pessoaFisica.setCpf("123.456.789-00");
//        pessoaFisica.setTelefone("11999999999");
//        pessoaFisica.setEmail("joao@teste.com");
//        pessoaFisica.setTipoPessoa("pessoaFisica");
    }
}
