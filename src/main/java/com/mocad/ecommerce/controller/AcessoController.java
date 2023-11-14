package com.mocad.ecommerce.controller;

import com.mocad.ecommerce.model.Acesso;
import com.mocad.ecommerce.repository.AcessoRepository;
import com.mocad.ecommerce.service.AcessoService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
  
  @ResponseBody
  @DeleteMapping(value = "**/deleteAcessoPorId/{id}") /*Mapeando a url para receber JSON*/
	public ResponseEntity<?> deleteAcessoPorId(@PathVariable Long id) {
		
		acessoRepository.deleteById(id);
		
		return new ResponseEntity("Acesso Removido",HttpStatus.OK);
	}
  
  @ResponseBody
  @GetMapping(value = "**/obeterAcesso/{id}") /*Mapeando a url para receber JSON*/
	public ResponseEntity<?> obeterAcesso(@PathVariable Long id) {
		
		Acesso acesso = acessoRepository.findById(id).get();
		
		return new ResponseEntity<Acesso>(acesso, HttpStatus.OK);
	}
  
  @ResponseBody
  @GetMapping(value = "**/buscarPorDesc/{desc}") /*Mapeando a url para receber JSON*/
	public ResponseEntity<List<Acesso>> buscarPorDesc(@PathVariable String desc) {
		
		List<Acesso> acesso = acessoRepository.buscarAcessoDesc(desc);
		
		return new ResponseEntity<List<Acesso>>(acesso, HttpStatus.OK);
	}
}
