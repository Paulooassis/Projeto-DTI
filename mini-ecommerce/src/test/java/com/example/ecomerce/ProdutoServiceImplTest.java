// ProdutoServiceImplTest.java
package com.example.ecomerce;

import com.example.ecomerce.dto.AtualizarProdutoDTO;
import com.example.ecomerce.dto.CriarProdutoDTO;
import com.example.ecomerce.dto.ProdutoDTO;
import com.example.ecomerce.entity.Produto;
import com.example.ecomerce.enuns.Categoria;
import com.example.ecomerce.exception.ProdutoNaoEncontradoException;
import com.example.ecomerce.repository.ProdutoRepository;
import com.example.ecomerce.service.ProdutoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceImplTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @InjectMocks
    private ProdutoServiceImpl produtoService;

    private Produto produto;
    private CriarProdutoDTO criarProdutoDTO;
    private AtualizarProdutoDTO atualizarProdutoDTO;

    @BeforeEach
    void setUp() {
        produto = new Produto(
                1L,
                "Notebook Dell",
                3500.00,
                Categoria.ELETRONICOS,
                10,
                LocalDate.now(),
                "Notebook Dell Inspiron 15",
                "http://imagem.com/notebook.jpg"
        );

        criarProdutoDTO = new CriarProdutoDTO();
        criarProdutoDTO.setNome("Mouse Gamer");
        criarProdutoDTO.setPreco(150.00);
        criarProdutoDTO.setCategoria(Categoria.ELETRONICOS);
        criarProdutoDTO.setEstoque(20);
        criarProdutoDTO.setDataEntrada(LocalDate.now());
        criarProdutoDTO.setDescricao("Mouse gamer RGB");
        criarProdutoDTO.setImagemUrl("http://imagem.com/mouse.jpg");

        atualizarProdutoDTO = new AtualizarProdutoDTO();
        atualizarProdutoDTO.setNome("Notebook Dell Atualizado");
        atualizarProdutoDTO.setPreco(4000.00);
        atualizarProdutoDTO.setCategoria(Categoria.ELETRONICOS);
        atualizarProdutoDTO.setEstoque(15);
        atualizarProdutoDTO.setDataEntrada(LocalDate.now());
        atualizarProdutoDTO.setDescricao("Notebook Dell atualizado");
        atualizarProdutoDTO.setImagemUrl("http://imagem.com/notebook-atualizado.jpg");
    }

    @Test
    void listarTodos_DeveRetornarListaDeProdutos() {
        // Arrange
        Produto produto2 = new Produto(
                2L,
                "Livro Java",
                80.00,
                Categoria.LIVROS,
                5,
                LocalDate.now(),
                "Livro de programação Java",
                "http://imagem.com/livro.jpg"
        );

        when(produtoRepository.findAll()).thenReturn(Arrays.asList(produto, produto2));

        List<ProdutoDTO> resultado = produtoService.listarTodos();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Notebook Dell", resultado.get(0).getNome());
        assertEquals("Livro Java", resultado.get(1).getNome());
        verify(produtoRepository, times(1)).findAll();
    }

    @Test
    void buscarPorId_QuandoProdutoExiste_DeveRetornarProduto() {
        // Arrange
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));

        ProdutoDTO resultado = produtoService.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Notebook Dell", resultado.getNome());
        assertEquals(3500.00, resultado.getPreco());
        verify(produtoRepository, times(1)).findById(1L);
    }

    @Test
    void buscarPorId_QuandoProdutoNaoExiste_DeveLancarExcecao() {
        when(produtoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ProdutoNaoEncontradoException.class, () -> {
            produtoService.buscarPorId(99L);
        });
        verify(produtoRepository, times(1)).findById(99L);
    }

    @Test
    void buscarPorCategoria_DeveRetornarProdutosDaCategoria() {
        // Arrange
        when(produtoRepository.findByCategoria(Categoria.ELETRONICOS))
                .thenReturn(Arrays.asList(produto));


        List<ProdutoDTO> resultado = produtoService.buscarPorCategoria(Categoria.ELETRONICOS);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(Categoria.ELETRONICOS, resultado.get(0).getCategoria());
        verify(produtoRepository, times(1)).findByCategoria(Categoria.ELETRONICOS);
    }

    @Test
    void buscarPorNome_DeveRetornarProdutosComNomeSimilar() {
        // Arrange
        when(produtoRepository.findByNomeContainingIgnoreCase("Dell"))
                .thenReturn(Arrays.asList(produto));


        List<ProdutoDTO> resultado = produtoService.buscarPorNome("Dell");

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertTrue(resultado.get(0).getNome().contains("Dell"));
        verify(produtoRepository, times(1)).findByNomeContainingIgnoreCase("Dell");
    }

    @Test
    void buscarPorTermo_DeveRetornarProdutosComTermo() {
        // Arrange
        when(produtoRepository.buscarPorTermo("notebook"))
                .thenReturn(Arrays.asList(produto));


        List<ProdutoDTO> resultado = produtoService.buscarPorTermo("notebook");

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(produtoRepository, times(1)).buscarPorTermo("notebook");
    }

    @Test
    void criar_DeveSalvarERetornarProdutoCriado() {
        // Arrange
        Produto produtoParaSalvar = new Produto(
                null,
                criarProdutoDTO.getNome(),
                criarProdutoDTO.getPreco(),
                criarProdutoDTO.getCategoria(),
                criarProdutoDTO.getEstoque(),
                criarProdutoDTO.getDataEntrada(),
                criarProdutoDTO.getDescricao(),
                criarProdutoDTO.getImagemUrl()
        );

        Produto produtoSalvo = new Produto(
                3L,
                criarProdutoDTO.getNome(),
                criarProdutoDTO.getPreco(),
                criarProdutoDTO.getCategoria(),
                criarProdutoDTO.getEstoque(),
                criarProdutoDTO.getDataEntrada(),
                criarProdutoDTO.getDescricao(),
                criarProdutoDTO.getImagemUrl()
        );

        when(produtoRepository.save(any(Produto.class))).thenReturn(produtoSalvo);


        ProdutoDTO resultado = produtoService.criar(criarProdutoDTO);

        // Assert
        assertNotNull(resultado);
        assertEquals(3L, resultado.getId());
        assertEquals("Mouse Gamer", resultado.getNome());
        assertEquals(150.00, resultado.getPreco());
        verify(produtoRepository, times(1)).save(any(Produto.class));
    }

    @Test
    void atualizar_QuandoProdutoExiste_DeveAtualizarERetornarProduto() {
        // Arrange
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));
        when(produtoRepository.save(any(Produto.class))).thenReturn(produto);


        ProdutoDTO resultado = produtoService.atualizar(1L, atualizarProdutoDTO);

        // Assert
        assertNotNull(resultado);
        assertEquals("Notebook Dell Atualizado", resultado.getNome());
        assertEquals(4000.00, resultado.getPreco());
        assertEquals(15, resultado.getEstoque());
        verify(produtoRepository, times(1)).findById(1L);
        verify(produtoRepository, times(1)).save(any(Produto.class));
    }

    @Test
    void atualizar_QuandoProdutoNaoExiste_DeveLancarExcecao() {
        // Arrange
        when(produtoRepository.findById(99L)).thenReturn(Optional.empty());


        assertThrows(ProdutoNaoEncontradoException.class, () -> {
            produtoService.atualizar(99L, atualizarProdutoDTO);
        });
        verify(produtoRepository, times(1)).findById(99L);
        verify(produtoRepository, never()).save(any(Produto.class));
    }

    @Test
    void excluir_QuandoProdutoExiste_DeveExcluirProduto() {
        // Arrange
        when(produtoRepository.existsById(1L)).thenReturn(true);


        produtoService.excluir(1L);

        // Assert
        verify(produtoRepository, times(1)).existsById(1L);
        verify(produtoRepository, times(1)).deleteById(1L);
    }

    @Test
    void excluir_QuandoProdutoNaoExiste_DeveLancarExcecao() {
        when(produtoRepository.existsById(99L)).thenReturn(false);

        assertThrows(ProdutoNaoEncontradoException.class, () -> {
            produtoService.excluir(99L);
        });
        verify(produtoRepository, times(1)).existsById(99L);
        verify(produtoRepository, never()).deleteById(anyLong());
    }

    @Test
    void contarTotalProdutos_DeveRetornarQuantidadeTotal() {
        when(produtoRepository.count()).thenReturn(5L);


        Long resultado = produtoService.contarTotalProdutos();


        assertEquals(5L, resultado);
        verify(produtoRepository, times(1)).count();
    }

    @Test
    void contarTotalEstoque_DeveRetornarSomaTotalEstoque() {
        Produto produto2 = new Produto(
                2L,
                "Teclado",
                200.00,
                Categoria.ELETRONICOS,
                8,
                LocalDate.now(),
                "Teclado mecânico",
                "http://imagem.com/teclado.jpg"
        );

        when(produtoRepository.findAll()).thenReturn(Arrays.asList(produto, produto2));


        Integer resultado = produtoService.contarTotalEstoque();

        assertEquals(18, resultado); // 10 + 8
        verify(produtoRepository, times(1)).findAll();
    }

    @Test
    void calcularValorTotalEstoque_DeveRetornarValorTotal() {
        Produto produto2 = new Produto(
                2L,
                "Teclado",
                200.00,
                Categoria.ELETRONICOS,
                8,
                LocalDate.now(),
                "Teclado mecânico",
                "http://imagem.com/teclado.jpg"
        );

        when(produtoRepository.findAll()).thenReturn(Arrays.asList(produto, produto2));


        Double resultado = produtoService.calcularValorTotalEstoque();

        // Assert
        assertEquals(3500.00 * 10 + 200.00 * 8, resultado); // 35000 + 1600 = 36600
        verify(produtoRepository, times(1)).findAll();
    }

    @Test
    void converterParaDTO_DeveConverterProdutoParaDTO() {

        ProdutoDTO resultado = produtoService.converterParaDTO(produto);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Notebook Dell", resultado.getNome());
        assertEquals(3500.00, resultado.getPreco());
        assertEquals(Categoria.ELETRONICOS, resultado.getCategoria());
        assertEquals(10, resultado.getEstoque());
        assertEquals("Notebook Dell Inspiron 15", resultado.getDescricao());
        assertEquals("http://imagem.com/notebook.jpg", resultado.getImagemUrl());
    }
}