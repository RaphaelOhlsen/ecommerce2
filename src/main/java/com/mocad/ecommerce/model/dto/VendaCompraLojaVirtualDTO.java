package com.mocad.ecommerce.model.dto;

import com.mocad.ecommerce.model.Endereco;
import com.mocad.ecommerce.model.Pessoa;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class VendaCompraLojaVirtualDTO {


    private Long id;

    private Pessoa pessoa;

    private Endereco enderecoEntrega;

    private Endereco enderecoCobranca;

    private BigDecimal valorTotal = BigDecimal.ZERO;

    private BigDecimal valorDesconto = BigDecimal.ZERO;

    private BigDecimal valorFrete = BigDecimal.ZERO;

    List<ItemVendaDTO> itemVendaLojas = new ArrayList<>();


    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Endereco getEnderecoEntrega() {
        return enderecoEntrega;
    }

    public void setEnderecoEntrega(Endereco enderecoEntrega) {
        this.enderecoEntrega = enderecoEntrega;
    }

    public Endereco getEnderecoCobranca() {
        return enderecoCobranca;
    }

    public void setEnderecoCobranca(Endereco enderecoCobranca) {
        this.enderecoCobranca = enderecoCobranca;
    }

    public BigDecimal getValorDesconto() {
        return valorDesconto;
    }

    public void setValorDesconto(BigDecimal valorDesconto) {
        this.valorDesconto = valorDesconto;
    }

    public BigDecimal getValorFrete() {
        return valorFrete;
    }

    public void setValorFrete(BigDecimal valorFrete) {
        this.valorFrete = valorFrete;
    }

    public List<ItemVendaDTO> getItemVendaLojas() {
        return itemVendaLojas;
    }

    public void setItemVendaLojas(List<ItemVendaDTO> itemVendaLoja) {
        this.itemVendaLojas = itemVendaLoja;
    }
}
