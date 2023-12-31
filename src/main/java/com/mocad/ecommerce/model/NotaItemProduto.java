package com.mocad.ecommerce.model;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "nota_item_produto")
@SequenceGenerator(name = "seq_nota_item_produto", sequenceName = "seq_nota_item_produto", allocationSize = 1, initialValue = 1)
public class NotaItemProduto implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_nota_item_produto")
  private Long id;

  @DecimalMin(value = "1", message = "Quantidade deve ser maior que zero")
  @NotNull(message = "Quantidade deve ser informada")
  @Column(nullable = false)
  private Double quantidade;

  @ManyToOne
  @JoinColumn(name = "produto_id", nullable = false,
          foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "produto_fk"))
  private Produto produto;

  @ManyToOne
  @JoinColumn(name = "nota_fiscal_compra_id", nullable = false,
          foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "nota_fiscal_compra_fk"))
  private NotaFiscalCompra notaFiscalCompra;

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

  public Double getQuantidade() {
    return quantidade;
  }

  public void setQuantidade(Double quantidade) {
    this.quantidade = quantidade;
  }

  public Produto getProduto() {
    return produto;
  }

  public void setProduto(Produto produto) {
    this.produto = produto;
  }

  public NotaFiscalCompra getNotaFiscalCompra() {
    return notaFiscalCompra;
  }

  public void setNotaFiscalCompra(NotaFiscalCompra notaFiscalCompra) {
    this.notaFiscalCompra = notaFiscalCompra;
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
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    NotaItemProduto other = (NotaItemProduto) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    return true;
  }
}
