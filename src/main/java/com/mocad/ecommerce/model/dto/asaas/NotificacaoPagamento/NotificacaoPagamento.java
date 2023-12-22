package com.mocad.ecommerce.model.dto.asaas.NotificacaoPagamento;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mocad.ecommerce.model.dto.asaas.NotificacaoPagamento.Payment.Payment;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NotificacaoPagamento {

  private String event;

  private Payment payment = new Payment();

  public String getEvent() {
    return event;
  }

  public void setEvent(String event) {
    this.event = event;
  }

  public Payment getPayment() {
    return payment;
  }

  public void setPayment(Payment payment) {
    this.payment = payment;
  }

  public String idFatura() {
    return payment.getId();
  }

  public String statusPagamento() {
    return getPayment().getStatus();
  }

  public Boolean boletoPixFaturaPaga() {
    return statusPagamento().equalsIgnoreCase("CONFIRMED") || statusPagamento().equalsIgnoreCase("RECEIVED");
  }

}
