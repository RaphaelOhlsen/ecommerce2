package com.mocad.ecommerce.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "vd_cp_loja_virt")
@SequenceGenerator(name = "seq_vd_cp_loja_virt", sequenceName = "seq_vd_cp_loja_virt", allocationSize = 1, initialValue = 1)
public class VendaCompraLojaVirtual implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_vd_cp_loja_virt")
  private Long id;

  @Column(nullable = false)
  private BigDecimal valorTotal;

  private BigDecimal valorDesconto;

  @Column(nullable = false)
  private BigDecimal valorFrete;

  @Column(nullable = false)
  private int dias_entrega;

  @Column(nullable = false)
  @Temporal(TemporalType.DATE)
  private Date dataVenda;

  @Column(nullable = false)
  @Temporal(TemporalType.DATE)
  private Date dataEntrega;

  @ManyToOne(targetEntity = Pessoa.class)
  @JoinColumn(name = "pessoa_id", nullable = false, foreignKey = @ForeignKey(
          value = ConstraintMode.CONSTRAINT, name= "pessoa_fk"))
  private Pessoa pessoa;

  @ManyToOne(targetEntity = Pessoa.class)
  @JoinColumn(name = "empresa_id", nullable = false,
          foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "empresa_id_fk"))
  private Pessoa empresa;

  @ManyToOne
  @JoinColumn(name = "endereco_entrega_id", nullable = false, foreignKey = @ForeignKey(
          value = ConstraintMode.CONSTRAINT, name = "endereco_entrega_fk"))
  private Endereco enderecoEntrega;

  @ManyToOne
  @JoinColumn(name = "endereco_cobranca_id", nullable = false, foreignKey = @ForeignKey(
          value = ConstraintMode.CONSTRAINT, name = "endereco_cobranca_fk"))
  private Endereco enderecoCobranca;

  @OneToOne
  @JoinColumn(name = "nota_fiscal_venda_id", nullable = false, foreignKey = @ForeignKey(
          value = ConstraintMode.CONSTRAINT, name = "nota_fiscal_venda_fk"))
  private NotaFiscalVenda notaFiscalVenda;

  @ManyToOne
  @JoinColumn(name = "cup_desc_id", foreignKey = @ForeignKey(
          value = ConstraintMode.CONSTRAINT, name = "cup_desc_fk"))
  private CupDesc cupDesc;

  @ManyToOne
  @JoinColumn(name = "forma_pagamento_id", nullable = false, foreignKey = @ForeignKey(
          value = ConstraintMode.CONSTRAINT, name = "forma_pagamento_fk"))
  private FormaPagamento formapagamento;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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

  public BigDecimal getValorFrete() {
    return valorFrete;
  }

  public void setValorFrete(BigDecimal valorFrete) {
    this.valorFrete = valorFrete;
  }

  public int getDias_entrega() {
    return dias_entrega;
  }

  public void setDias_entrega(int dias_entrega) {
    this.dias_entrega = dias_entrega;
  }

  public Date getDataVenda() {
    return dataVenda;
  }

  public void setDataVenda(Date dataVenda) {
    this.dataVenda = dataVenda;
  }

  public Date getDataEntrega() {
    return dataEntrega;
  }

  public void setDataEntrega(Date dataEntrega) {
    this.dataEntrega = dataEntrega;
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

  public Endereco getEnderecoEntrega() {
    return enderecoEntrega;
  }

  public void setEnderecoEntrega(Endereco enderecoEntrega) {
    this.enderecoEntrega = enderecoEntrega;
  }

  public Endereco getEnderecoCobranca() {
    return enderecoCobranca;
  }

  public void setEnderecoCobranca(Endereco enderecoCobranca) {
    this.enderecoCobranca = enderecoCobranca;
  }

  public NotaFiscalVenda getNotaFiscalVenda() {
    return notaFiscalVenda;
  }

  public void setNotaFiscalVenda(NotaFiscalVenda notaFiscalVenda) {
    this.notaFiscalVenda = notaFiscalVenda;
  }

  public CupDesc getCupDesc() {
    return cupDesc;
  }

  public void setCupDesc(CupDesc cupDesc) {
    this.cupDesc = cupDesc;
  }

  public FormaPagamento getFormapagamento() {
    return formapagamento;
  }

  public void setFormapagamento(FormaPagamento formapagamento) {
    this.formapagamento = formapagamento;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    return prime * result + ((id == null) ? 0 : id.hashCode());
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null || getClass() != obj.getClass())
      return false;
    VendaCompraLojaVirtual other = (VendaCompraLojaVirtual) obj;
    if (id == null) {
      if (other.id != null) return false;
    } else if (!id.equals(other.id))
      return false;
    return true;
  }
}
