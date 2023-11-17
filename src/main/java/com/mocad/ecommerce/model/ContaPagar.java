package com.mocad.ecommerce.model;


import com.mocad.ecommerce.enums.StatusContaPagar;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "conta_pagar")
@SequenceGenerator(name = "seq_conta_pagar", sequenceName = "seq_conta_pagar", allocationSize = 1, initialValue = 1)
public class ContaPagar implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_conta_pagar")
  private Long id;

  @Column(nullable = false)
  private String descricao;

  @Column(nullable = false)
  private BigDecimal valorTotal;

  private BigDecimal valorDesconto;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private StatusContaPagar status;

  @Column(nullable = false)
  @Temporal(TemporalType.DATE)
  private Date dtVencimento;

  @Temporal(TemporalType.DATE)
  private Date dtPagamento;

  @ManyToOne(targetEntity = Pessoa.class)
  @JoinColumn(name = "pessoa_id", nullable = false, foreignKey = @ForeignKey(
          value = ConstraintMode.CONSTRAINT, name= "pessoa_fk"))
  private Pessoa pessoa;

  @ManyToOne(targetEntity = Pessoa.class)
  @JoinColumn(name = "pessoa_forn_id", nullable = false, foreignKey = @ForeignKey(
          value = ConstraintMode.CONSTRAINT, name= "pessoa_forn_fk"))
  private Pessoa pessoa_fornecedor;

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

  public String getDescricao() {
    return descricao;
  }

  public void setDescricao(String descricao) {
    this.descricao = descricao;
  }

  public BigDecimal getValorTotal() {
    return valorTotal;
  }

  public void setValorTotal(BigDecimal valorTotal) {
    this.valorTotal = valorTotal;
  }

  public BigDecimal getValorDesconto() {
    return valorDesconto;
  }

  public void setValorDesconto(BigDecimal valorDesconto) {
    this.valorDesconto = valorDesconto;
  }

  public StatusContaPagar getStatus() {
    return status;
  }

  public void setStatus(StatusContaPagar status) {
    this.status = status;
  }

  public Date getDtVencimento() {
    return dtVencimento;
  }

  public void setDtVencimento(Date dtVencimento) {
    this.dtVencimento = dtVencimento;
  }

  public Date getDtPagamento() {
    return dtPagamento;
  }

  public void setDtPagamento(Date dtPagamento) {
    this.dtPagamento = dtPagamento;
  }

  public Pessoa getPessoa() {
    return pessoa;
  }

  public void setPessoa(Pessoa pessoa) {
    this.pessoa = pessoa;
  }

  public Pessoa getPessoa_fornecedor() {
    return pessoa_fornecedor;
  }

  public void setPessoa_fornecedor(Pessoa pessoa_fornecedor) {
    this.pessoa_fornecedor = pessoa_fornecedor;
  }

  public Pessoa getEmpresa() {
    return empresa;
  }

  public void setEmpresa(Pessoa empresa) {
    this.empresa = empresa;
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
    ContaPagar other = (ContaPagar) obj;
    if (id == null) {
      if (other.id != null) return false;
    } else if (!id.equals(other.id))
      return false;
    return true;
  }
}
