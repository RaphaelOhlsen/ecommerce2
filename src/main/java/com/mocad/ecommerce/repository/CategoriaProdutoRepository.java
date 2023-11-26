package com.mocad.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mocad.ecommerce.model.CategoriaProduto;

import java.util.List;

@Repository
public interface CategoriaProdutoRepository extends JpaRepository<CategoriaProduto, Long> {

    @Query(nativeQuery = true, value = "select count(1) > 0 from categoria_produto where upper(trim(nome_desc)) = upper(trim(?1)) and empresa_id = ?2 ")
    public boolean existeCategoria(String nomeCategoria, Long empresa_id);

    @Query("select a from CategoriaProduto a where upper(trim(a.nomeDesc)) like %?1% and empresa.id = ?2")
    public List<CategoriaProduto> buscarCategoriaDesc(String nomeDesc, Long empresa_id);


}
