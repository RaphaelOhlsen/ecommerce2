package com.mocad.ecommerce;

import com.mocad.ecommerce.controller.AcessoController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.mocad.ecommerce.model.Acesso;
import com.mocad.ecommerce.repository.AcessoRepository;

import antlr.collections.List;
import junit.framework.TestCase;

@SpringBootTest(classes = EcommerceApplication.class)
public class EcommerceApplicationTests extends TestCase{

	@Autowired
	private AcessoController acessoController;
	
	@Autowired
	private AcessoRepository acessoRepository;

	@Test
	public void testCadastraAcesso() {

		Acesso acesso =  new Acesso();

		acesso.setDescricao("ROLE_USER");

		acesso = acessoController.salvarAcesso(acesso).getBody();
		
		assertEquals(true, acesso.getId() > 0);
		
		/* Teste de Carregamento */
		
		Acesso acesso2 = acessoRepository.findById(acesso.getId()).get();
		
		assertEquals(acesso.getId(), acesso2.getId());
		
		/* Teste de delete */
		
		acessoRepository.deleteById(acesso2.getId());
		
		acessoRepository.flush();
		
		Acesso acesso3 = acessoRepository.findById(acesso2.getId()).orElse(null);
		
		assertEquals(true, acesso3 == null);
		

	}

}
