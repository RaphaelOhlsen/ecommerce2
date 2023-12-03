package com.mocad.ecommerce.controller;

import com.mocad.ecommerce.ExceptionEcommerce;
import com.mocad.ecommerce.model.CupDesc;
import com.mocad.ecommerce.model.PessoaFisica;
import com.mocad.ecommerce.repository.CupDescRepository;
import com.mocad.ecommerce.repository.PessoaFisicaRepository;
import com.mocad.ecommerce.repository.PessoaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CupDescController {

    @Autowired
    private CupDescRepository cupDescRepository;


    @Autowired
    private PessoaRepository pessoaJuridicaRepository;

    @PostMapping("/salvarCupDesc")
    public ResponseEntity<CupDesc> salvarCupDesc(@RequestBody CupDesc cupDesc){
        return ResponseEntity.ok(cupDescRepository.saveAndFlush(cupDesc));
    }


    @GetMapping("/listarCupDescPorEmpresa/{id}")
    public ResponseEntity<List<CupDesc>> listarCupDescPorEmpresa(@PathVariable("id") Long id) throws ExceptionEcommerce {

        pessoaJuridicaRepository.findById(id).
                orElseThrow(() -> new ExceptionEcommerce("Empresa não encontrada"));

        return ResponseEntity.ok(cupDescRepository.buscarCupDescPorEmpresa(id));
    }

}
