package com.mocad.ecommerce.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "produto")
@SequenceGenerator(name = "seq_produto", sequenceName = "seq_produto", allocationSize = 1, initialValue = 1)
public class Produto implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_produto")
  private Long id;

  @NotNull(message = "O tipo de unidade é obrigatório")
  @Column(nullable = false)
  private String tipoUnidade;

  @Size(min = 10, message = "O nome do produto deve ter no mínimo 10 caracteres")
  @NotNull(message = "O nome do produto é obrigatório")
  @Column(nullable = false)
  private String nome;

  @NotNull(message = "O campo ativo é obrigatório")
  @Column(nullable = false)
  private Boolean ativo = Boolean.TRUE;

  @NotNull(message = "A descrição do produto é obrigatório")
  @Column(columnDefinition = "text", length = 2000, nullable = false)
  private String descricao;

  @NotNull(message = "O peso do produto é obrigatório")
  @Column(nullable = false)
  private Double peso;

  @NotNull(message = "A largura do produto é obrigatório")
  @Column(nullable = false)
  private Double largura;

  @NotNull(message = "A altura do produto é obrigatório")
  @Column(nullable = false)
  private Double altura;

  @NotNull(message = "A profundidade do produto é obrigatório")
  @Column(nullable = false)
  private Double profundidade;

  @NotNull(message = "O valor de venda do produto é obrigatório")
  @Column(nullable = false)
  private BigDecimal valorVenda = BigDecimal.ZERO;

  @NotNull(message = "A quantidade em estoque do produto é obrigatório")
  @Column(nullable = false)
  private Integer qtdEstoque = 0;

  private Integer qtdAlertaEstoque = 0;

  private String linkYoutube;

  private Boolean alertaQtdEstoque = Boolean.FALSE;

  private Integer qtdClique = 0;


  @ManyToOne(targetEntity = Pessoa.class)
  @JoinColumn(name = "empresa_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "empresa_id_fk"))
  private PessoaJuridica empresa;


  @ManyToOne(targetEntity = CategoriaProduto.class)
  @JoinColumn(name = "categoria_produto_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "categoria_produto_id_fk"))
  private CategoriaProduto categoriaProduto;


  @ManyToOne(targetEntity = MarcaProduto.class)
  @JoinColumn(name = "marca_produto_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "marca_produto_id_fk"))
  private MarcaProduto marcaProduto;

  @OneToMany(mappedBy = "produto", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<ImagemProduto> imagens;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTipoUnidade() {
    return tipoUnidade;
  }

  public void setTipoUnidade(String tipoUnidade) {
    this.tipoUnidade = tipoUnidade;
  }

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public Boolean getAtivo() {
    return ativo;
  }

  public void setAtivo(Boolean ativo) {
    this.ativo = ativo;
  }

  public String getDescricao() {
    return descricao;
  }

  public void setDescricao(String descricao) {
    this.descricao = descricao;
  }

  public Double getPeso() {
    return peso;
  }

  public void setPeso(Double peso) {
    this.peso = peso;
  }

  public Double getLargura() {
    return largura;
  }

  public void setLargura(Double largura) {
    this.largura = largura;
  }

  public Double getAltura() {
    return altura;
  }

  public void setAltura(Double altura) {
    this.altura = altura;
  }

  public Double getProfundidade() {
    return profundidade;
  }

  public void setProfundidade(Double profundidade) {
    this.profundidade = profundidade;
  }

  public BigDecimal getValorVenda() {
    return valorVenda;
  }

  public void setValorVenda(BigDecimal valorVenda) {
    this.valorVenda = valorVenda;
  }

  public Integer getQtdEstoque() {
    return qtdEstoque;
  }

  public void setQtdEstoque(Integer qtdEstoque) {
    this.qtdEstoque = qtdEstoque;
  }

  public Integer getQtdAlertaEstoque() {
    return qtdAlertaEstoque;
  }

  public void setQtdAlertaEstoque(Integer qtdAlertaEstoque) {
    this.qtdAlertaEstoque = qtdAlertaEstoque;
  }

  public String getLinkYoutube() {
    return linkYoutube;
  }

  public void setLinkYoutube(String linkYoutube) {
    this.linkYoutube = linkYoutube;
  }

  public Boolean getAlertaQtdEstoque() {
    return alertaQtdEstoque;
  }

  public void setAlertaQtdEstoque(Boolean alertaQtdEstoque) {
    this.alertaQtdEstoque = alertaQtdEstoque;
  }

  public Integer getQtdClique() {
    return qtdClique;
  }

  public void setQtdClique(Integer qtdClique) {
    this.qtdClique = qtdClique;
  }

  public PessoaJuridica getEmpresa() {
    return empresa;
  }

  public void setEmpresa(PessoaJuridica empresa) {
    this.empresa = empresa;
  }

  public CategoriaProduto getCategoriaProduto() {
    return categoriaProduto;
  }

  public void setCategoriaProduto(CategoriaProduto categoriaProduto) {
    this.categoriaProduto = categoriaProduto;
  }

  public MarcaProduto getMarcaProduto() {
    return marcaProduto;
  }

  public void setMarcaProduto(MarcaProduto marcaProduto) {
    this.marcaProduto = marcaProduto;
  }

  public List<ImagemProduto> getImagens() {
    return imagens;
  }

  public void setImagens(List<ImagemProduto> imagens) {
    this.imagens = imagens;
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
    Produto other = (Produto) obj;
    if (id == null) {
      if (other.id != null) return false;
    } else if (!id.equals(other.id))
      return false;
    return true;
  }
}
