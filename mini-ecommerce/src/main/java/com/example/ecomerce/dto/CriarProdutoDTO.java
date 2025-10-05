// CriarProdutoDTO.java
package com.example.ecomerce.dto;

import com.example.ecomerce.enuns.Categoria;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

// TODO: CODE REVIEW - Código duplicado nos DTOs
// Sugestão: Criar classe base BaseProdutoDTO com campos e validações comuns
// Benefício: Elimina duplicação, facilita manutenção e garante consistência nas validações
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CriarProdutoDTO {
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Integer getEstoque() {
        return estoque;
    }

    public void setEstoque(Integer estoque) {
        this.estoque = estoque;
    }

    public LocalDate getDataEntrada() {
        return dataEntrada;
    }

    public void setDataEntrada(LocalDate dataEntrada) {
        this.dataEntrada = dataEntrada;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getImagemUrl() {
        return imagemUrl;
    }

    public void setImagemUrl(String imagemUrl) {
        this.imagemUrl = imagemUrl;
    }

    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
    private String nome;

    @NotNull(message = "Preço é obrigatório")
    @DecimalMin(value = "0.0", inclusive = false, message = "Preço deve ser maior que zero")
    // TODO: CODE REVIEW - Substituir Double por BigDecimal para valores monetários
    // Sugestão: Usar BigDecimal para evitar problemas de precisão em cálculos financeiros
    // Benefício: Garante precisão em cálculos monetários e evita erros de arredondamento
    private Double preco;

    @NotNull(message = "Categoria é obrigatória")
    private Categoria categoria;

    @NotNull(message = "Estoque é obrigatório")
    @Min(value = 0, message = "Estoque não pode ser negativo")
    private Integer estoque;

    @NotNull(message = "Data de entrada é obrigatória")
    private LocalDate dataEntrada;

    @Size(max = 500, message = "Descrição deve ter no máximo 500 caracteres")
    private String descricao;

    @Size(max = 255, message = "URL da imagem deve ter no máximo 255 caracteres")
    private String imagemUrl;
}