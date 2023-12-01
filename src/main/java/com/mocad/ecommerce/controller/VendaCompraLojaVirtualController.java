package com.mocad.ecommerce.controller;

import com.mocad.ecommerce.ExceptionEcommerce;
import com.mocad.ecommerce.model.*;
import com.mocad.ecommerce.model.dto.ItemVendaDTO;
import com.mocad.ecommerce.model.dto.VendaCompraLojaVirtualDTO;
import com.mocad.ecommerce.repository.*;
import com.mocad.ecommerce.service.VendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
public class VendaCompraLojaVirtualController {

    @Autowired
    private VendaCompraLojaVirtualRepository vendaCompraLojaVirtualRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private PessoaController pessoaController;

    @Autowired
    private NotaFiscalVendaRepository notaFiscalVendaRepository;

    @Autowired
    private StatusRastreioRepository statusRastreioRepository;

    @Autowired
    private VendaService vendaService;

    @PostMapping("/salvarVendaLoja")
    public ResponseEntity<VendaCompraLojaVirtualDTO> salvarVenda(@RequestBody @Valid VendaCompraLojaVirtual vendaCompraLojaVirtual) throws ExceptionEcommerce {

//        if (vendaCompraLojaVirtual.getPessoa() == null || vendaCompraLojaVirtual.getPessoa().getId() <= 0){
//            throw new ExceptionEcommerce("Pessoa não informada ou inválida");
//        }

//        if (vendaCompraLojaVirtual.getEmpresa() == null || vendaCompraLojaVirtual.getEmpresa().getId() <= 0){
//            throw new ExceptionEcommerce("Produto não informado ou inválido");
//        }
//
//        if (vendaCompraLojaVirtual.getEnderecoEntrega() == null || vendaCompraLojaVirtual.getEnderecoEntrega().getId() <= 0){
//            throw new ExceptionEcommerce("Endereço de entrega não informado ou inválido");
//        }
//
//        if (vendaCompraLojaVirtual.getEnderecoCobranca() == null || vendaCompraLojaVirtual.getEnderecoCobranca().getId() <= 0){
//            throw new ExceptionEcommerce("Endereço de cobrança não informado ou inválido");
//        }
//
//        if (vendaCompraLojaVirtual.getNotaFiscalVenda() == null || vendaCompraLojaVirtual.getNotaFiscalVenda().getId() <= 0){
//            throw new ExceptionEcommerce("Nota fiscal não informada ou inválida");
//        }
//
//        if (vendaCompraLojaVirtual.getFormapagamento() == null || vendaCompraLojaVirtual.getFormapagamento().getId() <= 0){
//            throw new ExceptionEcommerce("Forma de pagamento não informada ou inválida");
//        }


        vendaCompraLojaVirtual.getPessoa().setEmpresa(vendaCompraLojaVirtual.getEmpresa());
        PessoaFisica pessoaFisica = pessoaController.salvarPF(vendaCompraLojaVirtual.getPessoa()).getBody();
        vendaCompraLojaVirtual.setPessoa(pessoaFisica);

        vendaCompraLojaVirtual.getEnderecoCobranca().setPessoa(pessoaFisica);
        vendaCompraLojaVirtual.getEnderecoCobranca().setEmpresa(vendaCompraLojaVirtual.getEmpresa());
        Endereco enderecoCobranca = enderecoRepository.save(vendaCompraLojaVirtual.getEnderecoCobranca());
        vendaCompraLojaVirtual.setEnderecoCobranca(enderecoCobranca);

        vendaCompraLojaVirtual.getEnderecoEntrega().setPessoa(pessoaFisica);
        vendaCompraLojaVirtual.getEnderecoEntrega().setEmpresa(vendaCompraLojaVirtual.getEmpresa());
        Endereco enderecoEntrega = enderecoRepository.save(vendaCompraLojaVirtual.getEnderecoEntrega());
        vendaCompraLojaVirtual.setEnderecoEntrega(enderecoEntrega);

        vendaCompraLojaVirtual.getNotaFiscalVenda().setEmpresa(vendaCompraLojaVirtual.getEmpresa());

        for (int i = 0; i < vendaCompraLojaVirtual.getItemVendaLojas().size(); i++) {
            vendaCompraLojaVirtual.getItemVendaLojas().get(i).setEmpresa(vendaCompraLojaVirtual.getEmpresa());
            vendaCompraLojaVirtual.getItemVendaLojas().get(i).setVendaCompraLojaVirtual(vendaCompraLojaVirtual);
        }


        /* Salva primeiro a venda e todos os dados */
        vendaCompraLojaVirtual = vendaCompraLojaVirtualRepository.save(vendaCompraLojaVirtual);

        /* Salva o status de rastreio */
        StatusRastreio statusRastreio = new StatusRastreio();
        statusRastreio.setCentroDistribuicao("Serra");
        statusRastreio.setCidade("Serra");
        statusRastreio.setEstado("ES");
        statusRastreio.setStatus("Em separação");
        statusRastreio.setVendaCompraLojaVirtual(vendaCompraLojaVirtual);
        statusRastreio.setEmpresa(vendaCompraLojaVirtual.getEmpresa());
        statusRastreioRepository.save(statusRastreio);



        /* Associa a venda gravada no banco com a nota fiscal */
        vendaCompraLojaVirtual.getNotaFiscalVenda().setVendaCompraLojaVirtual(vendaCompraLojaVirtual);


        /* Persiste novamente a nota fiscal novamente para ficar amarrada na venda*/
        notaFiscalVendaRepository.saveAndFlush(vendaCompraLojaVirtual.getNotaFiscalVenda());

        VendaCompraLojaVirtualDTO vendaCompraLojaVirtualDTO = new VendaCompraLojaVirtualDTO();

        vendaCompraLojaVirtualDTO.setValorTotal(vendaCompraLojaVirtual.getValorTotal());
        vendaCompraLojaVirtualDTO.setPessoa(vendaCompraLojaVirtual.getPessoa());
        vendaCompraLojaVirtualDTO.setId(vendaCompraLojaVirtual.getId());
        vendaCompraLojaVirtualDTO.setEnderecoCobranca(vendaCompraLojaVirtual.getEnderecoCobranca());
        vendaCompraLojaVirtualDTO.setEnderecoEntrega(vendaCompraLojaVirtual.getEnderecoEntrega());
        vendaCompraLojaVirtualDTO.setValorDesconto(vendaCompraLojaVirtual.getValorDesconto());
        vendaCompraLojaVirtualDTO.setValorFrete(vendaCompraLojaVirtual.getValorFrete());

        for (ItemVendaLoja item: vendaCompraLojaVirtual.getItemVendaLojas()) {

            ItemVendaDTO itemVendaDTO = new ItemVendaDTO();
            itemVendaDTO.setId(item.getId());
            itemVendaDTO.setProduto(item.getProduto());
            itemVendaDTO.setQuantidade(item.getQuantidade());

            vendaCompraLojaVirtualDTO.getItemVendaLoja().add(itemVendaDTO);
        }

        return ResponseEntity.ok(vendaCompraLojaVirtualDTO);
    }

    @GetMapping("/consultaVenda/{id}")
    public ResponseEntity<VendaCompraLojaVirtualDTO> consultaVenda(@PathVariable("id") Long idVenda) throws ExceptionEcommerce {

        VendaCompraLojaVirtual vendaCompraLojaVirtual = vendaCompraLojaVirtualRepository.findByIdExclusao(idVenda);

        if (vendaCompraLojaVirtual == null){
            vendaCompraLojaVirtual = new VendaCompraLojaVirtual();
        }

        VendaCompraLojaVirtualDTO vendaCompraLojaVirtualDTO = new VendaCompraLojaVirtualDTO();

        vendaCompraLojaVirtualDTO.setValorTotal(vendaCompraLojaVirtual.getValorTotal());
        vendaCompraLojaVirtualDTO.setPessoa(vendaCompraLojaVirtual.getPessoa());
        vendaCompraLojaVirtualDTO.setId(vendaCompraLojaVirtual.getId());
        vendaCompraLojaVirtualDTO.setEnderecoCobranca(vendaCompraLojaVirtual.getEnderecoCobranca());
        vendaCompraLojaVirtualDTO.setEnderecoEntrega(vendaCompraLojaVirtual.getEnderecoEntrega());
        vendaCompraLojaVirtualDTO.setValorDesconto(vendaCompraLojaVirtual.getValorDesconto());
        vendaCompraLojaVirtualDTO.setValorFrete(vendaCompraLojaVirtual.getValorFrete());

        for (ItemVendaLoja item: vendaCompraLojaVirtual.getItemVendaLojas()) {

            ItemVendaDTO itemVendaDTO = new ItemVendaDTO();
            itemVendaDTO.setId(item.getId());
            itemVendaDTO.setProduto(item.getProduto());
            itemVendaDTO.setQuantidade(item.getQuantidade());

            vendaCompraLojaVirtualDTO.getItemVendaLoja().add(itemVendaDTO);
        }

        return ResponseEntity.ok(vendaCompraLojaVirtualDTO);
    }

    @DeleteMapping("/deleteVendaTotalBanco/{id}")
    public ResponseEntity<String> deleteVendaTotalBanco(@PathVariable("id") Long idVenda) throws ExceptionEcommerce {

        vendaCompraLojaVirtualRepository.findById(idVenda).
                orElseThrow(() -> new ExceptionEcommerce("Venda não encontrada"));

        vendaService.exclusaoTotalVendaBanco(idVenda);

        return ResponseEntity.ok("Venda excluída com sucesso");
    }

    @DeleteMapping("/deleteVendaTotalBanco2/{id}")
    public ResponseEntity<String> deleteVendaTotalBanco2(@PathVariable("id") Long idVenda) throws ExceptionEcommerce {

        if (vendaCompraLojaVirtualRepository.findByIdExclusao(idVenda) == null) {
            throw new ExceptionEcommerce("Venda não encontrada");
        }


        vendaCompraLojaVirtualRepository.deleteByIdLogico(idVenda);

        return ResponseEntity.ok("Venda excluída com sucesso");
    }

    @GetMapping("/consultaVendasPorProduto")
    public ResponseEntity<List<VendaCompraLojaVirtualDTO>> consultaVendasPorProduto(@RequestParam("idProduto") Long idProduto,
                                                                                    @RequestParam("idEmpresa") Long idEmpresa) {

        List<VendaCompraLojaVirtual> vendaCompraLojaVirtual = vendaCompraLojaVirtualRepository.vendaPorProduto(idProduto, idEmpresa);

        if (vendaCompraLojaVirtual == null){
            vendaCompraLojaVirtual = new ArrayList<>();
        }

        List<VendaCompraLojaVirtualDTO> compraLojaVirtualDTOList = new ArrayList<>();

        for (VendaCompraLojaVirtual vcl : vendaCompraLojaVirtual) {
            VendaCompraLojaVirtualDTO vendaCompraLojaVirtualDTO = new VendaCompraLojaVirtualDTO();

            vendaCompraLojaVirtualDTO.setValorTotal(vcl.getValorTotal());
            vendaCompraLojaVirtualDTO.setPessoa(vcl.getPessoa());
            vendaCompraLojaVirtualDTO.setId(vcl.getId());
            vendaCompraLojaVirtualDTO.setEnderecoCobranca(vcl.getEnderecoCobranca());
            vendaCompraLojaVirtualDTO.setEnderecoEntrega(vcl.getEnderecoEntrega());
            vendaCompraLojaVirtualDTO.setValorDesconto(vcl.getValorDesconto());
            vendaCompraLojaVirtualDTO.setValorFrete(vcl.getValorFrete());

            for (ItemVendaLoja item: vcl.getItemVendaLojas()) {

                ItemVendaDTO itemVendaDTO = new ItemVendaDTO();
                itemVendaDTO.setId(item.getId());
                itemVendaDTO.setProduto(item.getProduto());
                itemVendaDTO.setQuantidade(item.getQuantidade());

                vendaCompraLojaVirtualDTO.getItemVendaLoja().add(itemVendaDTO);
            }

            compraLojaVirtualDTOList.add(vendaCompraLojaVirtualDTO);
        }



        return ResponseEntity.ok(compraLojaVirtualDTOList);
    }
}
