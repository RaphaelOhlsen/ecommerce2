package com.mocad.ecommerce.service;

import com.mocad.ecommerce.model.Acesso;
import com.mocad.ecommerce.repository.AcessoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AcessoService {

  @Autowired
  private AcessoRepository acessoRepository;

  public Acesso save(Acesso acesso) {
    return acessoRepository.save(acesso);
  }
}
