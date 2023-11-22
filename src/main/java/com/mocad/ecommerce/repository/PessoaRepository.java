package com.mocad.ecommerce.repository;
import com.mocad.ecommerce.model.PessoaJuridica;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PessoaRepository extends CrudRepository<PessoaJuridica, Long> {
    @Query(value = "select pj from PessoaJuridica pj where pj.cnpj = ?1")
    public PessoaJuridica existeCnpjCadastrado(String cnpj);

    @Query(value = "select inscEstadual from PessoaJuridica inscEstadual where pj.inscEstadual = ?1")
    public PessoaJuridica existeInscEstadualCadastrado(String inscEstadual);
}
