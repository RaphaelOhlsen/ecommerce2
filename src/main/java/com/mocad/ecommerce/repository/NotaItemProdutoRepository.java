package com.mocad.ecommerce.repository;

import com.mocad.ecommerce.model.NotaItemProduto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface NotaItemProdutoRepository extends JpaRepository<NotaItemProduto, Long> {

    @Query("select a from NotaItemProduto a where a.produto.id = ?1 and a.notaFiscalCompra.id = ?2 and a.empresa.id = ?3")
    List<NotaItemProduto> buscaNotaItemPorProdutoNota(Long idProduto, Long IdNotaFiscal, Long idEmpresa);

    @Query("select a from NotaItemProduto a where a.produto.id = ?1 and a.empresa.id = ?2")
    List<NotaItemProduto> buscaNotaItemPorProduto(Long idProduto, Long idEmpresa);

    @Query("select a from NotaItemProduto a where a.notaFiscalCompra.id = ?1 and a.empresa.id = ?2")
    List<NotaItemProduto> buscaNotaItemPorNotaFiscal(Long idNotaFiscalCompra, Long idEmpresa);

    @Query("select a from NotaItemProduto a where a.empresa.id = ?1")
    List<NotaItemProduto> buscaNotaItemPorEmpresa(Long idEmpresa);
}
