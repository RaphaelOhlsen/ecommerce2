package com.mocad.ecommerce;

import com.mocad.ecommerce.controller.AcessoController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.mocad.ecommerce.model.Acesso;
import com.mocad.ecommerce.repository.AcessoRepository;
import com.mocad.ecommerce.service.AcessoService;

@SpringBootTest(classes = EcommerceApplication.class)
class EcommerceApplicationTests {

	@Autowired
	private AcessoController acessoController;

	@Test
	public void testCadastraAcesso() {

		Acesso acesso =  new Acesso();

		acesso.setDescricao("ROLE_ADMIN");

		acessoController.salvarAcesso(acesso);

	}

}
