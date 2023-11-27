package com.mocad.ecommerce.repository;

import com.mocad.ecommerce.model.MarcaProduto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MarcaProdutoRespository extends JpaRepository<MarcaProduto, Long> {

    @Query(nativeQuery = true, value = "select count(1) > 0 from marca_produto where upper(trim(nome_desc)) = upper(trim(?1)) and empresa_id = ?2 ")
    public boolean existeMarca(String nomeMarca, Long empresa_id);

    @Query("select a from MarcaProduto a where upper(trim(a.nomeDesc)) like %?1% and empresa_id = ?2")
    public List<MarcaProduto> buscarMarcaPorNome(String nome, Long idEmpresa);
}
