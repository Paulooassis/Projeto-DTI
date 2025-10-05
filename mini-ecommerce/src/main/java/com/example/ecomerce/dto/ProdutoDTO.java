// ProdutoDTO.java
package com.example.ecomerce.dto;

import com.example.ecomerce.enuns.Categoria;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

// TODO: CODE REVIEW - Código duplicado nos DTOs
// Sugestão: Criar classe base BaseProdutoDTO com campos e validações comuns
// Benefício: Elimina duplicação, facilita manutenção e garante consistência nas validações
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoDTO {
    private Long id;
    private String nome;
    // TODO: CODE REVIEW - Substituir Double por BigDecimal para valores monetários
    // Sugestão: Usar BigDecimal para evitar problemas de precisão em cálculos financeiros
    // Benefício: Garante precisão em cálculos monetários e evita erros de arredondamento
    private Double preco;
    private Categoria categoria;
    private Integer estoque;
    private LocalDate dataEntrada;
    private String descricao;
    private String imagemUrl;

    public ProdutoDTO(Long id, String nome, Double preco, Categoria categoria, Integer estoque, LocalDate dataEntrada, String descricao, String imagemUrl) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
        this.categoria = categoria;
        this.estoque = estoque;
        this.dataEntrada = dataEntrada;
        this.descricao = descricao;
        this.imagemUrl = imagemUrl;
    }

    public ProdutoDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImagemUrl() {
        return imagemUrl;
    }

    public void setImagemUrl(String imagemUrl) {
        this.imagemUrl = imagemUrl;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDate getDataEntrada() {
        return dataEntrada;
    }

    public void setDataEntrada(LocalDate dataEntrada) {
        this.dataEntrada = dataEntrada;
    }

    public Integer getEstoque() {
        return estoque;
    }

    public void setEstoque(Integer estoque) {
        this.estoque = estoque;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}