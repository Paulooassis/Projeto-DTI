package com.example.ecomerce.controller;

import com.example.ecomerce.dto.AtualizarProdutoDTO;
import com.example.ecomerce.dto.CriarProdutoDTO;
import com.example.ecomerce.dto.ProdutoDTO;
import com.example.ecomerce.enuns.Categoria;
import com.example.ecomerce.service.ProdutoService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/produtos")
@CrossOrigin(origins = {"http://127.0.0.1:5500", "http://localhost:5500"})
public class ProdutoController {

    private static final Logger logger = LoggerFactory.getLogger(ProdutoController.class);
    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @GetMapping
    public ResponseEntity<List<ProdutoDTO>> listarTodos() {
        logger.info("GET /api/produtos - Listando todos os produtos");
        try {
            List<ProdutoDTO> produtos = produtoService.listarTodos();
            logger.info("GET /api/produtos - {} produtos encontrados", produtos.size());
            return ResponseEntity.ok(produtos);
        } catch (Exception e) {
            logger.error("GET /api/produtos - Erro ao listar produtos: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdutoDTO> buscarPorId(@PathVariable Long id) {
        logger.info("GET /api/produtos/{} - Buscando produto por ID", id);
        try {
            ProdutoDTO produto = produtoService.buscarPorId(id);
            logger.info("GET /api/produtos/{} - Produto encontrado: {}", id, produto.getNome());
            return ResponseEntity.ok(produto);
        } catch (Exception e) {
            logger.error("GET /api/produtos/{} - Erro ao buscar produto: {}", id, e.getMessage(), e);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<ProdutoDTO>> buscarPorCategoria(@PathVariable Categoria categoria) {
        logger.info("GET /api/produtos/categoria/{} - Buscando produtos por categoria", categoria);
        try {
            List<ProdutoDTO> produtos = produtoService.buscarPorCategoria(categoria);
            logger.info("GET /api/produtos/categoria/{} - {} produtos encontrados", categoria, produtos.size());
            return ResponseEntity.ok(produtos);
        } catch (Exception e) {
            logger.error("GET /api/produtos/categoria/{} - Erro ao buscar produtos: {}", categoria, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<ProdutoDTO>> buscarPorNome(@RequestParam String nome) {
        logger.info("GET /api/produtos/buscar?nome={} - Buscando produtos por nome", nome);
        try {
            List<ProdutoDTO> produtos = produtoService.buscarPorNome(nome);
            logger.info("GET /api/produtos/buscar?nome={} - {} produtos encontrados", nome, produtos.size());
            return ResponseEntity.ok(produtos);
        } catch (Exception e) {
            logger.error("GET /api/produtos/buscar?nome={} - Erro ao buscar produtos: {}", nome, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/buscar-termo")
    public ResponseEntity<List<ProdutoDTO>> buscarPorTermo(@RequestParam String termo) {
        logger.info("GET /api/produtos/buscar-termo?termo={} - Buscando produtos por termo", termo);
        try {
            List<ProdutoDTO> produtos = produtoService.buscarPorTermo(termo);
            logger.info("GET /api/produtos/buscar-termo?termo={} - {} produtos encontrados", termo, produtos.size());
            return ResponseEntity.ok(produtos);
        } catch (Exception e) {
            logger.error("GET /api/produtos/buscar-termo?termo={} - Erro ao buscar produtos: {}", termo, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<ProdutoDTO> criar(@Valid @RequestBody CriarProdutoDTO produtoDTO) {
        logger.info("POST /api/produtos - Criando novo produto: {}", produtoDTO.getNome());
        try {
            ProdutoDTO produtoCriado = produtoService.criar(produtoDTO);
            logger.info("POST /api/produtos - Produto criado com sucesso: ID {}", produtoCriado.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(produtoCriado);
        } catch (Exception e) {
            logger.error("POST /api/produtos - Erro ao criar produto: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProdutoDTO> atualizar(@PathVariable Long id,
                                                @Valid @RequestBody AtualizarProdutoDTO produtoDTO) {
        logger.info("PUT /api/produtos/{} - Atualizando produto", id);
        try {
            ProdutoDTO produtoAtualizado = produtoService.atualizar(id, produtoDTO);
            logger.info("PUT /api/produtos/{} - Produto atualizado com sucesso", id);
            return ResponseEntity.ok(produtoAtualizado);
        } catch (Exception e) {
            logger.error("PUT /api/produtos/{} - Erro ao atualizar produto: {}", id, e.getMessage(), e);
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        logger.info("DELETE /api/produtos/{} - Excluindo produto", id);
        try {
            produtoService.excluir(id);
            logger.info("DELETE /api/produtos/{} - Produto excluído com sucesso", id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("DELETE /api/produtos/{} - Erro ao excluir produto: {}", id, e.getMessage(), e);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/dashboard/total-produtos")
    public ResponseEntity<Long> contarTotalProdutos() {
        logger.info("GET /api/produtos/dashboard/total-produtos - Contando total de produtos");
        try {
            Long total = produtoService.contarTotalProdutos();
            logger.info("GET /api/produtos/dashboard/total-produtos - Total: {}", total);
            return ResponseEntity.ok(total);
        } catch (Exception e) {
            logger.error("GET /api/produtos/dashboard/total-produtos - Erro ao contar produtos: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/dashboard/total-estoque")
    public ResponseEntity<Integer> contarTotalEstoque() {
        logger.info("GET /api/produtos/dashboard/total-estoque - Contando total em estoque");
        try {
            Integer total = produtoService.contarTotalEstoque();
            logger.info("GET /api/produtos/dashboard/total-estoque - Total em estoque: {}", total);
            return ResponseEntity.ok(total);
        } catch (Exception e) {
            logger.error("GET /api/produtos/dashboard/total-estoque - Erro ao contar estoque: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/dashboard/valor-total")
    public ResponseEntity<Double> calcularValorTotalEstoque() {
        logger.info("GET /api/produtos/dashboard/valor-total - Calculando valor total do estoque");
        try {
            Double valorTotal = produtoService.calcularValorTotalEstoque();
            logger.info("GET /api/produtos/dashboard/valor-total - Valor total: R$ {}", valorTotal);
            return ResponseEntity.ok(valorTotal);
        } catch (Exception e) {
            logger.error("GET /api/produtos/dashboard/valor-total - Erro ao calcular valor: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}