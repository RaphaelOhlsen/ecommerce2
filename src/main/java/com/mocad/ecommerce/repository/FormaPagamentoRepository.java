package com.mocad.ecommerce.repository;

import com.mocad.ecommerce.model.FormaPagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FormaPagamentoRepository extends JpaRepository<FormaPagamento, Long> {


    @Query("select f from FormaPagamento f where upper(trim(f.descricao)) like %?1% and f.empresa.id = ?2")
    List<FormaPagamento> buscarFormaPagamentoPorDesc(String descricao, Long empresaId);

    @Query("select f from FormaPagamento f where f.empresa.id = ?1")
    List<FormaPagamento> buscarFormaPagamentoPorEmpresa(Long empresaId);
}