package com.mocad.ecommerce.model.dto;

public class CobrancaApiAsaasCartao {

  private String customer;
  private String billingType;
  private float value;
  private String dueDate;
  private String description;
  private String externalReference;
  private Integer installmentCount;
  private float installmentValue;

  private DiscontCobancaAsaas discount = new DiscontCobancaAsaas();
  private InterestCobrancaAsass interest = new InterestCobrancaAsass();
  private FineConbrancaAsaas fine = new FineConbrancaAsaas();

  private CartaoCreditoApiAsaas creditCard = new CartaoCreditoApiAsaas();
  private CartaoCreditoAsaasHolderInfo creditCardHolderInfo = new CartaoCreditoAsaasHolderInfo();

  private boolean postalService = false;

  public String getCustomer() {
    return customer;
  }

  public void setCustomer(String customer) {
    this.customer = customer;
  }

  public String getBillingType() {
    return billingType;
  }

  public void setBillingType(String billingType) {
    this.billingType = billingType;
  }

  public float getValue() {
    return value;
  }

  public void setValue(float value) {
    this.value = value;
  }

  public String getDueDate() {
    return dueDate;
  }

  public void setDueDate(String dueDate) {
    this.dueDate = dueDate;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getExternalReference() {
    return externalReference;
  }

  public void setExternalReference(String externalReference) {
    this.externalReference = externalReference;
  }

  public Integer getInstallmentCount() {
    return installmentCount;
  }

  public void setInstallmentCount(Integer installmentCount) {
    this.installmentCount = installmentCount;
  }

  public float getInstallmentValue() {
    return installmentValue;
  }

  public void setInstallmentValue(float installmentValue) {
    this.installmentValue = installmentValue;
  }

  public DiscontCobancaAsaas getDiscount() {
    return discount;
  }

  public void setDiscount(DiscontCobancaAsaas discount) {
    this.discount = discount;
  }

  public InterestCobrancaAsass getInterest() {
    return interest;
  }

  public void setInterest(InterestCobrancaAsass interest) {
    this.interest = interest;
  }

  public FineConbrancaAsaas getFine() {
    return fine;
  }

  public void setFine(FineConbrancaAsaas fine) {
    this.fine = fine;
  }

  public CartaoCreditoApiAsaas getCreditCard() {
    return creditCard;
  }

  public void setCreditCard(CartaoCreditoApiAsaas creditCard) {
    this.creditCard = creditCard;
  }

  public CartaoCreditoAsaasHolderInfo getCreditCardHolderInfo() {
    return creditCardHolderInfo;
  }

  public void setCreditCardHolderInfo(CartaoCreditoAsaasHolderInfo creditCardHolderInfo) {
    this.creditCardHolderInfo = creditCardHolderInfo;
  }

  public boolean isPostalService() {
    return postalService;
  }

  public void setPostalService(boolean postalService) {
    this.postalService = postalService;
  }

}