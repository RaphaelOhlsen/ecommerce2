package com.mocad.ecommerce.repository;

import com.mocad.ecommerce.model.AvaliacaoProduto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AvaliacaoProdutoRepository extends JpaRepository<AvaliacaoProduto, Long> {

    @Query("select a from AvaliacaoProduto a where a.produto.id = ?1")
    List<AvaliacaoProduto> obterAvaliacoesPorProduto(Long idProduto);

    @Query("select a from AvaliacaoProduto a where a.produto.id = ?1 and a.pessoa.id = ?2")
    List<AvaliacaoProduto> obterAvaliacoesPorProdutoPessoa(Long idProduto, Long idPessoa);

    @Query("select a from AvaliacaoProduto a where a.pessoa.id = ?1")
    List<AvaliacaoProduto> obterAvaliacoesPorPessoa(Long idPessoa);

}
