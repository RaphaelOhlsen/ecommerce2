package com.mocad.ecommerce.repository;

import com.mocad.ecommerce.model.ContaPagar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface ContaPagarRepository extends JpaRepository<ContaPagar, Long > {

    @Query("select a from ContaPagar a where upper(trim(a.descricao)) like %?1%")
    List<ContaPagar> buscaContaDesc(String desc);

    @Query("select a from ContaPagar a where upper(trim(a.descricao)) like %?1% and a.empresa.id = ?2")
    List<ContaPagar> buscaContaDesc(String desc, Long idEmpresa);

    @Query("select a from ContaPagar a where a.pessoaFisica.id = ?1")
    List<ContaPagar> buscaContasPorPessoa(Long idPessoa);

    @Query("select a from ContaPagar a where a.pessoaFornecedor.id = ?1")
    List<ContaPagar> buscaContasPorFornecedor(Long idFornecedor, Long idEmpresa);

    @Query("select a from ContaPagar a where a.empresa.id = ?1")
    List<ContaPagar> buscaContaPorEmpresaId(Long idEmpresa);

}
