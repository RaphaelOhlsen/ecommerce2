package com.mocad.ecommerce.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mocad.ecommerce.enums.TipoEndereco;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "endereco")
@SequenceGenerator(name = "seq_endereco", sequenceName = "seq_endereco", allocationSize = 1, initialValue = 1)
public class Endereco implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_endereco")
  private Long id;

  @Column(nullable = false)
  private String ruaLogra;

  @Column(nullable = false)
  private String cep;

  @Column(nullable = false)
  private String numero;

  private String complemento;

  @Column(nullable = false)
  private String bairro;

  @Column(nullable = false)
  private String cidade;

//  @Column(nullable = false)
  private String estado;

  @Column(nullable = false)
  private String uf;

  @Column
  private String country_id;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private TipoEndereco tipoEndereco;

  @JsonIgnore
  @ManyToOne(targetEntity = Pessoa.class)
  @JoinColumn(name = "pessoa_id", nullable = false, foreignKey = @ForeignKey(
          value = ConstraintMode.CONSTRAINT, name= "pessoa_fk"))
  private Pessoa pessoa;

  @JsonIgnore
  @ManyToOne(targetEntity = Pessoa.class)
  @JoinColumn(name = "empresa_id", nullable = false,
          foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "empresa_id_fk"))
  private Pessoa empresa;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getRuaLogra() {
    return ruaLogra;
  }

  public void setRuaLogra(String ruaLogra) {
    this.ruaLogra = ruaLogra;
  }

  public String getCep() {
    return cep;
  }

  public void setCep(String cep) {
    this.cep = cep;
  }

  public String getNumero() {
    return numero;
  }

  public void setNumero(String numero) {
    this.numero = numero;
  }

  public String getComplemento() {
    return complemento;
  }

  public void setComplemento(String complemento) {
    this.complemento = complemento;
  }

  public String getBairro() {
    return bairro;
  }

  public void setBairro(String bairro) {
    this.bairro = bairro;
  }

  public String getCidade() {
    return cidade;
  }

  public void setCidade(String cidade) {
    this.cidade = cidade;
  }

  public String getUf() {
    return uf;
  }

  public void setUf(String uf) {
    this.uf = uf;
  }

  public TipoEndereco getTipoEndereco() {
    return tipoEndereco;
  }

  public void setTipoEndereco(TipoEndereco tipoEndereco) {
    this.tipoEndereco = tipoEndereco;
  }

  public Pessoa getPessoa() {
    return pessoa;
  }

  public void setPessoa(Pessoa pessoa) {
    this.pessoa = pessoa;
  }

  public Pessoa getEmpresa() {
    return empresa;
  }

  public void setEmpresa(Pessoa empresa) {
    this.empresa = empresa;
  }

  public String getEstado() {
    return estado;
  }

  public void setEstado(String estado) {
    this.estado = estado;
  }

  public String getCountry_id() {
    return country_id;
  }

  public void setCountry_id(String country_id) {
    this.country_id = country_id;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
   return  prime * result + ((id == null) ? 0 : id.hashCode());
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    Endereco other = (Endereco) obj;
    if (id == null) {
      if (other.id != null) return false;
    } else if (!id.equals(other.id))
      return false;
    return true;
  }
}
