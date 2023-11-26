package com.mocad.ecommerce.repository;
import com.mocad.ecommerce.model.PessoaFisica;
import com.mocad.ecommerce.model.PessoaJuridica;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PessoaRepository extends CrudRepository<PessoaJuridica, Long> {

    @Query(value = "select pj from PessoaJuridica pj where upper(trim(pj.nome)) like %?1%")
    public List<PessoaJuridica> pesquisaPorNomePJ(String nome);

    @Query(value = "select pj from PessoaJuridica pj where pj.cnpj = ?1")
    public PessoaJuridica existeCnpjCadastrado(String cnpj);

    @Query(value = "select pj from PessoaJuridica pj where pj.cnpj = ?1")
    public List<PessoaJuridica> existeCnpjCadastradoList(String cnpj);

    @Query(value = "select pf from PessoaFisica pf where pf.cpf = ?1")
    public PessoaFisica existeCpfCadastrado(String cpf);

    @Query(value = "select pf from PessoaFisica pf where pf.cpf = ?1")
    public List<PessoaFisica> existeCpfCadastradoList(String cpf);

    @Query(value = "select pj from PessoaJuridica pj where pj.inscEstadual = ?1")
    public PessoaJuridica existeInscEstadualCadastrado(String inscEstadual);

    @Query(value = "select pj from PessoaJuridica pj where pj.inscEstadual = ?1")
    public List<PessoaJuridica> existeInscEstadualCadastradoList(String inscEstadual);

    @Query(value = "select pj from PessoaJuridica pj where pj.id = ?1")
    public PessoaJuridica findPjById(long id);
}
