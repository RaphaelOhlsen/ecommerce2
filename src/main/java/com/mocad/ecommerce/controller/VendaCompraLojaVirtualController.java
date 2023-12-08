package com.mocad.ecommerce.controller;

import antlr.TokenStreamRewriteEngine;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mocad.ecommerce.ExceptionEcommerce;
import com.mocad.ecommerce.enums.StatusContaReceber;
import com.mocad.ecommerce.env.ApiTokenIntegracao;
import com.mocad.ecommerce.model.*;
import com.mocad.ecommerce.model.dto.*;
import com.mocad.ecommerce.repository.*;
import com.mocad.ecommerce.service.ServiceSendEmail;
import com.mocad.ecommerce.service.VendaService;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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

  @Autowired
  private ContaReceberRepository contaReceberRepository;

  @Autowired
  private ServiceSendEmail serviceSendEmail;

  @Autowired
  private JdbcTemplate jdbcTemplate;


  private final ModelMapper modelMapper = new ModelMapper();


  @PostMapping("/salvarVendaLoja")
  public ResponseEntity<VendaCompraLojaVirtualDTO> salvarVenda(
      @RequestBody @Valid VendaCompraLojaVirtual vendaCompraLojaVirtual)
      throws ExceptionEcommerce, MessagingException, UnsupportedEncodingException {

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

    /* Usando ModelMapper para converter VendaCompraLojaVirtual para VendaCompraLojaVirtualDTO */

    VendaCompraLojaVirtualDTO vendaCompraLojaVirtualDTO = modelMapper.map(vendaCompraLojaVirtual, VendaCompraLojaVirtualDTO.class);



    /* Usando Boiller Plate para converter VendaCompraLojaVirtual para VendaCompraLojaVirtualDTO */

//        VendaCompraLojaVirtualDTO vendaCompraLojaVirtualDTO = new VendaCompraLojaVirtualDTO();
//        vendaCompraLojaVirtualDTO.setValorTotal(vendaCompraLojaVirtual.getValorTotal());
//        vendaCompraLojaVirtualDTO.setPessoa(vendaCompraLojaVirtual.getPessoa());
//
//        vendaCompraLojaVirtualDTO.setEnderecoCobranca(vendaCompraLojaVirtual.getEnderecoCobranca());
//        vendaCompraLojaVirtualDTO.setEnderecoEntrega(vendaCompraLojaVirtual.getEnderecoEntrega());
//
//        vendaCompraLojaVirtualDTO.setValorDesconto(vendaCompraLojaVirtual.getValorDesconto());
//        vendaCompraLojaVirtualDTO.setValorFrete(vendaCompraLojaVirtual.getValorFrete());
//        vendaCompraLojaVirtualDTO.setId(vendaCompraLojaVirtual.getId());

//        for (ItemVendaLoja item: vendaCompraLojaVirtual.getItemVendaLojas()) {
//
////            ItemVendaDTO itemVendaDTO = modelMapper.map(item, ItemVendaDTO.class);
//            ItemVendaDTO itemVendaDTO = new ItemVendaDTO();
//            itemVendaDTO.setId(item.getId());
//            itemVendaDTO.setProduto(item.getProduto());
//            itemVendaDTO.setQuantidade(item.getQuantidade());
//
//            vendaCompraLojaVirtualDTO.getItemVendaLojas().add(itemVendaDTO);
//        }

    ContaReceber contaReceber = new ContaReceber();
    contaReceber.setDescricao("Venda da loja virtual nº: " + vendaCompraLojaVirtual.getId());
    contaReceber.setDtPagamento(Calendar.getInstance().getTime());
    contaReceber.setDtVencimento(Calendar.getInstance().getTime());
    contaReceber.setEmpresa(vendaCompraLojaVirtual.getEmpresa());
    contaReceber.setPessoa(vendaCompraLojaVirtual.getPessoa());
    contaReceber.setStatus(StatusContaReceber.QUITADA);
    contaReceber.setValorDesconto(vendaCompraLojaVirtual.getValorDesconto());
    contaReceber.setValorTotal(vendaCompraLojaVirtual.getValorTotal());

    contaReceberRepository.saveAndFlush(contaReceber);

    /*Email para o comprador*/
    StringBuilder msgemail = new StringBuilder();
    msgemail.append("Olá, ").append(pessoaFisica.getNome()).append("</br>");
    msgemail.append("Você realizou a compra de nº: ").append(vendaCompraLojaVirtual.getId()).append("</br>");
    msgemail.append("Na loja ").append(vendaCompraLojaVirtual.getEmpresa().getNomeFantasia());
    /*assunto, msg, destino*/
    serviceSendEmail.enviarEmailHtml("Compra Realizada", msgemail.toString(), pessoaFisica.getEmail());

    /*Email para o vendedor*/
    msgemail = new StringBuilder();
    msgemail.append("Você realizou uma venda, nº ").append(vendaCompraLojaVirtual.getId());
    serviceSendEmail.enviarEmailHtml("Venda Realizada", msgemail.toString(), vendaCompraLojaVirtual.getEmpresa().getEmail());


    return ResponseEntity.ok(vendaCompraLojaVirtualDTO);
  }

  @GetMapping("/consultaVenda/{id}")
  public ResponseEntity<VendaCompraLojaVirtualDTO> consultaVenda(@PathVariable("id") Long idVenda) throws ExceptionEcommerce {

    VendaCompraLojaVirtual vendaCompraLojaVirtual = vendaCompraLojaVirtualRepository.findByIdExclusao(idVenda);

    if (vendaCompraLojaVirtual == null) {
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

    for (ItemVendaLoja item : vendaCompraLojaVirtual.getItemVendaLojas()) {

      ItemVendaDTO itemVendaDTO = new ItemVendaDTO();
      itemVendaDTO.setId(item.getId());
      itemVendaDTO.setProduto(item.getProduto());
      itemVendaDTO.setQuantidade(item.getQuantidade());

      vendaCompraLojaVirtualDTO.getItemVendaLojas().add(itemVendaDTO);
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

    if (vendaCompraLojaVirtual == null) {
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

      for (ItemVendaLoja item : vcl.getItemVendaLojas()) {

        ItemVendaDTO itemVendaDTO = new ItemVendaDTO();
        itemVendaDTO.setId(item.getId());
        itemVendaDTO.setProduto(item.getProduto());
        itemVendaDTO.setQuantidade(item.getQuantidade());

        vendaCompraLojaVirtualDTO.getItemVendaLojas().add(itemVendaDTO);
      }

      compraLojaVirtualDTOList.add(vendaCompraLojaVirtualDTO);
    }


    return ResponseEntity.ok(compraLojaVirtualDTOList);
  }

  @GetMapping("/consultaVendaDinamica/{valor}/{tipoConsulta}/{idEmpresa}")
  public ResponseEntity<List<VendaCompraLojaVirtualDTO>> consultaVendaDinamica(@PathVariable("valor") String valor,
                                                                               @PathVariable("tipoConsulta") String tipoConsulta,
                                                                               @PathVariable("idEmpresa") Long idEmpresa) {

    List<VendaCompraLojaVirtual> vendaCompraLojaVirtual = null;

    if (tipoConsulta.equalsIgnoreCase("POR_ID_PROD")) {
      vendaCompraLojaVirtual = vendaCompraLojaVirtualRepository.vendaPorProduto(Long.parseLong(valor), idEmpresa);
    } else if (tipoConsulta.equalsIgnoreCase("POR_NOME_PROD")) {
      vendaCompraLojaVirtual = vendaCompraLojaVirtualRepository.vendaPorNomeProduto(valor.trim().toUpperCase(), idEmpresa);
    } else if (tipoConsulta.equalsIgnoreCase("POR_NOME_CLIENTE")) {
      vendaCompraLojaVirtual = vendaCompraLojaVirtualRepository.vendaPorNomeCliente(valor.trim().toUpperCase(), idEmpresa);
    } else if (tipoConsulta.equalsIgnoreCase("POR_ENDERECO_COBRANCA")) {
      vendaCompraLojaVirtual = vendaCompraLojaVirtualRepository.vendaPorEnderecoCobranca(valor.trim().toUpperCase(), idEmpresa);
    } else if (tipoConsulta.equalsIgnoreCase("POR_CPF_CLIENTE")) {
      vendaCompraLojaVirtual = vendaCompraLojaVirtualRepository.vendaPorCpfCliente(valor, idEmpresa);
    } else if (tipoConsulta.equalsIgnoreCase("POR_ID_CLIENTE")) {
      vendaCompraLojaVirtual = vendaCompraLojaVirtualRepository.vendaPorCliente(Long.parseLong(valor));
    }

    if (vendaCompraLojaVirtual == null) {
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

      for (ItemVendaLoja item : vcl.getItemVendaLojas()) {

        ItemVendaDTO itemVendaDTO = new ItemVendaDTO();
        itemVendaDTO.setId(item.getId());
        itemVendaDTO.setProduto(item.getProduto());
        itemVendaDTO.setQuantidade(item.getQuantidade());

        vendaCompraLojaVirtualDTO.getItemVendaLojas().add(itemVendaDTO);
      }

      compraLojaVirtualDTOList.add(vendaCompraLojaVirtualDTO);
    }
    return ResponseEntity.ok(compraLojaVirtualDTOList);
  }

  @GetMapping("/consultaVendaFaixaData/{data1}/{data2}/{idEmpresa}")
  public ResponseEntity<List<VendaCompraLojaVirtualDTO>>
  consultaVendaFaixaData(
      @PathVariable("data1") String data1,
      @PathVariable("data2") String data2,
      @PathVariable("idEmpresa") Long idEmpresa) throws ParseException {

    List<VendaCompraLojaVirtual> vendaCompraLojaVirtual = null;

    SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");

    Date d1 = formato.parse(data1);

    Date d2 = formato.parse(data2);


    vendaCompraLojaVirtual = vendaCompraLojaVirtualRepository.vendaPorFaixaData(d1, d2, idEmpresa);

    if (vendaCompraLojaVirtual == null) {
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

      for (ItemVendaLoja item : vcl.getItemVendaLojas()) {

        ItemVendaDTO itemVendaDTO = new ItemVendaDTO();
        itemVendaDTO.setId(item.getId());
        itemVendaDTO.setProduto(item.getProduto());
        itemVendaDTO.setQuantidade(item.getQuantidade());

        vendaCompraLojaVirtualDTO.getItemVendaLojas().add(itemVendaDTO);
      }

      compraLojaVirtualDTOList.add(vendaCompraLojaVirtualDTO);
    }

    return ResponseEntity.ok(compraLojaVirtualDTOList);

  }

  @Transactional
  @ResponseBody
  @GetMapping(value = "**/imprimeCompraEtiquetaFrete/{idVenda}")
  public ResponseEntity<String> imprimeCompraEtiquetaFrete(@PathVariable Long idVenda) throws ExceptionEcommerce, IOException {

    VendaCompraLojaVirtual compraLojaVirtual = vendaCompraLojaVirtualRepository.findById(idVenda).orElseGet(null);

    if (compraLojaVirtual == null) {
      throw new ExceptionEcommerce("Venda não encontrada");
    }

//    List<Endereco> enderecos = enderecoRepository.enderecoPj(compraLojaVirtual.getEmpresa().getId());
//    compraLojaVirtual.getEmpresa().setEnderecos(enderecos);

    EnvioEtiquetaDTO envioEtiquetaDTO = new EnvioEtiquetaDTO();

    envioEtiquetaDTO.setService(compraLojaVirtual.getServicoTransportadora());
    envioEtiquetaDTO.setAgency("49");
    envioEtiquetaDTO.getFrom().setName(compraLojaVirtual.getEmpresa().getNome());
    envioEtiquetaDTO.getFrom().setPhone(compraLojaVirtual.getEmpresa().getTelefone());
    envioEtiquetaDTO.getFrom().setEmail(compraLojaVirtual.getEmpresa().getEmail());
    envioEtiquetaDTO.getFrom().setCompany_document(compraLojaVirtual.getEmpresa().getCnpj());
    envioEtiquetaDTO.getFrom().setState_register(compraLojaVirtual.getEmpresa().getInscEstadual());
    envioEtiquetaDTO.getFrom().setAddress(compraLojaVirtual.getEmpresa().getEnderecos().get(0).getRuaLogra());
    envioEtiquetaDTO.getFrom().setComplement(compraLojaVirtual.getEmpresa().getEnderecos().get(0).getComplemento());
    envioEtiquetaDTO.getFrom().setNumber(compraLojaVirtual.getEmpresa().getEnderecos().get(0).getNumero());
    envioEtiquetaDTO.getFrom().setDistrict(compraLojaVirtual.getEmpresa().getEnderecos().get(0).getBairro());
    envioEtiquetaDTO.getFrom().setCity(compraLojaVirtual.getEmpresa().getEnderecos().get(0).getCidade());
    envioEtiquetaDTO.getFrom().setCountry_id(compraLojaVirtual.getEmpresa().getEnderecos().get(0).getCountry_id());
    envioEtiquetaDTO.getFrom().setPostal_code(compraLojaVirtual.getEmpresa().getEnderecos().get(0).getCep());
    envioEtiquetaDTO.getFrom().setState_abbr(compraLojaVirtual.getEmpresa().getEnderecos().get(0).getEstado());
    envioEtiquetaDTO.getFrom().setNote("Não há");


    envioEtiquetaDTO.getTo().setName(compraLojaVirtual.getPessoa().getNome());
    envioEtiquetaDTO.getTo().setPhone(compraLojaVirtual.getPessoa().getTelefone());
    envioEtiquetaDTO.getTo().setEmail(compraLojaVirtual.getPessoa().getEmail());
    envioEtiquetaDTO.getTo().setDocument(compraLojaVirtual.getPessoa().getCpf());
    envioEtiquetaDTO.getTo().setAddress(compraLojaVirtual.getPessoa().enderecoEntrega().getRuaLogra());
    envioEtiquetaDTO.getTo().setComplement(compraLojaVirtual.getPessoa().enderecoEntrega().getComplemento());
    envioEtiquetaDTO.getTo().setNumber(compraLojaVirtual.getPessoa().enderecoEntrega().getNumero());
    envioEtiquetaDTO.getTo().setDistrict(compraLojaVirtual.getPessoa().enderecoEntrega().getBairro());
    envioEtiquetaDTO.getTo().setCity(compraLojaVirtual.getPessoa().enderecoEntrega().getCidade());
    envioEtiquetaDTO.getTo().setState_abbr(compraLojaVirtual.getPessoa().enderecoEntrega().getEstado());
    envioEtiquetaDTO.getTo().setCountry_id(compraLojaVirtual.getPessoa().enderecoEntrega().getCountry_id());
    envioEtiquetaDTO.getTo().setPostal_code(compraLojaVirtual.getPessoa().enderecoEntrega().getCep());
    envioEtiquetaDTO.getTo().setNote("Não há");


    List<ProductsEnvioEtiquetaDTO> products = new ArrayList<ProductsEnvioEtiquetaDTO>();

    for (ItemVendaLoja itemVendaLoja : compraLojaVirtual.getItemVendaLojas()) {

      ProductsEnvioEtiquetaDTO dto = new ProductsEnvioEtiquetaDTO();

      dto.setName(itemVendaLoja.getProduto().getNome());
      dto.setQuantity(itemVendaLoja.getQuantidade().toString());
      dto.setUnitary_value("" + itemVendaLoja.getProduto().getValorVenda().doubleValue());

      products.add(dto);
    }


    envioEtiquetaDTO.setProducts(products);


    List<VolumesEnvioEtiquetaDTO> volumes = new ArrayList<VolumesEnvioEtiquetaDTO>();

    for (ItemVendaLoja itemVendaLoja : compraLojaVirtual.getItemVendaLojas()) {

      VolumesEnvioEtiquetaDTO dto = new VolumesEnvioEtiquetaDTO();

      dto.setHeight(itemVendaLoja.getProduto().getAltura().toString());
      dto.setLength(itemVendaLoja.getProduto().getProfundidade().toString());
      dto.setWeight(itemVendaLoja.getProduto().getPeso().toString());
      dto.setWidth(itemVendaLoja.getProduto().getLargura().toString());

      volumes.add(dto);
    }


    envioEtiquetaDTO.setVolumes(volumes);

    //Prestar atenção no campo abaixo de insurance_value, pois ele pode ser de outro valor, verificar na documentação
    envioEtiquetaDTO.getOptions().setInsurance_value(12);
    envioEtiquetaDTO.getOptions().setReceipt(true);
    envioEtiquetaDTO.getOptions().setOwn_hand(true);
    envioEtiquetaDTO.getOptions().setReverse(true);
    envioEtiquetaDTO.getOptions().setNon_commercial(true);
    envioEtiquetaDTO.getOptions().getInvoice().setKey("31190307586261000184550010000092481404848162");
    envioEtiquetaDTO.getOptions().setPlatform(compraLojaVirtual.getEmpresa().getNomeFantasia());

    TagsEnvioDTO dtoTagEnvio = new TagsEnvioDTO();
    dtoTagEnvio.setTag("Identificação do pedido na plataforma, exemplo:" + compraLojaVirtual.getId());
    dtoTagEnvio.setUrl(null);
    envioEtiquetaDTO.getOptions().getTags().add(dtoTagEnvio);


    String jsonEnvio = new ObjectMapper().writeValueAsString(envioEtiquetaDTO);


    OkHttpClient client = new OkHttpClient().newBuilder().build();
    okhttp3.MediaType mediaType = okhttp3.MediaType.parse("application/json");
    okhttp3.RequestBody body = okhttp3.RequestBody.create(mediaType, jsonEnvio);
    okhttp3.Request request = new okhttp3.Request.Builder()
        .url(ApiTokenIntegracao.URL_MELHOR_ENVIO_SAND_BOX + "api/v2/me/cart")
        .method("POST", body)
        .addHeader("Accept", "application/json")
        .addHeader("Content-Type", "application/json")
        .addHeader("Authorization", "Bearer " + ApiTokenIntegracao.TOKEN_MELHOR_ENVIO_SAND_BOX)
        .addHeader("User-Agent", "suporte@jdevtreinamento.com.br")
        .build();

    okhttp3.Response response = client.newCall(request).execute();

    String respostaJson = response.body().string();

    if (respostaJson.contains("error")) {
      throw new ExceptionEcommerce(respostaJson);
    }

    JsonNode jsonNode = new ObjectMapper().readTree(respostaJson);

    Iterator<JsonNode> iterator = jsonNode.iterator();

    String idEtiqueta = "";

    while(iterator.hasNext()) {
      JsonNode node = iterator.next();
      if (node.get("id") != null) {
        idEtiqueta = node.get("id").asText();
      }else {
        idEtiqueta= node.asText();
      }
      break;
    }

    System.out.println("ID da etiqueta: " + idEtiqueta);
    System.out.println("Compra: " + compraLojaVirtual.getId());


//    jdbcTemplate.execute("begin; update vd_cp_loja_virt set codigo_etiqueta = '"+idEtiqueta+"' where id = "+compraLojaVirtual.getId()+"  ;commit;");
    /*Salvando o código da etiqueta*/
    vendaCompraLojaVirtualRepository.updateEtiqueta(idEtiqueta, compraLojaVirtual.getId());


    OkHttpClient clientCompra = new OkHttpClient().newBuilder().build();
    okhttp3.MediaType mediaTypeC = okhttp3.MediaType.parse("application/json");
    okhttp3.RequestBody bodyC = okhttp3.RequestBody.create(mediaTypeC, "{\n    \"orders\": [\n        \"" + idEtiqueta + "\"\n    ]\n}");
    okhttp3.Request requestC = new okhttp3.Request.Builder()
        .url(ApiTokenIntegracao.URL_MELHOR_ENVIO_SAND_BOX + "api/v2/me/shipment/checkout")
        .method("POST", bodyC)
        .addHeader("Accept", "application/json")
        .addHeader("Content-Type", "application/json")
        .addHeader("Authorization", "Bearer " + ApiTokenIntegracao.TOKEN_MELHOR_ENVIO_SAND_BOX)
        .addHeader("User-Agent", "suporte@jdevtreinamento.com.br")
        .build();

    okhttp3.Response responseC = clientCompra.newCall(requestC).execute();

    if (!responseC.isSuccessful()) {
      throw new ExceptionEcommerce("Não foi possível realizar a compra da etiqueta");
    }

    OkHttpClient clientGe = new OkHttpClient().newBuilder().build();
    okhttp3.MediaType mediaTypeGe = okhttp3.MediaType.parse("application/json");
    okhttp3.RequestBody bodyGe = okhttp3.RequestBody.create(mediaTypeGe, "{\n    \"orders\":[\n        \"" + idEtiqueta + "\"\n    ]\n}");
    okhttp3.Request requestGe = new okhttp3.Request.Builder()
        .url(ApiTokenIntegracao.URL_MELHOR_ENVIO_SAND_BOX + "api/v2/me/shipment/generate")
        .method("POST", bodyGe)
        .addHeader("Accept", "application/json")
        .addHeader("Content-Type", "application/json")
        .addHeader("Authorization", "Bearer " + ApiTokenIntegracao.TOKEN_MELHOR_ENVIO_SAND_BOX)
        .addHeader("User-Agent", "suporte@jdevtreinamento.com.br")
        .build();

    okhttp3.Response responseGe = clientGe.newCall(requestGe).execute();

    if (!responseGe.isSuccessful()) {
      throw new ExceptionEcommerce("Não foi possível gerar a etiqueta");
    }


    /*Faz impresão das etiquetas*/

    OkHttpClient clientIm = new OkHttpClient().newBuilder().build();
    okhttp3.MediaType mediaTypeIm = MediaType.parse("application/json");
    okhttp3.RequestBody bodyIm = okhttp3.RequestBody.create(mediaTypeIm, "{\n    \"mode\": \"private\",\n    \"orders\": [\n        \"" + idEtiqueta + "\"\n    ]\n}");
    okhttp3.Request requestIm = new Request.Builder()
        .url(ApiTokenIntegracao.URL_MELHOR_ENVIO_SAND_BOX + "api/v2/me/shipment/print")
        .method("POST", bodyIm)
        .addHeader("Accept", "application/json")
        .addHeader("Content-Type", "application/json")
        .addHeader("Authorization", "Bearer " + ApiTokenIntegracao.TOKEN_MELHOR_ENVIO_SAND_BOX)
        .addHeader("User-Agent", "suporte@jdevtreinamento.com.br")
        .build();

    okhttp3.Response responseIm = clientIm.newCall(requestIm).execute();


    if (!responseIm.isSuccessful()) {
      throw new ExceptionEcommerce("Não foi possível imprimir a etiqueta");
    }

    String urlEtiqueta = "";

    try {
      ObjectMapper objectMapper = new ObjectMapper();
      JsonNode jsonNodeIm = objectMapper.readTree(responseIm.body().string());
      urlEtiqueta = jsonNodeIm.get("url").asText();
    } catch (Exception e) {
      e.printStackTrace();
    }


    vendaCompraLojaVirtualRepository.updateURLEtiqueta(urlEtiqueta, compraLojaVirtual.getId());


    return ResponseEntity.ok("Etiqueta gerada com sucesso");

  }

  @GetMapping("/cancelarEtiqueta/{idEtiqueta}/{descricao}")
  public ResponseEntity<String> cancelarEtiqueta(@PathVariable("idEtiqueta") String idEtiqueta,
                                                 @PathVariable("descricao") String descricao) throws ExceptionEcommerce, IOException {

    CancelarEtiquetaDTO cancelarEtiquetaDTO = new CancelarEtiquetaDTO();

    cancelarEtiquetaDTO.getOrder().setId(idEtiqueta);
    cancelarEtiquetaDTO.getOrder().setReason_id("2");
    cancelarEtiquetaDTO.getOrder().setDescription(descricao);

    String jsonEnvio = new ObjectMapper().writeValueAsString(cancelarEtiquetaDTO);

    OkHttpClient clientCE = new OkHttpClient().newBuilder().build();
    okhttp3.MediaType mediaTypeCE = okhttp3.MediaType.parse("application/json");
    okhttp3.RequestBody bodyCE = okhttp3.RequestBody.create(mediaTypeCE, jsonEnvio);
    okhttp3.Request requestCE = new okhttp3.Request.Builder()
        .url(ApiTokenIntegracao.URL_MELHOR_ENVIO_SAND_BOX + "api/v2/me/shipment/cancel")
        .method("POST", bodyCE)
        .addHeader("Accept", "application/json")
        .addHeader("Content-Type", "application/json")
        .addHeader("Authorization", "Bearer " + ApiTokenIntegracao.TOKEN_MELHOR_ENVIO_SAND_BOX)
        .addHeader("User-Agent", "raphael@mocad.dev")
        .build();


    okhttp3.Response responseCE = clientCE.newCall(requestCE).execute();

    String responseBody = responseCE.body().string();
    ObjectMapper objectMapper = new ObjectMapper();
    JsonNode jsonNode = objectMapper.readTree(responseBody);

    String etiquetaId = jsonNode.fieldNames().next();
    JsonNode etiquetaNode = jsonNode.get(etiquetaId);

    boolean isEtiquetaCancelada = etiquetaNode.get("canceled").asBoolean();

    if (!isEtiquetaCancelada) {
      throw new ExceptionEcommerce("Não foi possível cancelar a etiqueta");
    }


    return ResponseEntity.ok("Etiqueta cancelada com sucesso");

  }
}

