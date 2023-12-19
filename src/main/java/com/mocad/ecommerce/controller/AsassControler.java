package com.mocad.ecommerce.controller;

import com.mocad.ecommerce.model.dto.ObjetoPostCarneJuno;
import com.mocad.ecommerce.service.ServiceJunoBoleto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AsassControler {

  @Autowired
  private ServiceJunoBoleto serviceJunoBoleto;

  @GetMapping(value = "/gerarChavePix")
  public String gerarChave() throws Exception {
    return serviceJunoBoleto.criarChavePixAsaas();
  }

  @GetMapping(value = "/consultarCliente")
  public String consultarCliente(@RequestBody ObjetoPostCarneJuno pessoa) throws Exception {
    return serviceJunoBoleto.buscaClientePessoaApiAsaas(pessoa);
  }
}
