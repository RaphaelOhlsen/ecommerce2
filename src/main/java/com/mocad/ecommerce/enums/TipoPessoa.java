package com.mocad.ecommerce.enums;

public enum TipoPessoa {

    JURIDICA("Juridica"),
    JURIDICA_FORNECEDOR("JuridicaFornecedor"),
    FISICA("Fisica");

    private String descricao;

    TipoPessoa(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
