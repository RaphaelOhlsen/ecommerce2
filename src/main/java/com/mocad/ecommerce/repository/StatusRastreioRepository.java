package com.mocad.ecommerce.repository;

import com.mocad.ecommerce.model.StatusRastreio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatusRastreioRepository extends JpaRepository<StatusRastreio, Long> {

   // Quebrar linha para melhor visualização

    @Query("SELECT s FROM StatusRastreio s WHERE s.vendaCompraLojaVirtual.id = ?1 "
            + "AND s.vendaCompraLojaVirtual.empresa.id = ?2 order by s.id")
    List<StatusRastreio>  statusRastreioPorVenda(Long idVenda, Long idEmpresa);
}
