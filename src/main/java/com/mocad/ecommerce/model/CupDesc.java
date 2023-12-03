package com.mocad.ecommerce.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "cup_desc")
@SequenceGenerator(name = "seq_cup_desc", sequenceName = "seq_cup_desc", allocationSize = 1, initialValue = 1)
public class CupDesc implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_cup_desc")
  private Long id;

  @Column(nullable = false)
  private String codDesc;

  @ManyToOne(targetEntity = Pessoa.class)
  @JoinColumn(name = "empresa_id", nullable = false,
          foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "empresa_id_fk"))
  private PessoaJuridica empresa;

  private BigDecimal valorRealDesc;

  private BigDecimal valorPorcentDesc;

  @Column(nullable = false)
  @Temporal(TemporalType.DATE)
  private Date dataValidadeCupom;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getCodDesc() {
    return codDesc;
  }

  public void setCodDesc(String codDesc) {
    this.codDesc = codDesc;
  }

  public BigDecimal getValorRealDesc() {
    return valorRealDesc;
  }

  public void setValorRealDesc(BigDecimal valorRealDesc) {
    this.valorRealDesc = valorRealDesc;
  }

  public BigDecimal getValorPorcentDesc() {
    return valorPorcentDesc;
  }

  public void setValorPorcentDesc(BigDecimal valorPorcentDesc) {
    this.valorPorcentDesc = valorPorcentDesc;
  }

  public Date getDataValidadeCupom() {
    return dataValidadeCupom;
  }

  public void setDataValidadeCumpom(Date dataValidadeCupom) {
    this.dataValidadeCupom = dataValidadeCupom;
  }

  public void setDataValidadeCupom(Date dataValidadeCupom) {
    this.dataValidadeCupom = dataValidadeCupom;
  }

  public PessoaJuridica getEmpresa() {
    return empresa;
  }

  public void setEmpresa(PessoaJuridica empresa) {
    this.empresa = empresa;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    return  prime * result + ((id == null) ? 0 : id.hashCode());
  }

  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    CupDesc other = (CupDesc) obj;
    if (id == null) {
      if (other.id != null) return false;
    } else if (!id.equals(other.id))
      return false;
    return true;
  }
}
