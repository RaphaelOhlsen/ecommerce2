package com.mocad.ecommerce.controller;

import com.mocad.ecommerce.ExceptionEcommerce;
import com.mocad.ecommerce.model.NotaItemProduto;
import com.mocad.ecommerce.repository.NotaItemProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class NotaItemProdutoController {

    @Autowired
    private NotaItemProdutoRepository notaItemProdutoRepository;

    @PostMapping("**/salvarNotaItemProduto")
    private ResponseEntity<NotaItemProduto> salvarNotaItemProduto(@RequestBody @Valid NotaItemProduto notaItemProduto) throws ExceptionEcommerce {

        if(notaItemProduto.getEmpresa() == null || notaItemProduto.getEmpresa().getId() <= 0) {
            throw new ExceptionEcommerce("Empresa não informada");
        }

        if (notaItemProduto.getProduto() == null || notaItemProduto.getProduto().getId() <= 0) {
            throw new ExceptionEcommerce("Produto não informado");
        }

        if (notaItemProduto.getNotaFiscalCompra() == null || notaItemProduto.getNotaFiscalCompra().getId() <= 0) {
            throw new ExceptionEcommerce("Nota fiscal não informada");
        }

        if (notaItemProduto.getId() == null) {
            List<NotaItemProduto> notaExistente = notaItemProdutoRepository.buscaNotaItemPorProdutoNota(
                    notaItemProduto.getProduto().getId(),
                    notaItemProduto.getNotaFiscalCompra().getId(), notaItemProduto.getEmpresa().getId());

            if (!notaExistente.isEmpty()) {
                throw new ExceptionEcommerce("Já existe este produto cadastrado nesta nota fiscal");
            }
        }

        NotaItemProduto notaItemProdutoSalva = notaItemProdutoRepository.save(notaItemProduto);

        NotaItemProduto notaItemProdutoRecuperado = notaItemProdutoRepository.findById(notaItemProdutoSalva.getId())
                .orElseThrow(() -> new ExceptionEcommerce("Erro ao recuperar o item recém-salvo"));

        return ResponseEntity.ok(notaItemProdutoRecuperado);
    }

    @GetMapping("**/obterNotaItemProdutoPorId/{id}")
    private ResponseEntity<NotaItemProduto> obterNotaItemProduto (@PathVariable("id") Long id) throws ExceptionEcommerce{
        NotaItemProduto notaItemProduto = notaItemProdutoRepository.findById(id).
                orElseThrow(() -> new ExceptionEcommerce("Nota item produto não encontrado"));

        return ResponseEntity.ok(notaItemProduto);
    }

    @DeleteMapping("**/deleteNotaItemProduto/{id}")
    private ResponseEntity<?> deleteNotaItemProduto (@PathVariable("id") Long id) throws ExceptionEcommerce {

        NotaItemProduto notaItemProduto = notaItemProdutoRepository.findById(id)
                .orElseThrow(() -> new ExceptionEcommerce("Nota item produto não encontrado"));

        notaItemProdutoRepository.delete(notaItemProduto);

        return ResponseEntity.ok("Nota Item Produto excluído com sucesso");
    }
}
