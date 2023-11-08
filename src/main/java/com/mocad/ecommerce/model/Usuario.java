package com.mocad.ecommerce.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "usuario")
@SequenceGenerator(name = "seq_usuario", sequenceName = "seq_usuario", allocationSize = 1, initialValue = 1)
public class Usuario implements UserDetails {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_usuario")
  private Long id;

  @Column(nullable = false, unique = true)
  private String login;

  @Column(nullable = false)
  private String senha;

  @Column(nullable = false)
  @Temporal(TemporalType.DATE)
  private Date dataAtualSenha;

  @ManyToOne(targetEntity = Pessoa.class)
  @JoinColumn(name = "pessoa_id", nullable = false,
          foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "pessoa_fk"))
  private Pessoa pessoa;

  @OneToMany(fetch = FetchType.LAZY)
  @JoinTable(
          name = "usuario_acesso",
          uniqueConstraints = @UniqueConstraint(
                  columnNames = {"usuario_id", "acesso_id"},
                  name = "unique_acesso_user"
          ),
         joinColumns = @JoinColumn(
                 name = "usuario_id",
                 referencedColumnName = "id",
                 table = "usuario",
                 unique = false,
                 foreignKey = @ForeignKey(
                         name = "usuario_fk",
                         value = ConstraintMode.CONSTRAINT
                 )
          ),
          inverseJoinColumns = @JoinColumn(
                  name = "acesso_id",
                  referencedColumnName = "id",
                  table = "acesso",
                  unique = false,
                  foreignKey = @ForeignKey(
                          name = "acesso_fk",
                          value = ConstraintMode.CONSTRAINT
                  )
          )
  )
  private List<Acesso> acessos;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return this.acessos;
  }

  @Override
  public String getPassword() {
    return this.senha;
  }

  @Override
  public String getUsername() {
    return this.login;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getLogin() {
    return login;
  }

  public void setLogin(String login) {
    this.login = login;
  }

  public String getSenha() {
    return senha;
  }

  public void setSenha(String senha) {
    this.senha = senha;
  }

  public Date getDataAtualSenha() {
    return dataAtualSenha;
  }

  public void setDataAtualSenha(Date dataAtualSenha) {
    this.dataAtualSenha = dataAtualSenha;
  }

  public Pessoa getPessoa() {
    return pessoa;
  }

  public void setPessoa(Pessoa pessoa) {
    this.pessoa = pessoa;
  }

  public List<Acesso> getAcessos() {
    return acessos;
  }

  public void setAcessos(List<Acesso> acessos) {
    this.acessos = acessos;
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
    Usuario other = (Usuario) obj;
    if (id == null) {
      if (other.id != null) return false;
    } else if (!id.equals(other.id))
      return false;
    return true;
  }
}
