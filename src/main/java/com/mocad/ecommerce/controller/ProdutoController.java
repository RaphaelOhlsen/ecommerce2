package com.mocad.ecommerce.controller;

import com.mocad.ecommerce.ExceptionEcommerce;
import com.mocad.ecommerce.model.Produto;
import com.mocad.ecommerce.repository.ProdutoRepository;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RestController
public class ProdutoController {

    @Autowired
    private ProdutoRepository produtoRepository;

    @ResponseBody /*Poder dar um retorno da API*/
    @PostMapping(value = "**/salvarProduto") /*Mapeando a url para receber JSON*/
    public ResponseEntity<Produto> salvarProduto(@RequestBody @Valid Produto produto) throws ExceptionEcommerce { /*Recebe o JSON e converte pra Objeto*/

        if (produto.getEmpresa().getId() == null || produto.getEmpresa().getId() <= 0) {
            throw new ExceptionEcommerce("Empresa não informada");
        }

        if (produto.getId() == null) {
            List<Produto> produtos = produtoRepository.buscarProdutoNome(produto.getNome().toUpperCase(), produto.getEmpresa().getId());

            if (!produtos.isEmpty()) {
                throw new ExceptionEcommerce("Já existe Produto com este nome: " + produto.getNome());
            }
        }

        if (produto.getCategoriaProduto().getId()  == null || produto.getCategoriaProduto().getId() <= 0) {
            throw new ExceptionEcommerce("Categoria não informada");
        }

        if (produto.getMarcaProduto().getId() == null || produto.getMarcaProduto().getId() <= 0) {
            throw new ExceptionEcommerce("Marca não informada");
        }


        Produto produtoSalvo = produtoRepository.save(produto);

        return new ResponseEntity(produtoSalvo, HttpStatus.OK);
    }

    @ResponseBody /*Poder dar um retorno da API*/
    @PostMapping(value = "**/deleteProduto") /*Mapeando a url para receber JSON*/
    public ResponseEntity<String> deleteAcesso(@RequestBody Produto produto) { /*Recebe o JSON e converte pra Objeto*/

        produtoRepository.deleteById(produto.getId());

        return new ResponseEntity("Produto Removido",HttpStatus.OK);
    }

    @ResponseBody
    @DeleteMapping(value = "**/deleteProdutoPorId/{id}") /*Mapeando a url para receber JSON*/
    public ResponseEntity<String> deleteAcessoPorId(@PathVariable Long id) {

        produtoRepository.deleteById(id);

        return new ResponseEntity("Acesso Removido",HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping(value = "**/obeterProduto/{id}") /*Mapeando a url para receber JSON*/
    public ResponseEntity<Produto> obeterProduto(@PathVariable Long id) throws ExceptionEcommerce {

        Produto produto = produtoRepository.findById(id).orElse(null);

        if (produto == null) {
            throw new ExceptionEcommerce("Produto "+ id + " não encontrado");
        }

        return new ResponseEntity<Produto>(produto, HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping(value = "**/buscarPorNomeProduto/{desc}") /*Mapeando a url para receber JSON*/
    public ResponseEntity<List<Produto>> buscarPorNome(@RequestParam("desc") String desc,
                                                       @RequestParam("idEmpresa") Long idEmpresa) {

        List<Produto> produtos = produtoRepository.buscarProdutoNome(desc.toUpperCase().trim(), idEmpresa);

        return new ResponseEntity<List<Produto>>(produtos, HttpStatus.OK);
    }
}
