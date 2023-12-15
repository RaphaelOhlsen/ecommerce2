package com.mocad.ecommerce.service;

import com.mocad.ecommerce.model.ItemVendaLoja;
import com.mocad.ecommerce.model.VendaCompraLojaVirtual;
import com.mocad.ecommerce.model.dto.ItemVendaDTO;
import com.mocad.ecommerce.model.dto.VendaCompraLojaVirtualDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class VendaService {


  @Autowired
  private JdbcTemplate jdbcTemplate;

  public void exclusaoTotalVendaBanco(Long idVenda) {

    String value =
        " begin;"
            + " UPDATE nota_fiscal_venda set venda_compra_loja_virt_id = null where venda_compra_loja_virt_id = " + idVenda + "; "
            + " delete from nota_fiscal_venda where venda_compra_loja_virt_id = " + idVenda + "; "
            + " delete from item_venda_loja where venda_compra_loja_virtu_id = " + idVenda + "; "
            + " delete from status_rastreio where venda_compra_loja_virt_id = " + idVenda + "; "
            + " delete from vd_cp_loja_virt where id = " + idVenda + "; "
            + " commit; ";

    jdbcTemplate.execute(value);
  }

  public VendaCompraLojaVirtualDTO consultaVenda(VendaCompraLojaVirtual compraLojaVirtual) {


    VendaCompraLojaVirtualDTO compraLojaVirtualDTO = new VendaCompraLojaVirtualDTO();

    compraLojaVirtualDTO.setValorTotal(compraLojaVirtual.getValorTotal());
    compraLojaVirtualDTO.setPessoa(compraLojaVirtual.getPessoa());

    compraLojaVirtualDTO.setEnderecoEntrega(compraLojaVirtual.getEnderecoEntrega());
    compraLojaVirtualDTO.setEnderecoCobranca(compraLojaVirtual.getEnderecoCobranca());

    compraLojaVirtualDTO.setValorDesconto(compraLojaVirtual.getValorDesconto());
    compraLojaVirtualDTO.setValorFrete(compraLojaVirtual.getValorFrete());
    compraLojaVirtualDTO.setId(compraLojaVirtual.getId());

    for (ItemVendaLoja item : compraLojaVirtual.getItemVendaLojas()) {

      ItemVendaDTO itemVendaDTO = new ItemVendaDTO();
      itemVendaDTO.setQuantidade(item.getQuantidade());
      itemVendaDTO.setProduto(item.getProduto());

      compraLojaVirtualDTO.getItemVendaLojas().add(itemVendaDTO);
    }

    return compraLojaVirtualDTO;
  }

}
