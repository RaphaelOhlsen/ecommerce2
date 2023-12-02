package com.mocad.ecommerce.controller;


import com.mocad.ecommerce.ExceptionEcommerce;
import com.mocad.ecommerce.model.StatusRastreio;
import com.mocad.ecommerce.repository.PessoaRepository;
import com.mocad.ecommerce.repository.StatusRastreioRepository;
import com.mocad.ecommerce.repository.VendaCompraLojaVirtualRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class StatusRastreioController {

    @Autowired
    private StatusRastreioRepository statusRastreioRepository;

    @Autowired
    private VendaCompraLojaVirtualRepository vendaCompraLojaVirtualRepository;

    @Autowired
    private PessoaRepository pessoaJuridicaRepository;

    @GetMapping("/statusRastreioPorVenda/{idVenda}/{idEmpresa}")
    public ResponseEntity<List<StatusRastreio>> statusRastreioPorVenda(@PathVariable Long idVenda, @PathVariable Long idEmpresa)
            throws ExceptionEcommerce {

        vendaCompraLojaVirtualRepository.findById(idVenda).
                orElseThrow(() -> new ExceptionEcommerce("Venda não encontrada"));

        pessoaJuridicaRepository.findById(idEmpresa).
                orElseThrow(() -> new ExceptionEcommerce("Empresa não encontrada"));

        List<StatusRastreio> statusRastreio = statusRastreioRepository.statusRastreioPorVenda(idVenda, idEmpresa);

        return ResponseEntity.ok().body(statusRastreio);
    }
}
