package com.example.ecomerce.enuns;

public enum Categoria {
    ELETRONICOS("Eletrônicos"),
    LIVROS("Livros"),
    ROUPAS("Roupas"),
    ESPORTES("Esportes"),
    CASA("Casa e Cozinha");

    private final String descricao;

    Categoria(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}