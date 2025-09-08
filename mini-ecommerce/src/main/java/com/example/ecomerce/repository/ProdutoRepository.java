// ProdutoRepository.java
package com.example.ecomerce.repository;

import com.example.ecomerce.entity.Produto;
import com.example.ecomerce.enuns.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {


    List<Produto> findByCategoria(Categoria categoria);

    List<Produto> findByNomeContainingIgnoreCase(String nome);

    List<Produto> findByPrecoBetween(Double precoMin, Double precoMax);

    @Query("SELECT p FROM Produto p WHERE p.estoque > 0")
    List<Produto> findComEstoque();

    @Query("SELECT p FROM Produto p WHERE LOWER(p.nome) LIKE LOWER(CONCAT('%', :termo, '%')) OR " +
            "LOWER(p.descricao) LIKE LOWER(CONCAT('%', :termo, '%'))")
    List<Produto> buscarPorTermo(@Param("termo") String termo);
}