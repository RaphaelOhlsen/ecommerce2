package com.mocad.ecommerce.service;


import com.mocad.ecommerce.model.dto.RelatorioNFStatusDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotaFiscalVendaService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<RelatorioNFStatusDTO> relatorioNFStatus(RelatorioNFStatusDTO relatorioNFStatusDTO) {

        List<RelatorioNFStatusDTO> data = new ArrayList<>();

        String sql = "select venda.id as codigoVenda, venda.data_venda as dataVenda, venda.valor_total as valorVenda, " +
                         "comprador.id as codigoCliente, comprador.nome as nomeCliente, comprador.email as emailCliente, comprador.telefone as foneCliente, " +
                         "forma.descricao as formaPagamento, " +
                         "produto.id as codigoProduto, produto.nome as nomeProduto, " +
                         "status_venda_loja_virtual as statusVenda " +
                     "from vd_cp_loja_virt as venda " +
                     "inner join item_venda_loja as item on item.venda_compra_loja_virtu_id = venda.id " +
                     "inner join produto on produto.id = item.produto_id " +
                     "inner join pessoa_fisica as comprador on venda.pessoa_id = comprador.id " +
                     "inner join forma_pagamento as forma on forma.id = venda.forma_pagamento_id ";

                sql += "where venda.status_venda_loja_virtual = '" + relatorioNFStatusDTO.getStatusVenda() + "'";
                sql += "and venda.data_venda between '" + relatorioNFStatusDTO.getDataInicial() + "' and '" + relatorioNFStatusDTO.getDataFinal() + "'";
                sql += "order by venda.id";


        data = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(RelatorioNFStatusDTO.class));

        return data;
    }
}
