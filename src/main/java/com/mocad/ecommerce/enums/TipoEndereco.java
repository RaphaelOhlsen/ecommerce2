package com.mocad.ecommerce.enums;

public enum TipoEndereco {

  COBRANCA("Cobrança"),
  ENTREGA("Entrega");

  private final String descricao;

  private TipoEndereco(String descricao) {
    this.descricao = descricao;
  }

  public String getDescricao() {
    return descricao;
  }

  @Override
  public String toString() {
    return this.descricao;
  }
}
