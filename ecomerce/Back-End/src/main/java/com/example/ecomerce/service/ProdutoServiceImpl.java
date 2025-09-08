// ProdutoServiceImpl.java
package com.example.ecomerce.service;

import com.example.ecomerce.dto.AtualizarProdutoDTO;
import com.example.ecomerce.dto.CriarProdutoDTO;
import com.example.ecomerce.dto.ProdutoDTO;
import com.example.ecomerce.entity.Produto;
import com.example.ecomerce.enuns.Categoria;
import com.example.ecomerce.exception.ProdutoNaoEncontradoException;
import com.example.ecomerce.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProdutoServiceImpl implements ProdutoService {

    private final ProdutoRepository produtoRepository;

    public ProdutoServiceImpl(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    @Override
    public List<ProdutoDTO> listarTodos() {
        return produtoRepository.findAll().stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ProdutoDTO buscarPorId(Long id) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new ProdutoNaoEncontradoException(id));
        return converterParaDTO(produto);
    }

    @Override
    public List<ProdutoDTO> buscarPorCategoria(Categoria categoria) {
        return produtoRepository.findByCategoria(categoria).stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProdutoDTO> buscarPorNome(String nome) {
        return produtoRepository.findByNomeContainingIgnoreCase(nome).stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProdutoDTO> buscarPorTermo(String termo) {
        return produtoRepository.buscarPorTermo(termo).stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ProdutoDTO criar(CriarProdutoDTO produtoDTO) {
        Produto produto = new Produto(
                null,
                produtoDTO.getNome(),
                produtoDTO.getPreco(),
                produtoDTO.getCategoria(),
                produtoDTO.getEstoque(),
                produtoDTO.getDataEntrada(),
                produtoDTO.getDescricao(),
                produtoDTO.getImagemUrl()
        );

        Produto produtoSalvo = produtoRepository.save(produto);
        return converterParaDTO(produtoSalvo);
    }

    @Override
    public ProdutoDTO atualizar(Long id, AtualizarProdutoDTO produtoDTO) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new ProdutoNaoEncontradoException(id));

        produto.setNome(produtoDTO.getNome());
        produto.setPreco(produtoDTO.getPreco());
        produto.setCategoria(produtoDTO.getCategoria());
        produto.setEstoque(produtoDTO.getEstoque());
        produto.setDataEntrada(produtoDTO.getDataEntrada());
        produto.setDescricao(produtoDTO.getDescricao());
        produto.setImagemUrl(produtoDTO.getImagemUrl());

        Produto produtoAtualizado = produtoRepository.save(produto);
        return converterParaDTO(produtoAtualizado);
    }

    @Override
    public void excluir(Long id) {
        if (!produtoRepository.existsById(id)) {
            throw new ProdutoNaoEncontradoException(id);
        }
        produtoRepository.deleteById(id);
    }

    @Override
    public Long contarTotalProdutos() {
        return produtoRepository.count();
    }

    @Override
    public Integer contarTotalEstoque() {
        return produtoRepository.findAll().stream()
                .mapToInt(Produto::getEstoque)
                .sum();
    }

    @Override
    public Double calcularValorTotalEstoque() {
        return produtoRepository.findAll().stream()
                .mapToDouble(p -> p.getPreco() * p.getEstoque())
                .sum();
    }

    public ProdutoDTO converterParaDTO(Produto produto) {
        return new ProdutoDTO(
                produto.getId(),
                produto.getNome(),
                produto.getPreco(),
                produto.getCategoria(),
                produto.getEstoque(),
                produto.getDataEntrada(),
                produto.getDescricao(),
                produto.getImagemUrl()
        );
    }
}