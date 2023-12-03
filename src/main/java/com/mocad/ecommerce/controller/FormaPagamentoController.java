package com.mocad.ecommerce.controller;

import com.mocad.ecommerce.ExceptionEcommerce;
import com.mocad.ecommerce.model.FormaPagamento;
import com.mocad.ecommerce.model.VendaCompraLojaVirtual;
import com.mocad.ecommerce.model.dto.FormaPagamentoDTO;
import com.mocad.ecommerce.repository.FormaPagamentoRepository;
import com.mocad.ecommerce.repository.PessoaRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
public class FormaPagamentoController {

    @Autowired
    FormaPagamentoRepository formaPagamentoRepository;

    @Autowired
    PessoaRepository pessoaJuridicaRepository;

    private final ModelMapper modelMapper = new ModelMapper();

    @PostMapping("/salvarFormaPagamento")
    public ResponseEntity<FormaPagamento> salvarFormaPagamento(@RequestBody @Valid FormaPagamento formaPagamento) throws ExceptionEcommerce {

        if (formaPagamento.getEmpresa().getId() == null || formaPagamento.getEmpresa().getId() <= 0){
            throw new ExceptionEcommerce("A empresa é obrigatória");
        }

        pessoaJuridicaRepository.findById(formaPagamento.getEmpresa().getId()).
                orElseThrow(() -> new ExceptionEcommerce("Empresa não encontrada"));

        if (formaPagamento.getId() == null || formaPagamento.getId() <= 0){
            List<FormaPagamento> formaPagamentoList = formaPagamentoRepository.buscarFormaPagamentoPorDesc(
                    formaPagamento.getDescricao().toUpperCase().trim(), formaPagamento.getEmpresa().getId()
            );

            if (!formaPagamentoList.isEmpty()){
                throw new ExceptionEcommerce("Forma de pagamento já cadastrada");
            }
        }


        return ResponseEntity.ok(formaPagamentoRepository.saveAndFlush(formaPagamento));
    }

    @GetMapping("/listarTodasFormasPagamento/{id}")
    public ResponseEntity<List<FormaPagamentoDTO>> listarTodasFormasPagamento(@PathVariable("id") Long id){

        List<FormaPagamento> formaPagamento = formaPagamentoRepository.buscarFormaPagamentoPorEmpresa(id);

        List<FormaPagamentoDTO> formaPagamentoDTO = new ArrayList<>();

        formaPagamento.forEach(item -> {
            formaPagamentoDTO.add(modelMapper.map(item, FormaPagamentoDTO.class));
        });

        return ResponseEntity.ok(formaPagamentoDTO);
    }

    @DeleteMapping("/deletarFormaPagamento/{id}")
    public ResponseEntity<String> deletarFormaPagamento(@PathVariable Long id){

        formaPagamentoRepository.findById(id).
                orElseThrow(() -> new RuntimeException("Forma de pagamento não encontrada"));

        formaPagamentoRepository.deleteById(id);
        return ResponseEntity.ok("Forma de pagamento deletada com sucesso");
    }
}
