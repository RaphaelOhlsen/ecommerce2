package com.mocad.ecommerce.model;


import com.mocad.ecommerce.enums.StatusContaPagar;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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

  @NotNull(message = "Descrição da conta é obrigatória")
  @Column(nullable = false)
  private String descricao;


  @NotNull(message = "Valor total da conta é obrigatório")
  @Column(nullable = false)
  private BigDecimal valorTotal;

  private BigDecimal valorDesconto;


  @NotNull(message = "Status da conta é obrigatório")
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
  private PessoaFisica pessoaFisica;

  @ManyToOne(targetEntity = Pessoa.class)
  @JoinColumn(name = "pessoa_forn_id", nullable = false, foreignKey = @ForeignKey(
          value = ConstraintMode.CONSTRAINT, name= "pessoa_forn_fk"))
  private PessoaJuridica pessoaFornecedor;

  @ManyToOne(targetEntity = Pessoa.class)
  @JoinColumn(name = "empresa_id", nullable = false,
          foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "empresa_id_fk"))
  private PessoaJuridica empresa;

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

  public PessoaFisica getPessoaFisica() {
    return pessoaFisica;
  }

  public void setPessoaFisica(PessoaFisica pessoaFisica) {
    this.pessoaFisica = pessoaFisica;
  }

  public PessoaJuridica getPessoaFornecedor() {
    return pessoaFornecedor;
  }

  public void setPessoaFornecedor(PessoaJuridica pessoaFornecedor) {
    this.pessoaFornecedor = pessoaFornecedor;
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
