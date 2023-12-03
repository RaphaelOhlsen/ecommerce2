package com.mocad.ecommerce.repository;

import com.mocad.ecommerce.model.NotaFiscalVenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotaFiscalVendaRepository extends JpaRepository<NotaFiscalVenda, Long> {

    @Query("SELECT n FROM NotaFiscalVenda n WHERE n.vendaCompraLojaVirtual.id = ?1 and n.empresa.id = ?2")
    List<NotaFiscalVenda> buscaNotaPorVenda(Long idVenda, Long idEmpresa);

    @Query("SELECT n FROM NotaFiscalVenda n WHERE n.vendaCompraLojaVirtual.id = ?1 and n.empresa.id = ?2")
    NotaFiscalVenda buscaNotaPorVendaUnica(Long idVenda, Long idEmpresa);

}
