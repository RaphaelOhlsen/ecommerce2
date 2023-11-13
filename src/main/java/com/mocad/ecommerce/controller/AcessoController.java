package com.mocad.ecommerce.controller;

import com.mocad.ecommerce.model.Acesso;
import com.mocad.ecommerce.repository.AcessoRepository;
import com.mocad.ecommerce.service.AcessoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RestController
public class AcessoController {

  @Autowired
  private AcessoService acessoService;
  
  @Autowired
	private AcessoRepository acessoRepository;

  @ResponseBody
  @PostMapping("**/salvarAcesso")
  public ResponseEntity<Acesso> salvarAcesso(@RequestBody Acesso acesso) {
	
	Acesso acessoSalvo = acessoService.save(acesso);
	  
	return new ResponseEntity<Acesso>(acessoSalvo, HttpStatus.OK);
  }
  
  @ResponseBody /*Poder dar um retorno da API*/
  @PostMapping(value = "**/deleteAcesso") /*Mapeando a url para receber JSON*/
	public ResponseEntity<?> deleteAcesso(@RequestBody Acesso acesso) { /*Recebe o JSON e converte pra Objeto*/
		
		acessoRepository.deleteById(acesso.getId());
		
		return new ResponseEntity("Acesso Removido",HttpStatus.OK);
	}
}
