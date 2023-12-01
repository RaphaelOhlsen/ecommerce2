package com.mocad.ecommerce.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "vd_cp_loja_virt")
@SequenceGenerator(name = "seq_vd_cp_loja_virt", sequenceName = "seq_vd_cp_loja_virt", allocationSize = 1, initialValue = 1)
public class VendaCompraLojaVirtual implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_vd_cp_loja_virt")
  private Long id;

  @DecimalMin(value = "1", inclusive = false, message = "O valor total deve ser maior que 1")
  @Column(nullable = false)
  private BigDecimal valorTotal;

  private BigDecimal valorDesconto;

  @DecimalMin(value = "1", inclusive = false, message = "O valor do frete deve ser maior que 1")
  @Column(nullable = false)
  private BigDecimal valorFrete;

  @Min(value = 1, message = "O valor de dias de entrega deve ser maior que 1")
  @Column(nullable = false)
  private int diasEntrega;

  @NotNull(message = "O campo data de venda é obrigatório")
  @Column(nullable = false)
  @Temporal(TemporalType.DATE)
  private Date dataVenda;

  @NotNull(message = "O campo data de entrega é obrigatório")
  @Column(nullable = false)
  @Temporal(TemporalType.DATE)
  private Date dataEntrega;

  @NotNull(message = "O campo pessoa é obrigatório")
  @ManyToOne(targetEntity = PessoaFisica.class, cascade = CascadeType.ALL)
  @JoinColumn(name = "pessoa_id", nullable = false, foreignKey = @ForeignKey(
          value = ConstraintMode.CONSTRAINT, name= "pessoa_fk"))
  private PessoaFisica pessoa;

  @NotNull(message = "O campo empresa é obrigatório")
  @ManyToOne(targetEntity = Pessoa.class)
  @JoinColumn(name = "empresa_id", nullable = false,
          foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "empresa_id_fk"))
  private PessoaJuridica empresa;

  @NotNull(message = "O campo endereço de entrega é obrigatório")
  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "endereco_entrega_id", nullable = false, foreignKey = @ForeignKey(
          value = ConstraintMode.CONSTRAINT, name = "endereco_entrega_fk"))
  private Endereco enderecoEntrega;

  @NotNull(message = "O campo endereço de cobrança é obrigatório")
  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "endereco_cobranca_id", nullable = false, foreignKey = @ForeignKey(
          value = ConstraintMode.CONSTRAINT, name = "endereco_cobranca_fk"))
  private Endereco enderecoCobranca;

  @JsonIgnoreProperties(allowGetters = true)
  @NotNull(message = "O campo nota fiscal é obrigatório")
  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "nota_fiscal_venda_id", nullable = true, foreignKey = @ForeignKey(
          value = ConstraintMode.CONSTRAINT, name = "nota_fiscal_venda_fk"))
  private NotaFiscalVenda notaFiscalVenda;

  @ManyToOne
  @JoinColumn(name = "cup_desc_id", foreignKey = @ForeignKey(
          value = ConstraintMode.CONSTRAINT, name = "cup_desc_fk"))
  private CupDesc cupDesc;


  @NotNull(message = "O campo forma de pagamento é obrigatório")
  @ManyToOne
  @JoinColumn(name = "forma_pagamento_id", nullable = false, foreignKey = @ForeignKey(
          value = ConstraintMode.CONSTRAINT, name = "forma_pagamento_fk"))
  private FormaPagamento formaPagamento;

  @OneToMany(mappedBy = "vendaCompraLojaVirtual", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  private List<ItemVendaLoja> itemVendaLojas = new ArrayList<ItemVendaLoja>();

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

  public int getDiasEntrega() {
    return diasEntrega;
  }

  public void setDiasEntrega(int diasEntrega) {
    this.diasEntrega = diasEntrega;
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

  public PessoaFisica getPessoa() {
    return pessoa;
  }

  public void setPessoa(PessoaFisica pessoa) {
    this.pessoa = pessoa;
  }

  public PessoaJuridica getEmpresa() {
    return empresa;
  }

  public void setEmpresa(PessoaJuridica empresa) {
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

  public FormaPagamento getFormaPagamento() {
    return formaPagamento;
  }

  public void setFormaPagamento(FormaPagamento formapagamento) {
    this.formaPagamento = formapagamento;
  }

  public List<ItemVendaLoja> getItemVendaLojas() {
    return itemVendaLojas;
  }

  public void setItemVendaLojas(List<ItemVendaLoja> itemVendaLojas) {
    this.itemVendaLojas = itemVendaLojas;
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
