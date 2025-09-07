// ProdutoController.java
package com.example.ecomerce.controller;

import com.example.ecomerce.dto.AtualizarProdutoDTO;
import com.example.ecomerce.dto.CriarProdutoDTO;
import com.example.ecomerce.dto.ProdutoDTO;
import com.example.ecomerce.enuns.Categoria;
import com.example.ecomerce.service.ProdutoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/produtos")
@CrossOrigin(origins = {"http://127.0.0.1:5500", "http://localhost:5500"})
public class ProdutoController {
    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    private final ProdutoService produtoService;

    @GetMapping
    public ResponseEntity<List<ProdutoDTO>> listarTodos() {
        List<ProdutoDTO> produtos = produtoService.listarTodos();
        return ResponseEntity.ok(produtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdutoDTO> buscarPorId(@PathVariable Long id) {
        ProdutoDTO produto = produtoService.buscarPorId(id);
        return ResponseEntity.ok(produto);
    }

    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<ProdutoDTO>> buscarPorCategoria(@PathVariable Categoria categoria) {
        List<ProdutoDTO> produtos = produtoService.buscarPorCategoria(categoria);
        return ResponseEntity.ok(produtos);
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<ProdutoDTO>> buscarPorNome(@RequestParam String nome) {
        List<ProdutoDTO> produtos = produtoService.buscarPorNome(nome);
        return ResponseEntity.ok(produtos);
    }

    @GetMapping("/buscar-termo")
    public ResponseEntity<List<ProdutoDTO>> buscarPorTermo(@RequestParam String termo) {
        List<ProdutoDTO> produtos = produtoService.buscarPorTermo(termo);
        return ResponseEntity.ok(produtos);
    }

    @PostMapping
    public ResponseEntity<ProdutoDTO> criar(@Valid @RequestBody CriarProdutoDTO produtoDTO) {
        ProdutoDTO produtoCriado = produtoService.criar(produtoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(produtoCriado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProdutoDTO> atualizar(@PathVariable Long id,
                                                @Valid @RequestBody AtualizarProdutoDTO produtoDTO) {
        ProdutoDTO produtoAtualizado = produtoService.atualizar(id, produtoDTO);
        return ResponseEntity.ok(produtoAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        produtoService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/dashboard/total-produtos")
    public ResponseEntity<Long> contarTotalProdutos() {
        Long total = produtoService.contarTotalProdutos();
        return ResponseEntity.ok(total);
    }

    @GetMapping("/dashboard/total-estoque")
    public ResponseEntity<Integer> contarTotalEstoque() {
        Integer total = produtoService.contarTotalEstoque();
        return ResponseEntity.ok(total);
    }

    @GetMapping("/dashboard/valor-total")
    public ResponseEntity<Double> calcularValorTotalEstoque() {
        Double valorTotal = produtoService.calcularValorTotalEstoque();
        return ResponseEntity.ok(valorTotal);
    }
}