package com.mocad.ecommerce.repository;

import com.mocad.ecommerce.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    @Query(nativeQuery = true, value = "select count(1) > 0 produto where upper(trim(nome)) = upper(trim(?1)) and empresa_id = ?2 ")
    public boolean existeProduto(String nomeCategoria, Long empresa_id);

    @Query("select a from Produto a where upper(trim(a.nome)) like %?1% and empresa.id = ?2")
    public List<Produto> buscarProdutoNome(String nome, Long empresa_id);


}
