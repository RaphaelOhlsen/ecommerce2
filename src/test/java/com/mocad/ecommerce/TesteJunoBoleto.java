package com.mocad.ecommerce;

import com.mocad.ecommerce.service.ServiceJunoBoleto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;


import junit.framework.TestCase;

@Profile("dev")
@SpringBootTest(classes = EcommerceApplication.class)
public class TesteJunoBoleto extends TestCase {

  @Autowired
  private ServiceJunoBoleto serviceJunoBoleto;



  @Test
  public void testcriarChavePixAsaas() throws Exception {

    String chaveAPi = serviceJunoBoleto.criarChavePixAsaas();

    System.out.println("Chave Asass API" + chaveAPi);
  }








}