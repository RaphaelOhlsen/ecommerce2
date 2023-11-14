package com.mocad.ecommerce;


import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.mocad.ecommerce.controller.AcessoController;
import com.mocad.ecommerce.model.Acesso;
import com.mocad.ecommerce.repository.AcessoRepository;
import junit.framework.TestCase;

@SpringBootTest(classes = EcommerceApplication.class)
public class EcommerceApplicationTests extends TestCase{

	@Autowired
	private AcessoController acessoController;
	
	@Autowired
	private AcessoRepository acessoRepository;
	
	@Autowired
	private WebApplicationContext wac;
	
	/*Teste do end-point de salvar*/
	@Test
	public void testRestApiCadastroAcesso() throws JsonProcessingException, Exception {
		
	    DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(this.wac);
	    MockMvc mockMvc = builder.build();
	    
	    Acesso acesso = new Acesso();
	    
	    acesso.setDescricao("ROLE_COMPRADOR");
	    
	    ObjectMapper objectMapper = new ObjectMapper();
	    
	    ResultActions retornoApi = mockMvc
	    						 .perform(MockMvcRequestBuilders.post("/salvarAcesso")
	    						 .content(objectMapper.writeValueAsString(acesso))
	    						 .accept(MediaType.APPLICATION_JSON)
	    						 .contentType(MediaType.APPLICATION_JSON));
	    
	    System.out.println("Retorno da API: " + retornoApi.andReturn().getResponse().getContentAsString());
	    
	    /*Conveter o retorno da API para um obejto de acesso*/
	    
	    Acesso objetoRetorno = objectMapper.
	    					   readValue(retornoApi.andReturn().getResponse().getContentAsString(),
	    					   Acesso.class);
	    
	    assertEquals(acesso.getDescricao(), objetoRetorno.getDescricao());
	}
	
	/*Teste do end-point de deletar*/
	@Test
	public void testRestApiDeletarAcesso() throws JsonProcessingException, Exception {
		
	    DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(this.wac);
	    MockMvc mockMvc = builder.build();
	    
	    Acesso acesso = new Acesso();
	    
	    acesso.setDescricao("ROLE_TESTE_DELETE");
	    
	    acesso = acessoRepository.save(acesso);
	    
	    ObjectMapper objectMapper = new ObjectMapper();
	    
	    ResultActions retornoApi = mockMvc
	    						 .perform(MockMvcRequestBuilders.post("/deleteAcesso")
	    						 .content(objectMapper.writeValueAsString(acesso))
	    						 .accept(MediaType.APPLICATION_JSON)
	    						 .contentType(MediaType.APPLICATION_JSON));
	    
	    System.out.println("Retorno da API: " + retornoApi.andReturn().getResponse().getContentAsString());
	    System.out.println("Status de Retorno: " + retornoApi.andReturn().getResponse().getStatus());
	   
	    assertEquals(200, retornoApi.andReturn().getResponse().getStatus());
	    assertEquals("Acesso Removido", retornoApi.andReturn().getResponse().getContentAsString());
	}
	
	/*Teste do end-point de deletar por Id*/
	@Test
	public void testRestApiDeletarAcessoporId() throws JsonProcessingException, Exception {
		
	    DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(this.wac);
	    MockMvc mockMvc = builder.build();
	    
	    Acesso acesso = new Acesso();
	    
	    acesso.setDescricao("ROLE_TESTE_DELETE_ID");
	    
	    acesso = acessoRepository.save(acesso);
	    
	    ObjectMapper objectMapper = new ObjectMapper();
	    
	    ResultActions retornoApi = mockMvc
	    						 .perform(MockMvcRequestBuilders.delete("/deleteAcessoPorId/" + acesso.getId())
	    						 .content(objectMapper.writeValueAsString(acesso))
	    						 .accept(MediaType.APPLICATION_JSON)
	    						 .contentType(MediaType.APPLICATION_JSON));
	    
	    System.out.println("Retorno da API: " + retornoApi.andReturn().getResponse().getContentAsString());
	    System.out.println("Status de Retorno: " + retornoApi.andReturn().getResponse().getStatus());
	   
	    assertEquals(200, retornoApi.andReturn().getResponse().getStatus());
	    assertEquals("Acesso Removido", retornoApi.andReturn().getResponse().getContentAsString());
	}
	
	/*Teste do end-point consultar acesso por Id*/
	@Test
	public void testRestApiObterAcessoId() throws JsonProcessingException, Exception {
		
	    DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(this.wac);
	    MockMvc mockMvc = builder.build();
	    
	    Acesso acesso = new Acesso();
	    
	    acesso.setDescricao("ROLE_OBTER_POR_ID");
	    
	    acesso = acessoRepository.save(acesso);
	    
	    ObjectMapper objectMapper = new ObjectMapper();
	    
	    ResultActions retornoApi = mockMvc
	    						 .perform(MockMvcRequestBuilders.get("/obeterAcesso/" + acesso.getId())
	    						 .content(objectMapper.writeValueAsString(acesso))
	    						 .accept(MediaType.APPLICATION_JSON)
	    						 .contentType(MediaType.APPLICATION_JSON));
	   
	    assertEquals(200, retornoApi.andReturn().getResponse().getStatus());
	    
	    Acesso objetoRetorno = objectMapper.
				   readValue(retornoApi.andReturn().getResponse().getContentAsString(),
				   Acesso.class);
	    
	    assertEquals(acesso.getDescricao(), objetoRetorno.getDescricao());
	    assertEquals(acesso.getId(), objetoRetorno.getId());
	}
	
	@Test
	public void testRestApiObterAcessoDesc() throws JsonProcessingException, Exception {
		
	    DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(this.wac);
	    MockMvc mockMvc = builder.build();
	    
	    Acesso acesso = new Acesso();
	    
	    acesso.setDescricao("ROLE_TESTE_OBTER_LIST");
	    
	    acesso = acessoRepository.save(acesso);
	    
	    ObjectMapper objectMapper = new ObjectMapper();
	    
	    ResultActions retornoApi = mockMvc
	    						 .perform(MockMvcRequestBuilders.get("/buscarPorDesc/OBTER_LIST")
	    						 .content(objectMapper.writeValueAsString(acesso))
	    						 .accept(MediaType.APPLICATION_JSON)
	    						 .contentType(MediaType.APPLICATION_JSON));
	    
	    assertEquals(200, retornoApi.andReturn().getResponse().getStatus());
	    
	    
	    List<Acesso> retornoApiList = objectMapper.
	    							     readValue(retornoApi.andReturn()
	    									.getResponse().getContentAsString(),
	    									 new TypeReference<List<Acesso>> () {});

	    assertEquals(1, retornoApiList.size());
	    
	    assertEquals(acesso.getDescricao(), retornoApiList.get(0).getDescricao());
	    
	    
	    acessoRepository.deleteById(acesso.getId());
	    
	}

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
		
/*Teste de query*/
		
		acesso = new Acesso();
		
		acesso.setDescricao("ROLE_ALUNO");
		
		acesso = acessoController.salvarAcesso(acesso).getBody();
		
		List<Acesso> acessos = acessoRepository.buscarAcessoDesc("ALUNO".trim().toUpperCase());
		
		assertEquals(1, acessos.size());
		
		acessoRepository.deleteById(acesso.getId());
		

	}

}
