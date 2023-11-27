package com.mocad.ecommerce.controller;

import com.mocad.ecommerce.ExceptionEcommerce;
import com.mocad.ecommerce.model.MarcaProduto;
import com.mocad.ecommerce.repository.MarcaProdutoRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class MarcaProdutoController {

    @Autowired
    private MarcaProdutoRespository marcaProdutoRepository;

    @GetMapping(value = "**/buscarMarcaProdutoPorDesc")
    public ResponseEntity<List<MarcaProduto>> buscarporNome(@RequestParam("nome") String nome,
                                                            @RequestParam("idEmpresa") Long idEmpresa) throws ExceptionEcommerce {

        List<MarcaProduto> marcas = marcaProdutoRepository.buscarMarcaPorNome(nome.toUpperCase().trim(), idEmpresa);

        return ResponseEntity.ok(marcas);
    }

    @PostMapping(value = "**/salvarMarca")
    public ResponseEntity<MarcaProduto> salvarMarcaProduto(@RequestBody @Valid MarcaProduto marcaProduto) throws ExceptionEcommerce {

        if (marcaProduto.getEmpresa().getId() == null || marcaProduto.getEmpresa().getId() <= 0) {
            throw new ExceptionEcommerce("Empresa não informada");
        }

        if (marcaProdutoRepository.existeMarca(marcaProduto.getNomeDesc(), marcaProduto.getEmpresa().getId())) {
            throw new ExceptionEcommerce("Não pode cadastrar duas marcas com o mesmo nome");
        }

        return ResponseEntity.ok(marcaProdutoRepository.save(marcaProduto));
    }

    @DeleteMapping("**/deleteMarcaPorId/{id}")
    public ResponseEntity<String> deletarMarcaProduto(@PathVariable("id") Long id) throws ExceptionEcommerce {
        marcaProdutoRepository.deleteById(id);
        return ResponseEntity.ok("Marca removida");
    }

    @GetMapping(value = "**/obterMarcaProduto/{id}")
    public ResponseEntity<?> obeterMarcaProduto(@PathVariable("id") Long id) throws ExceptionEcommerce {
        MarcaProduto marcaProduto = marcaProdutoRepository.findById(id).orElse(null);

        if (marcaProduto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Marca Produto não encontrado");
        }

        return ResponseEntity.ok(marcaProduto);
    }
}
