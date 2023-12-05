package com.mocad.ecommerce.controller;

import com.mocad.ecommerce.ExceptionEcommerce;
import com.mocad.ecommerce.model.NotaFiscalVenda;
import com.mocad.ecommerce.model.dto.RelatorioNFStatusDTO;
import com.mocad.ecommerce.repository.NotaFiscalVendaRepository;
import com.mocad.ecommerce.repository.PessoaRepository;
import com.mocad.ecommerce.repository.VendaCompraLojaVirtualRepository;
import com.mocad.ecommerce.service.NotaFiscalVendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class NotaFiscalVendaController {

    @Autowired
    private NotaFiscalVendaRepository notaFiscalVendaRepository;

    @Autowired
    private NotaFiscalVendaService notaFiscalVendaService;

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

    /**
     * Relatório de Nota Fiscal por Status, retorna uma lista de Nota Fiscal de Venda com condições de Status e Data
     * @param relatorioNFStatusDTO
     * @return relatorioNFStatusDTO
     */
    @GetMapping("/relatorioNotaVendaStatus")
    private ResponseEntity<List<RelatorioNFStatusDTO>> relatorioNFStatus(@RequestBody @Valid RelatorioNFStatusDTO relatorioNFStatusDTO) {
        return ResponseEntity.ok(notaFiscalVendaService.relatorioNFStatus(relatorioNFStatusDTO));
    }
}
