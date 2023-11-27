package com.mocad.ecommerce.controller;

import com.mocad.ecommerce.ExceptionEcommerce;
import com.mocad.ecommerce.model.CategoriaProduto;
import com.mocad.ecommerce.model.dto.CategoriaProdutoDTO;
import com.mocad.ecommerce.repository.CategoriaProdutoRepository;
import com.mocad.ecommerce.repository.PessoaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CategoriaProdutoController {

    @Autowired
    private CategoriaProdutoRepository categoriaProdutoRepository;

    @Autowired
    private PessoaRepository pessoaRepository;

    @ResponseBody
    @GetMapping(value = "**/buscarPorDescCategoria")
    public ResponseEntity<List<CategoriaProduto>> buscarPorDesc( @RequestParam("desc") String desc,
                                                                 @RequestParam("idEmpresa") Long idEmpresa) throws ExceptionEcommerce {

        if (idEmpresa == null || idEmpresa <= 0) {
            throw new ExceptionEcommerce("Empresa não informada");
        }

        List<CategoriaProduto> categorias = categoriaProdutoRepository.buscarCategoriaDesc(desc.toUpperCase().trim(), idEmpresa);

        return ResponseEntity.ok(categorias);
    }


    @ResponseBody
    @PostMapping(value = "**/salvarCategoria")
    public ResponseEntity<CategoriaProdutoDTO> salvarCategoria (@RequestBody CategoriaProduto categoriaProduto) throws ExceptionEcommerce {

        if (categoriaProduto.getEmpresa().getId() == null || categoriaProduto.getEmpresa().getId() <= 0) {
            throw new ExceptionEcommerce("Empresa não informada");
        }

       if (pessoaRepository.findPjById(categoriaProduto.getEmpresa().getId()) == null) {
            throw new ExceptionEcommerce("Empresa não encontrada");
        }

        if (categoriaProduto.getId() == null &&
                categoriaProdutoRepository.existeCategoria(categoriaProduto.getNomeDesc(), categoriaProduto.getEmpresa().getId())) {
            throw new ExceptionEcommerce("Não pode cadastrar duas categorias com o mesmo nome");
        }

        CategoriaProduto categoriaProdutoSalva = categoriaProdutoRepository.save(categoriaProduto);

        CategoriaProdutoDTO categoriaProdutoDTO = new CategoriaProdutoDTO();
        categoriaProdutoDTO.setId(categoriaProdutoSalva.getId());
        categoriaProdutoDTO.setNomeDesc(categoriaProdutoSalva.getNomeDesc());
        categoriaProdutoDTO.setEmpresa(categoriaProdutoSalva.getEmpresa().getId().toString());

        return ResponseEntity.ok(categoriaProdutoDTO);
    }

    @DeleteMapping(value = "**/deleteCategoria/{id}")
    public ResponseEntity<?> deleteCategoria(@PathVariable("id") Long id) {

        boolean categoriaProduto = categoriaProdutoRepository.findById(id).isPresent();

        if (!categoriaProduto) {
            return ResponseEntity.badRequest().body("Categoria não encontrada");
        }

        categoriaProdutoRepository.deleteById(id);

        return ResponseEntity.ok("Categoria Removida");
    }
}
