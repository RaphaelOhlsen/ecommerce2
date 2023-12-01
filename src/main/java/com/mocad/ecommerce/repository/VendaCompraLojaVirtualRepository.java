package com.mocad.ecommerce.repository;

import com.mocad.ecommerce.model.VendaCompraLojaVirtual;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface VendaCompraLojaVirtualRepository extends JpaRepository<VendaCompraLojaVirtual, Long> {

    @Query("SELECT v FROM VendaCompraLojaVirtual v WHERE v.id = ?1 AND v.excluido IS FALSE")
    VendaCompraLojaVirtual findByIdExclusao(Long id);

    @Modifying
    @Transactional
    @Query("UPDATE VendaCompraLojaVirtual v SET v.excluido = true WHERE v.id = ?1")
    void deleteByIdLogico(Long id);


//    @Query(value="select i.vendaCompraLojaVirtual from ItemVendaLoja i where "
//            + " i.vendaCompraLojaVirtual.excluido = false and i.produto.id = ?1 and i.empresa.id = ?2")
    @Query("SELECT v FROM VendaCompraLojaVirtual v " +
            "JOIN ItemVendaLoja i ON v.id = i.vendaCompraLojaVirtual.id " +
            "WHERE i.produto.id = :idProduto AND i.empresa.id = :idEmpresa")
    List<VendaCompraLojaVirtual> vendaPorProduto(Long idProduto, Long idEmpresa);

}