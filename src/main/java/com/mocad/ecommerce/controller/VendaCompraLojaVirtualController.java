package com.mocad.ecommerce.controller;

import com.mocad.ecommerce.ExceptionEcommerce;
import com.mocad.ecommerce.model.Endereco;
import com.mocad.ecommerce.model.ItemVendaLoja;
import com.mocad.ecommerce.model.PessoaFisica;
import com.mocad.ecommerce.model.VendaCompraLojaVirtual;
import com.mocad.ecommerce.model.dto.ItemVendaDTO;
import com.mocad.ecommerce.model.dto.VendaCompraLojaVirtualDTO;
import com.mocad.ecommerce.repository.EnderecoRepository;
import com.mocad.ecommerce.repository.NotaFiscalVendaRepository;
import com.mocad.ecommerce.repository.PessoaFisicaRepository;
import com.mocad.ecommerce.repository.VendaCompraLojaVirtualRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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

        vendaCompraLojaVirtualRepository.findById(idVenda).
                orElseThrow(() -> new ExceptionEcommerce("Venda não encontrada"));

        VendaCompraLojaVirtual vendaCompraLojaVirtual = vendaCompraLojaVirtualRepository.findById(idVenda).get();

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
}
