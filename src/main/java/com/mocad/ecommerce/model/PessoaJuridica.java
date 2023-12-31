package com.mocad.ecommerce.model;

import org.hibernate.validator.constraints.br.CNPJ;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "pessoa_juridica")
@PrimaryKeyJoinColumn(name = "id")
public class PessoaJuridica  extends Pessoa {
  private static final long serialVersionUID = 1L;

  @CNPJ(message = "CNPJ inválido")
  @Column(nullable = false)
  private String cnpj;

  @Column(nullable = false)
  private String inscEstadual;

  private String inscMunicipal;

  @Column(nullable = false)
  private String razaoSocial;

  @Column(nullable = false)
  private String nomeFantasia;

  private String categoria;

  public String getCnpj() {
    return cnpj;
  }

  public void setCnpj(String cnpj) {
    this.cnpj = cnpj;
  }

  public String getInscEstadual() {
    return inscEstadual;
  }

  public void setInscEstadual(String inscEstadual) {
    this.inscEstadual = inscEstadual;
  }

  public String getInscMunicipal() {
    return inscMunicipal;
  }

  public void setInscMunicipal(String inscMunicipal) {
    this.inscMunicipal = inscMunicipal;
  }

  public String getRazaoSocial() {
    return razaoSocial;
  }

  public void setRazaoSocial(String razaoSocial) {
    this.razaoSocial = razaoSocial;
  }

  public String getNomeFantasia() {
    return nomeFantasia;
  }

  public void setNomeFantasia(String nomeFantasia) {
    this.nomeFantasia = nomeFantasia;
  }

  public String getCategoria() {
    return categoria;
  }

  public void setCategoria(String categoria) {
    this.categoria = categoria;
  }

}
