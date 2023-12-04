package com.mocad.ecommerce.service;

import com.mocad.ecommerce.model.dto.RelatorioProdCompraNotaFiscalDTO;
import com.mocad.ecommerce.model.dto.RelatorioProdEstoqueBaixoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotaFiscalCompraService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<RelatorioProdCompraNotaFiscalDTO> relatorioProdCompraNotaFiscal(
            RelatorioProdCompraNotaFiscalDTO relatorioProdCompraNotaFiscalDTO) {

        List<RelatorioProdCompraNotaFiscalDTO> retorno = new ArrayList<>();

        String sql = "select p.id as codigoProduto, p.nome as nomeProduto, "
                        + " p.valor_venda as valorVendaProduto, ntp.quantidade as quantidadeComprada, "
                        + " pj.id as codigoFornecedor, pj.nome as nomeFornecedor,cfc.data_compra as dataCompra "
                        + " from nota_fiscal_compra as cfc "
                        + " inner join nota_item_produto as ntp on  cfc.id = nota_fiscal_compra_id "
                        + " inner join produto as p on p.id = ntp.produto_id "
                        + " inner join pessoa_juridica as pj on pj.id = cfc.pessoa_id where ";

        sql += " cfc.data_compra >='"+relatorioProdCompraNotaFiscalDTO.getDataInicial()+"' and ";
        sql += " cfc.data_compra <= '" + relatorioProdCompraNotaFiscalDTO.getDataFinal() +"' ";

        if (!relatorioProdCompraNotaFiscalDTO.getCodigoNota().isEmpty()) {
            sql += " and cfc.id = " + relatorioProdCompraNotaFiscalDTO.getCodigoNota() + " ";
        }

        if (!relatorioProdCompraNotaFiscalDTO.getCodigoProduto().isEmpty()) {
            sql += " and p.id = " + relatorioProdCompraNotaFiscalDTO.getCodigoProduto() + " ";
        }

        if (!relatorioProdCompraNotaFiscalDTO.getNomeProduto().isEmpty()) {
            sql += " and upper(p.nome) like upper('%"+relatorioProdCompraNotaFiscalDTO.getNomeProduto()+"%')";
        }

        if (!relatorioProdCompraNotaFiscalDTO.getNomeFornecedor().isEmpty()) {
            sql += " and upper(pj.nome) like upper('%"+relatorioProdCompraNotaFiscalDTO.getNomeFornecedor()+"%')";
        }
        retorno = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(RelatorioProdCompraNotaFiscalDTO.class));

        return retorno;
    }

    /**
     * Este relatório retorna os produtos que estão com estoque  menor ou igual a quantidade definida no campo de qtde_alerta_estoque.
     * @param alertaEstoque ObejtoRequisicaoRelatorioProdutoAlertaEstoque
     * @return  List<ObejtoRequisicaoRelatorioProdutoAlertaEstoque>  Lista de Objetos ObejtoRequisicaoRelatorioProdutoAlertaEstoque
     */
    public List<RelatorioProdEstoqueBaixoDTO>
    gerarRelatorioAlertaEstoque(RelatorioProdEstoqueBaixoDTO alertaEstoque ){

        List<RelatorioProdEstoqueBaixoDTO> retorno = new ArrayList<>();

        String sql = "select p.id as codigoProduto, p.nome as nomeProduto, "
                + " p.valor_venda as valorVendaProduto, ntp.quantidade as quantidadeComprada, "
                + " pj.id as codigoFornecedor, pj.nome as nomeFornecedor,cfc.data_compra as dataCompra, "
                + " p.qtd_estoque as qtdEstoque, p.qtde_alerta_estoque as qtdAlertaEstoque "
                + " from nota_fiscal_compra as cfc "
                + " inner join nota_item_produto as ntp on  cfc.id = nota_fiscal_compra_id "
                + " inner join produto as p on p.id = ntp.produto_id "
                + " inner join pessoa_juridica as pj on pj.id = cfc.pessoa_id where ";

        sql += " cfc.data_compra >='"+alertaEstoque.getDataInicial()+"' and ";
        sql += " cfc.data_compra <= '" + alertaEstoque.getDataFinal() +"' ";
        sql += " and p.alerta_qtde_estoque = true and p.qtd_estoque <= p.qtde_alerta_estoque ";

        if (!alertaEstoque.getCodigoNota().isEmpty()) {
            sql += " and cfc.id = " + alertaEstoque.getCodigoNota() + " ";
        }

        if (!alertaEstoque.getCodigoProduto().isEmpty()) {
            sql += " and p.id = " + alertaEstoque.getCodigoProduto() + " ";
        }

        if (!alertaEstoque.getNomeProduto().isEmpty()) {
            sql += " upper(p.nome) like upper('%"+alertaEstoque.getNomeProduto()+"')";
        }

        if (!alertaEstoque.getNomeFornecedor().isEmpty()) {
            sql += " upper(pj.nome) like upper('%"+alertaEstoque.getNomeFornecedor()+"')";
        }

        retorno = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(RelatorioProdEstoqueBaixoDTO.class));

        return retorno;
    }
}
