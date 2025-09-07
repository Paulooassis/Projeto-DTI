// ProdutoService.java
package com.example.ecomerce.service;

import com.example.ecomerce.dto.AtualizarProdutoDTO;
import com.example.ecomerce.dto.CriarProdutoDTO;
import com.example.ecomerce.dto.ProdutoDTO;
import com.example.ecomerce.enuns.Categoria;

import java.util.List;

public interface ProdutoService {

    List<ProdutoDTO> listarTodos();

    ProdutoDTO buscarPorId(Long id);

    List<ProdutoDTO> buscarPorCategoria(Categoria categoria);

    List<ProdutoDTO> buscarPorNome(String nome);

    List<ProdutoDTO> buscarPorTermo(String termo);

    ProdutoDTO criar(CriarProdutoDTO produtoDTO);

    ProdutoDTO atualizar(Long id, AtualizarProdutoDTO produtoDTO);

    void excluir(Long id);

    Long contarTotalProdutos();

    Integer contarTotalEstoque();

    Double calcularValorTotalEstoque();
}