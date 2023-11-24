package com.mocad.ecommerce;

import com.mocad.ecommerce.controller.PessoaController;
import com.mocad.ecommerce.enums.TipoEndereco;
import com.mocad.ecommerce.model.Endereco;
import com.mocad.ecommerce.model.PessoaFisica;

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
    
    @Autowired
	private PessoaRepository pesssoaRepository;

    @Test
    public void testCadPessoaJuridica() throws ExceptionEcommerce {

        PessoaJuridica pessoaJuridica = new PessoaJuridica();

//        pessoaJuridica.setCnpj(""+ Calendar.getInstance().getTimeInMillis());
        pessoaJuridica.setCnpj("83.648.550/0001-49");
        pessoaJuridica.setNome("Raphael Bernardo Ohlsen");
        pessoaJuridica.setEmail("mocado3.flint@gmail.com");
        pessoaJuridica.setTelefone("119999934994");
        pessoaJuridica.setInscEstadual("12345p3d789047");
        pessoaJuridica.setInscMunicipal("123456738904");
        pessoaJuridica.setNomeFantasia("Armazem Raphael");
        pessoaJuridica.setRazaoSocial("Armazem Raphael LTDA");
        pessoaJuridica.setTipoPessoa("pessoaJuridica");


        Endereco endereco1 = new Endereco();
        endereco1.setBairro("Jd Dias");
        endereco1.setCep("556556565");
        endereco1.setComplemento("Casa cinza");
        endereco1.setEmpresa(pessoaJuridica);
        endereco1.setNumero("389");
        endereco1.setPessoa(pessoaJuridica);
        endereco1.setRuaLogra("Av. são joao sexto");
        endereco1.setTipoEndereco(TipoEndereco.COBRANCA);
        endereco1.setUf("PR");
        endereco1.setCidade("Curitiba");


        Endereco endereco2 = new Endereco();
        endereco2.setBairro("Jd Maracana");
        endereco2.setCep("7878778");
        endereco2.setComplemento("Andar 4");
        endereco2.setEmpresa(pessoaJuridica);
        endereco2.setNumero("555");
        endereco2.setPessoa(pessoaJuridica);
        endereco2.setRuaLogra("Av. maringá");
        endereco2.setTipoEndereco(TipoEndereco.ENTREGA);
        endereco2.setUf("PR");
        endereco2.setCidade("Curitiba");

        pessoaJuridica.getEnderecos().add(endereco2);
        pessoaJuridica.getEnderecos().add(endereco1);

        pessoaJuridica = pessoaController.salvarPJ(pessoaJuridica).getBody();

        assertEquals(true, pessoaJuridica.getId() > 0 );

        for (Endereco endereco : pessoaJuridica.getEnderecos()) {
            assertEquals(true, endereco.getId() > 0);
        }

        assertEquals(2, pessoaJuridica.getEnderecos().size());
    }
    
    @Test
	public void testCadPessoaFisica() throws ExceptionEcommerce {
		
		PessoaJuridica pessoaJuridica =  pesssoaRepository.existeCnpjCadastrado("62.160.238/0001-31");


		PessoaFisica pessoaFisica = new PessoaFisica();
		pessoaFisica.setCpf("738.630.580-03");
		pessoaFisica.setNome("Alex fernando");
		pessoaFisica.setEmail("alex.fe855499893r9559nando.egidio@gmail.com");
		pessoaFisica.setTelefone("45999795800");
		pessoaFisica.setEmpresa(pessoaJuridica);
		
		Endereco endereco1 = new Endereco();
		endereco1.setBairro("Jd Dias");
		endereco1.setCep("556556565");
		endereco1.setComplemento("Casa cinza");
		endereco1.setNumero("389");
		endereco1.setPessoa(pessoaFisica);
		endereco1.setRuaLogra("Av. são joao sexto");
		endereco1.setTipoEndereco(TipoEndereco.COBRANCA);
		endereco1.setUf("PR");
		endereco1.setCidade("Curitiba");
		endereco1.setEmpresa(pessoaJuridica);
		
		
		Endereco endereco2 = new Endereco();
		endereco2.setBairro("Jd Maracana");
		endereco2.setCep("7878778");
		endereco2.setComplemento("Andar 4");
		endereco2.setNumero("555");
		endereco2.setPessoa(pessoaFisica);
		endereco2.setRuaLogra("Av. maringá");
		endereco2.setTipoEndereco(TipoEndereco.ENTREGA);
		endereco2.setUf("PR");
		endereco2.setCidade("Curitiba");
		endereco2.setEmpresa(pessoaJuridica);
		
		pessoaFisica.getEnderecos().add(endereco2);
		pessoaFisica.getEnderecos().add(endereco1);

		pessoaFisica = pessoaController.salvarPF(pessoaFisica).getBody();
		
		assertEquals(true, pessoaFisica.getId() > 0 );
		
		for (Endereco endereco : pessoaFisica.getEnderecos()) {
			assertEquals(true, endereco.getId() > 0);
		}
		
		assertEquals(2, pessoaFisica.getEnderecos().size());

	}
    
   
}