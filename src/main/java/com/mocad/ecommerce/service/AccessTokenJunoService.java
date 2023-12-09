package com.mocad.ecommerce.service;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import com.mocad.ecommerce.model.AccessTokenJunoAPI;
import org.springframework.stereotype.Service;



@Service
public class AccessTokenJunoService {

  @PersistenceContext
  private EntityManager entityManager;

  public AccessTokenJunoAPI buscaTokenAtivo() {

    try {

      AccessTokenJunoAPI accessTokenJunoAPI =
          (AccessTokenJunoAPI) entityManager
              .createQuery("select a from AccessTokenJunoAPI a ")
              .setMaxResults(1).getSingleResult();

      return accessTokenJunoAPI;
    }catch (NoResultException e) {
      return null;
    }
  }
}