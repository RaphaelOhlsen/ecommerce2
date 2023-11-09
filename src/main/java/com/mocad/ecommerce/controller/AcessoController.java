package com.mocad.ecommerce.controller;

import com.mocad.ecommerce.model.Acesso;
import com.mocad.ecommerce.service.AcessoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AcessoController {

  @Autowired
  private AcessoService acessoService;

  @PostMapping("/acesso")
  public Acesso salvarAcesso(Acesso acesso) {
    return acessoService.save(acesso);
  }
}
