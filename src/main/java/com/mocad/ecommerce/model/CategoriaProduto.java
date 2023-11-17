package com.mocad.ecommerce.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "categoria_produto")
@SequenceGenerator(name = "seq_categoria_produto", sequenceName = "seq_categoria_produto",
        allocationSize = 1, initialValue = 1)
public class CategoriaProduto implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_categoria_produto")
  private Long id;

  @Column(name = "nome_desc", nullable = false)
  private String nomeDesc;

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

  public String getNomeDesc() {
    return nomeDesc;
  }

  public void setNomeDesc(String nomeDesc) {
    this.nomeDesc = nomeDesc;
  }

  public Pessoa getEmpresa() {
    return empresa;
  }

  public void setEmpresa(Pessoa empresa) {
    this.empresa = empresa;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    CategoriaProduto other = (CategoriaProduto) o;
    if (id == null) {
      if (other.id != null) return false;
    } else if (!id.equals(other.id))
      return false;
    return true;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    return prime * result + ((id == null) ? 0 : id.hashCode());
  }
}
