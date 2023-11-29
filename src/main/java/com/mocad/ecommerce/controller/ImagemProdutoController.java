package com.mocad.ecommerce.controller;

import com.mocad.ecommerce.ExceptionEcommerce;
import com.mocad.ecommerce.model.ImagemProduto;

import com.mocad.ecommerce.model.dto.ImagemProdutoDTO;
import com.mocad.ecommerce.repository.ImagemProdutoRepository;
import com.mocad.ecommerce.repository.PessoaRepository;
import com.mocad.ecommerce.repository.ProdutoRepository;
import com.mocad.ecommerce.service.ImagemProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ImagemProdutoController {

    @Autowired
    private ImagemProdutoRepository imagemProdutoRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private ImagemProdutoService imagemProdutoService;

    @GetMapping("/obterImagemPorProduto/{idProduto}")
    public ResponseEntity<List<ImagemProdutoDTO>> obterImagemPorProduto(@PathVariable("idProduto") Long idProduto) {
        List<ImagemProdutoDTO> dtos = new ArrayList<ImagemProdutoDTO>();

        List<ImagemProduto> imagemProdutos = imagemProdutoRepository.buscaImagemProduto(idProduto);

        for (ImagemProduto imagemProduto : imagemProdutos) {

            ImagemProdutoDTO imagemProdutoDTO = new ImagemProdutoDTO();
            imagemProdutoDTO.setId(imagemProduto.getId());
            imagemProdutoDTO.setEmpresa(imagemProduto.getEmpresa().getId());
            imagemProdutoDTO.setProduto(imagemProduto.getProduto().getId());
            imagemProdutoDTO.setImagemMiniatura(imagemProduto.getImagemMiniatura());
            imagemProdutoDTO.setImagemOriginal(imagemProduto.getImagemOriginal());

            dtos.add(imagemProdutoDTO);
        }

        return ResponseEntity.ok(dtos);

    }

    @DeleteMapping("/deleteImagemProduto/{id}")
    public ResponseEntity<String> deleteImagemProduto(@PathVariable("id") Long id) throws ExceptionEcommerce {

        imagemProdutoRepository.findById(id).
                orElseThrow(() -> new ExceptionEcommerce("Imagem não encontrada"));

        imagemProdutoRepository.deleteById(id);

        return ResponseEntity.ok("Imagem apagada com sucesso");
    }

    @DeleteMapping("/deleteTodasImagensPorProduto/{idProduto}")
    public ResponseEntity<String> deleteTodasImagensPorProduto(@PathVariable("idProduto") Long idProduto) throws ExceptionEcommerce {

        produtoRepository.findById(idProduto).
                orElseThrow(() -> new ExceptionEcommerce("Produto não encontrado"));

        imagemProdutoRepository.apagarImagensPorProduto(idProduto);

        return ResponseEntity.ok("Imagens apagadas com sucesso");
    }

    @ResponseBody
    @PostMapping(value = "**/salvarImagemProduto")
    public ResponseEntity<ImagemProdutoDTO> salvarImagemProduto(@RequestBody @Valid ImagemProduto imagemProduto)
            throws ExceptionEcommerce, IOException {

        if (imagemProduto.getProduto() == null || imagemProduto.getProduto().getId() <= 0) {
            throw new ExceptionEcommerce("O campo produto é obigatório");
        }

        if (imagemProduto.getEmpresa() == null || imagemProduto.getEmpresa().getId() <= 0) {
            throw new ExceptionEcommerce("O campo empresa é obigatório");
        }


        pessoaRepository.findById(imagemProduto.getEmpresa().getId()).
                orElseThrow(() -> new ExceptionEcommerce("Empresa não encontrada"));

        produtoRepository.findById(imagemProduto.getProduto().getId()).
                orElseThrow(() -> new ExceptionEcommerce("Produto não encontrado"));

        String imagemMiniatura = imagemProdutoService.miniaturaImagem(imagemProduto.getImagemOriginal());

        imagemProduto.setImagemMiniatura(imagemMiniatura);

        imagemProduto = imagemProdutoRepository.save(imagemProduto);


        ImagemProdutoDTO imagemProdutoDTO = new ImagemProdutoDTO();
        imagemProdutoDTO.setId(imagemProduto.getId());
        imagemProdutoDTO.setEmpresa(imagemProduto.getEmpresa().getId());
        imagemProdutoDTO.setProduto(imagemProduto.getProduto().getId());
        imagemProdutoDTO.setImagemMiniatura(imagemProduto.getImagemMiniatura());
        imagemProdutoDTO.setImagemOriginal(imagemProduto.getImagemOriginal());

        return ResponseEntity.ok(imagemProdutoDTO);

    }

}
