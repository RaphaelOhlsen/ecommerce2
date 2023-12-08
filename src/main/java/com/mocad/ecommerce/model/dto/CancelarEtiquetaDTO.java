package com.mocad.ecommerce.model.dto;

import java.io.Serializable;

public class CancelarEtiquetaDTO implements Serializable {

  private OrderCancelarEtiquetaDTO order = new OrderCancelarEtiquetaDTO();

  public OrderCancelarEtiquetaDTO getOrder() {
    return order;
  }

  public void setOrder(OrderCancelarEtiquetaDTO order) {
    this.order = order;
  }
}
