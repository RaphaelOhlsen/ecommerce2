package com.mocad.ecommerce.repository;

import com.mocad.ecommerce.model.VendaCompraLojaVirtual;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
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


    @Query(value="select distinct(i.vendaCompraLojaVirtual) from ItemVendaLoja i "
            + " where i.vendaCompraLojaVirtual.excluido = false and upper(trim(i.produto.nome)) like %?1% and i.empresa.id = ?2")
    List<VendaCompraLojaVirtual> vendaPorNomeProduto(String valor, Long idEmpresa);

    // selecionar na tabela venda_compra_loja_virtual, onde o nome do cliente seja igual ao valor passado por parametro
    @Query(value="select distinct(v) from VendaCompraLojaVirtual v "
            + " where v.excluido = false and upper(trim(v.pessoa.nome)) like %?1% and v.empresa.id = ?2")
    List<VendaCompraLojaVirtual> vendaPorNomeCliente(String valor, Long idEmpresa);

    //selecionar da tabela venda_compra_loja_virtual todos os registros onde o endereco de cobranca seja igual ao valor passado por parametro
    @Query(value="select distinct(v) from VendaCompraLojaVirtual v "
                + " where v.excluido = false and upper(trim(v.enderecoEntrega.ruaLogra)) like %?1% and v.empresa.id = ?2")
//    @Query(value="select distinct(i.vendaCompraLojaVirtual) from ItemVendaLoja i "
//            + " where i.vendaCompraLojaVirtual.excluido = false and upper(trim(i.vendaCompraLojaVirtual.enderecoCobranca.ruaLogra)) "
//            + " like %?1%")
    List<VendaCompraLojaVirtual> vendaPorEnderecoCobranca(String valor, Long idEmpresa);


    // fazer consulta por data entre data inicial e data final
    @Query(value="select distinct(v) from VendaCompraLojaVirtual v "
                + " where v.excluido = false and v.dataVenda between ?1 and ?2 and v.empresa.id = ?3")
    List<VendaCompraLojaVirtual> vendaPorFaixaData(Date dataInicial, Date dataFinal, Long idEmpresa);

   // fazer cosnulta por cpf
    @Query(value="select distinct(v) from VendaCompraLojaVirtual v "
            + " where v.excluido = false and v.pessoa.cpf = ?1 and v.empresa.id = ?2")
    List<VendaCompraLojaVirtual> vendaPorCpfCliente(String cpf, Long idEmpresa);

    @Query(value="select distinct(v) from VendaCompraLojaVirtual v "
            + " where v.excluido = false and v.pessoa.id = ?1 and v.empresa.id = ?2")
    List<VendaCompraLojaVirtual> vendaPorCliente(Long idCliente);
}