package com.mocad.ecommerce.service;
import java.io.Serializable;

import javax.ws.rs.core.MediaType;
import javax.xml.bind.DatatypeConverter;

import com.mocad.ecommerce.model.AccessTokenJunoAPI;
import com.mocad.ecommerce.repository.AccesTokenJunoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;



@Service
public class ServiceJunoBoleto implements Serializable {

  private static final long serialVersionUID = 1L;

  @Autowired
  private AccessTokenJunoService accessTokenJunoService;

  @Autowired
  private AccesTokenJunoRepository accesTokenJunoRepository;

  public AccessTokenJunoAPI obterTokenApiJuno() throws Exception {

    AccessTokenJunoAPI accessTokenJunoAPI = accessTokenJunoService.buscaTokenAtivo();

    if (accessTokenJunoAPI == null || (accessTokenJunoAPI != null && accessTokenJunoAPI.expirado()) ) {

      String clienteID = "vi7QZerW09C8JG1p";
      String secretID = "$A_+&ksH}&+2<3VM]1MZqc,F_xif_-Dd";

      Client client = new HostIgnoringClient("https://api.juno.com.br/").hostIgnoringClient();

      WebResource webResource = client.resource("https://api.juno.com.br/authorization-server/oauth/token?grant_type=client_credentials");

      String basicChave = clienteID + ":" + secretID;
      String token_autenticao = DatatypeConverter.printBase64Binary(basicChave.getBytes());

      ClientResponse clientResponse = webResource.
          accept(MediaType.APPLICATION_FORM_URLENCODED)
          .type(MediaType.APPLICATION_FORM_URLENCODED)
          .header("Content-Type", "application/x-www-form-urlencoded")
          .header("Authorization", "Basic " + token_autenticao)
          .post(ClientResponse.class);

      if (clientResponse.getStatus() == 200) { /*Sucesso*/
        accesTokenJunoRepository.deleteAll();
        accesTokenJunoRepository.flush();

        AccessTokenJunoAPI accessTokenJunoAPI2 = clientResponse.getEntity(AccessTokenJunoAPI.class);
        accessTokenJunoAPI2.setToken_acesso(token_autenticao);

        accessTokenJunoAPI2 = accesTokenJunoRepository.saveAndFlush(accessTokenJunoAPI2);

        return accessTokenJunoAPI2;
      }else {
        return null;
      }


    }else {
      return accessTokenJunoAPI;
    }
  }

}