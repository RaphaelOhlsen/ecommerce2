package com.mocad.ecommerce.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@SequenceGenerator(name = "seq_pessoa", sequenceName = "seq_pessoa", allocationSize = 1, initialValue = 1)
public abstract class Pessoa implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_pessoa")
  private Long id;

  @Column(nullable = false)
  private String nome;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false)
  private String telefone;
  
  @Column
  private String tipoPessoa;

  @OneToMany(mappedBy = "pessoa", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<Endereco> enderecos = new ArrayList<Endereco>();

  @ManyToOne(targetEntity = Pessoa.class)
  @JoinColumn(name = "empresa_id", nullable = true,
          foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "empresa_id_fk"))
  private Pessoa empresa;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getTelefone() {
    return telefone;
  }

  public void setTelefone(String telefone) {
    this.telefone = telefone;
  }
  
  public String getTipoPessoa() {
	return tipoPessoa;
}

public void setTipoPessoa(String tipoPessoa) {
	this.tipoPessoa = tipoPessoa;
}

public static long getSerialversionuid() {
	return serialVersionUID;
}

public List<Endereco> getEnderecos() {
    return enderecos;
  }

public void setEnderecos(List<Endereco> enderecos) {
    this.enderecos = enderecos;
  }

public Pessoa getEmpresa() {
  return empresa;
}

public void setEmpresa(Pessoa empresa) {
  this.empresa = empresa;
}

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    Pessoa other = (Pessoa) obj;
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
