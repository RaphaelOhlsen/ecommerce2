package com.mocad.ecommerce.controller;

import com.mocad.ecommerce.ExceptionEcommerce;
import com.mocad.ecommerce.model.NotaFiscalVenda;
import com.mocad.ecommerce.repository.NotaFiscalVendaRepository;
import com.mocad.ecommerce.repository.PessoaRepository;
import com.mocad.ecommerce.repository.VendaCompraLojaVirtualRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class NotaFiscalVendaController {

    @Autowired
    private NotaFiscalVendaRepository notaFiscalVendaRepository;

    @Autowired
    private PessoaRepository pessoaJuridicaRepository;

    @Autowired
    private VendaCompraLojaVirtualRepository vendaCompraLojaVirtualRepository;


    @GetMapping("**/buscarNotaFiscalPorVenda/{idVenda}/{idEmpresa}")
    public ResponseEntity<List<NotaFiscalVenda>> buscarNotaFiscalPorVenda(@PathVariable("idVenda") Long idVenda,
                                                                          @PathVariable("idEmpresa") Long idEmpresa)
            throws ExceptionEcommerce {

       vendaCompraLojaVirtualRepository.findById(idVenda).
               orElseThrow(() -> new ExceptionEcommerce("Venda não encontrada"));

       pessoaJuridicaRepository.findById(idEmpresa).
               orElseThrow(() -> new ExceptionEcommerce("Empresa não encontrada"));

       List<NotaFiscalVenda> nota = notaFiscalVendaRepository.buscaNotaPorVenda(idVenda, idEmpresa);

        return ResponseEntity.ok(nota);
    }

    @GetMapping("**/buscarNotaFiscalPorVendaUnica/{idVenda}/{idEmpresa}")
    public ResponseEntity<NotaFiscalVenda> buscarNotaFiscalPorVendaUnica(@PathVariable("idVenda") Long idVenda,
                                                                          @PathVariable("idEmpresa") Long idEmpresa)
            throws ExceptionEcommerce {

        vendaCompraLojaVirtualRepository.findById(idVenda).
                orElseThrow(() -> new ExceptionEcommerce("Venda não encontrada"));

        pessoaJuridicaRepository.findById(idEmpresa).
                orElseThrow(() -> new ExceptionEcommerce("Empresa não encontrada"));

        NotaFiscalVenda nota = notaFiscalVendaRepository.buscaNotaPorVendaUnica(idVenda, idEmpresa);

        return ResponseEntity.ok(nota);
    }
}
