package com.mocad.ecommerce.repository;

import com.mocad.ecommerce.model.ImagemProduto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface ImagemProdutoRepository extends JpaRepository<ImagemProduto, Long> {

    @Query("SELECT a FROM ImagemProduto a WHERE a.produto.id = ?1")
    List<ImagemProduto> buscaImagemProduto (Long idProduto);

    @Transactional
    @Modifying(flushAutomatically = true)
    @Query("DELETE FROM ImagemProduto a WHERE a.produto.id = ?1")
    void apagarImagensPorProduto(Long idProduto);
}
