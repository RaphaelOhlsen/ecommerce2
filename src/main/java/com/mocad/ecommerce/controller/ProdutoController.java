package com.mocad.ecommerce.controller;

import com.mocad.ecommerce.ExceptionEcommerce;
import com.mocad.ecommerce.model.Produto;
import com.mocad.ecommerce.model.dto.ProdutoDTO;
import com.mocad.ecommerce.repository.ProdutoRepository;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import com.mocad.ecommerce.service.ImagemProdutoService;
import com.mocad.ecommerce.service.ServiceSendEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.mail.MessagingException;
import javax.validation.Valid;
import javax.xml.bind.DatatypeConverter;

@Controller
@RestController
public class ProdutoController {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private ImagemProdutoService ImagemProdutoService;

    @Autowired
    private ServiceSendEmail serviceSendEmail;

    @ResponseBody /*Poder dar um retorno da API*/
    @PostMapping(value = "**/salvarProduto") /*Mapeando a url para receber JSON*/
    public ResponseEntity<ProdutoDTO> salvarProduto(@RequestBody @Valid Produto produto) throws ExceptionEcommerce, MessagingException, IOException { /*Recebe o JSON e converte pra Objeto*/

        if (produto.getTipoUnidade() == null || produto.getTipoUnidade().trim().isEmpty()) {
            throw new ExceptionEcommerce("Tipo de Unidade não informada");
        }

        if (produto.getEmpresa() == null || produto.getEmpresa().getId() <= 0) {
            throw new ExceptionEcommerce("Empresa não informada");
        }

        if (produto.getCategoriaProduto()  == null || produto.getCategoriaProduto().getId() <= 0) {
            throw new ExceptionEcommerce("Categoria não informada");
        }

        if (produto.getMarcaProduto() == null || produto.getMarcaProduto().getId() <= 0) {
            throw new ExceptionEcommerce("Marca não informada");
        }

        if (produto.getImagens() == null || produto.getImagens().isEmpty() || produto.getImagens().size() == 0) {
            throw new ExceptionEcommerce("Imagem não informada");
        }

        if (produto.getImagens().size() < 3 || produto.getImagens().size() > 6) {
            throw new ExceptionEcommerce("Quantidade de imagens deve ser entre 3 e 6");
        }

        if (produto.getId() == null) {

            List<Produto> produtos = produtoRepository.buscarProdutoNome(produto.getNome().toUpperCase(), produto.getEmpresa().getId());

            if (!produtos.isEmpty()) {
                throw new ExceptionEcommerce("Já existe Produto com este nome: " + produto.getNome());
            }

            for (int x = 0; x < produto.getImagens().size(); x++) {

                produto.getImagens().get(x).setProduto(produto);
                produto.getImagens().get(x).setEmpresa(produto.getEmpresa());

                String ImagemMiniatura = ImagemProdutoService.miniaturaImagem(produto.getImagens().get(x).getImagemOriginal());
                produto.getImagens().get(x).setImagemMiniatura(ImagemMiniatura);

//
//                String base64Image = "";
//
//                if (produto.getImagens().get(x).getImagemOriginal().contains("data:image")) {
//                    base64Image = produto.getImagens().get(x).getImagemOriginal().split(",")[1];
//                }else {
//                    base64Image = produto.getImagens().get(x).getImagemOriginal();
//                }
//
//                byte[] imageBytes =  DatatypeConverter.parseBase64Binary(base64Image);
//
//                BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(imageBytes));
//
//                if (bufferedImage != null) {
//
//                    int type = bufferedImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : bufferedImage.getType();
//                    int largura = Integer.parseInt("800");
//                    int altura = Integer.parseInt("600");
//
//                    BufferedImage resizedImage = new BufferedImage(largura, altura, type);
//                    Graphics2D g = resizedImage.createGraphics();
//                    g.drawImage(bufferedImage, 0, 0, largura, altura, null);
//                    g.dispose();
//
//                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                    ImageIO.write(resizedImage, "png", baos);
//
//                    String miniImgBase64 = "data:image/png;base64," + DatatypeConverter.printBase64Binary(baos.toByteArray());
//
//                    produto.getImagens().get(x).setImagemMiniatura(miniImgBase64);
//
//                    bufferedImage.flush();
//                    resizedImage.flush();
//                    baos.flush();
//                    baos.close();

//                }
            }
        }

        Produto produtoSalvo = produtoRepository.saveAndFlush(produto);

        ProdutoDTO produtoDTO = new ProdutoDTO();

        produtoDTO.setId(produtoSalvo.getId());
        produtoDTO.setTipoUnidade(produtoSalvo.getTipoUnidade());
        produtoDTO.setNome(produtoSalvo.getNome());
        produtoDTO.setAtivo(produtoSalvo.getAtivo());
        produtoDTO.setDescricao(produtoSalvo.getDescricao());
        produtoDTO.setAltura(produtoSalvo.getAltura());
        produtoDTO.setLargura(produtoSalvo.getLargura());
        produtoDTO.setPeso(produtoSalvo.getPeso());
        produtoDTO.setProfundidade(produtoSalvo.getProfundidade());
        produtoDTO.setValorVenda(produtoSalvo.getValorVenda());
        produtoDTO.setQtdEstoque(produtoSalvo.getQtdEstoque());
        produtoDTO.setQtdAlertaEstoque(produtoSalvo.getQtdeAlertaEstoque());
        produtoDTO.setAlertaQtdEstoque(produtoSalvo.getAlertaQtdeEstoque());
        produtoDTO.setQtdClique(produtoSalvo.getQtdClique());
        produtoDTO.setEmpresa(produtoSalvo.getEmpresa().getId());
        produtoDTO.setCategoriaProduto(produtoSalvo.getCategoriaProduto().getId());
        produtoDTO.setMarcaProduto(produtoSalvo.getMarcaProduto().getId());



        if (produto.getAlertaQtdeEstoque() && produto.getQtdEstoque() <= 1) {
            StringBuilder html = new StringBuilder();
            html.append("<h2>").append("Produto: ").append(produto.getNome())
                    .append(" com estoque baixo: " + produto.getQtdEstoque())
                    .append("</h2><br>");
            html.append("<p> Id Prod.:").append(produto.getId()).append("</p>");

            if (produto.getEmpresa().getEmail() != null && !produto.getEmpresa().getEmail().isEmpty()) {
                serviceSendEmail.enviarEmailHtml(
                        "Produto com Estoque Baixo",
                        html.toString(),
                        produto.getEmpresa().getEmail().toString()
                );
            }
        }

        return ResponseEntity.ok(produtoDTO);
    }

    @ResponseBody /*Poder dar um retorno da API*/
    @PostMapping(value = "**/deleteProduto") /*Mapeando a url para receber JSON*/
    public ResponseEntity<String> deleteAcesso(@RequestBody Produto produto) { /*Recebe o JSON e converte pra Objeto*/

        produtoRepository.deleteById(produto.getId());

        return new ResponseEntity("Produto Removido",HttpStatus.OK);
    }

    @ResponseBody
    @DeleteMapping(value = "**/deleteProdutoPorId/{id}") /*Mapeando a url para receber JSON*/
    public ResponseEntity<String> deleteAcessoPorId(@PathVariable Long id) {

        produtoRepository.deleteById(id);

        return new ResponseEntity("Acesso Removido",HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping(value = "**/obeterProduto/{id}") /*Mapeando a url para receber JSON*/
    public ResponseEntity<Produto> obeterProduto(@PathVariable Long id) throws ExceptionEcommerce {

        Produto produto = produtoRepository.findById(id).orElse(null);

        if (produto == null) {
            throw new ExceptionEcommerce("Produto "+ id + " não encontrado");
        }

        return new ResponseEntity<Produto>(produto, HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping(value = "**/buscarPorNomeProduto") /*Mapeando a url para receber JSON*/
    public ResponseEntity<List<Produto>> buscarPorNome(@RequestParam("nome") String nome,
                                                       @RequestParam("idEmpresa") Long idEmpresa) {

        List<Produto> produtos = produtoRepository.buscarProdutoNome(nome.toUpperCase().trim(), idEmpresa);

        return new ResponseEntity<>(produtos, HttpStatus.OK);
    }
}
