package com.mocad.ecommerce.controller;

import com.mocad.ecommerce.ExceptionEcommerce;
import com.mocad.ecommerce.model.AvaliacaoProduto;
import com.mocad.ecommerce.repository.AvaliacaoProdutoRepository;
import com.mocad.ecommerce.repository.PessoaFisicaRepository;
import com.mocad.ecommerce.repository.PessoaRepository;
import com.mocad.ecommerce.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class AvaliacaoProdutoController {

  @Autowired
  private AvaliacaoProdutoRepository avaliacaoProdutoRepository;

  @Autowired
  private PessoaRepository pessoaRepository;

  @Autowired
  private ProdutoRepository produtoRepository;

  @Autowired
  private PessoaFisicaRepository pessoaFisicaRepository;

  @PostMapping("/salvarAvaliacaoProduto")
  public AvaliacaoProduto salvarAvaliacaoProduto(@RequestBody @Valid AvaliacaoProduto avaliacaoProduto) throws ExceptionEcommerce {

    if (avaliacaoProduto.getPessoa() == null || avaliacaoProduto.getPessoa().getId() <= 0) {
      throw new ExceptionEcommerce("O campo pessoa é obrigatório");
    }

    if (avaliacaoProduto.getProduto() == null || avaliacaoProduto.getProduto().getId() <= 0) {
      throw new ExceptionEcommerce("O campo produto é obrigatório");
    }

    if (avaliacaoProduto.getEmpresa() == null || avaliacaoProduto.getEmpresa().getId() <= 0) {
      throw new ExceptionEcommerce("O campo empresa é obrigatório");
    }

    pessoaRepository.findById(avaliacaoProduto.getEmpresa().getId()).orElseThrow(() -> new ExceptionEcommerce("Empresa não encontrada"));

    produtoRepository.findById(avaliacaoProduto.getProduto().getId()).orElseThrow(() -> new ExceptionEcommerce("Produto não encontrado"));

    pessoaFisicaRepository.findById(avaliacaoProduto.getPessoa().getId()).orElseThrow(() -> new ExceptionEcommerce("Pessoa não encontrada"));


    return avaliacaoProdutoRepository.save(avaliacaoProduto);
  }


  @DeleteMapping("/deletarAvaliacaoProduto/{id}")
  public ResponseEntity<String> deletarAvaliacaoProduto(@PathVariable("id") Long id) throws ExceptionEcommerce {
    avaliacaoProdutoRepository.findById(id).orElseThrow(() -> new ExceptionEcommerce("Avaliação não encontrada"));

    avaliacaoProdutoRepository.deleteById(id);

    return ResponseEntity.ok("Avaliação deletada com sucesso");
  }

  @GetMapping("/obterAvaliacoesPorProduto/{idProduto}")
  public ResponseEntity<List<AvaliacaoProduto>> obterAvaliacoesPorProduto(@PathVariable("idProduto") Long idProduto)
      throws ExceptionEcommerce {
    produtoRepository.findById(idProduto).orElseThrow(() -> new ExceptionEcommerce("Produto não encontrado"));

    List<AvaliacaoProduto> avaliacoes = avaliacaoProdutoRepository.obterAvaliacoesPorProduto(idProduto);

    return ResponseEntity.ok(avaliacoes);
  }

  @GetMapping("/obterAvaliacoesPorProdutoPessoa")
  public ResponseEntity<List<AvaliacaoProduto>> obterAvaliacoesPorProdutoPessoa(@RequestParam("idProduto") Long idProduto, @RequestParam("idPessoa") Long idPessoa) throws ExceptionEcommerce {
    produtoRepository.findById(idProduto).orElseThrow(() -> new ExceptionEcommerce("Produto não encontrado"));

    pessoaFisicaRepository.findById(idPessoa).orElseThrow(() -> new ExceptionEcommerce("Pessoa não encontrada"));

    List<AvaliacaoProduto> avaliacoes = avaliacaoProdutoRepository.obterAvaliacoesPorProdutoPessoa(idProduto, idPessoa);

    return ResponseEntity.ok(avaliacoes);
  }

  @GetMapping("/obterAvaliacoesPorPessoa/{idPessoa}")
  public ResponseEntity<List<AvaliacaoProduto>> obterAvaliacoesPorPessoa(@PathVariable("idPessoa") Long idPessoa) throws ExceptionEcommerce {
    pessoaFisicaRepository.findById(idPessoa).orElseThrow(() -> new ExceptionEcommerce("Pessoa não encontrada"));

    List<AvaliacaoProduto> avaliacoes = avaliacaoProdutoRepository.obterAvaliacoesPorPessoa(idPessoa);

    return ResponseEntity.ok(avaliacoes);
  }
}
