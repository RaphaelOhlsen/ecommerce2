package com.mocad.ecommerce.controller;

import com.mocad.ecommerce.model.ContaPagar;
import com.mocad.ecommerce.repository.ContaPagarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class ContaPagarController {

    @Autowired
    private ContaPagarRepository contaPagarRepository;

    @GetMapping("**/buscarPorContaDesc")
    public ResponseEntity<List<ContaPagar>> buscarPorContaDesc(@RequestParam("desc") String desc,
                                                                 @RequestParam("idEmpresa") Long idEmpresa) {

        List<ContaPagar> contas = contaPagarRepository.buscaContaDesc(desc.toUpperCase().trim(),idEmpresa);
        return ResponseEntity.ok(contas);
    }

    @GetMapping("**/oterContaPagar/{id}")
    public ResponseEntity<?> oterContaPagar(@PathVariable("id") Long id) {
        ContaPagar contaPagar = contaPagarRepository.findById(id).orElse(null);

        if (contaPagar == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Conta não encontrada");
        }

        return ResponseEntity.ok(contaPagar);
    }

    @DeleteMapping("**/deletarContaPagarPorId/{id}")
    public ResponseEntity<?> deletarContaPagarPorId(@PathVariable("id") Long id) {
        ContaPagar contaPagar = contaPagarRepository.findById(id).orElse(null);

        if (contaPagar == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Conta não encontrada");
        }

        contaPagarRepository.deleteById(id);

        return ResponseEntity.ok("Conta deletada com sucesso");
    }

    @PostMapping("**/salvarContaPagar")
    public ResponseEntity<?> salvarContaPagar(@RequestBody @Valid ContaPagar contaPagar) {

        if (contaPagar.getPessoaFisica() == null || contaPagar.getPessoaFisica().getId() <= 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).
                    body("A pessoa responsável pela conta deve ser informada");
        }

        if (contaPagar.getEmpresa() == null || contaPagar.getEmpresa().getId() <= 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).
                    body("A Empresa responsável pela conta deve ser informada");
        }

        if (contaPagar.getPessoaFornecedor() == null || contaPagar.getPessoaFornecedor().getId() <= 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).
                    body("A pessoa fornecedora da conta deve ser informada");
        }

        if (contaPagar.getId() == null) {
            List<ContaPagar> contaPagars = contaPagarRepository.buscaContaDesc(contaPagar.getDescricao().toUpperCase().trim());

            if (!contaPagars.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).
                        body("Não pode cadastrar duas contas com a mesma descrição");
            }
        }

        ContaPagar contaPagarSalva = contaPagarRepository.save(contaPagar);

        return ResponseEntity.ok(contaPagarSalva);
    }

    @GetMapping("**/buscarTodasContasPagar/{id}")
    public ResponseEntity<List<ContaPagar>> buscarTodasContasPagar(@PathVariable("id") Long id) {
        List<ContaPagar> contas = contaPagarRepository.buscaContaPorEmpresaId(id);
        return ResponseEntity.ok(contas);
    }

    @GetMapping("**/buscarContasPorPessoa/{id}")
    public ResponseEntity<List<ContaPagar>> buscarContasPorPessoa(@PathVariable("id") Long id) {
        List<ContaPagar> contas = contaPagarRepository.buscaContasPorPessoa(id);
        return ResponseEntity.ok(contas);
    }

    @GetMapping("**/buscarContasPorFornecedor/")
    public ResponseEntity<List<ContaPagar>> buscarContasPorFornecedor(@RequestParam("idFornecedor") Long idForncedor,
                                                                       @RequestParam("idEmpresa") Long idEmpresa) {
        List<ContaPagar> contas = contaPagarRepository.buscaContasPorFornecedor(idForncedor, idEmpresa);
        return ResponseEntity.ok(contas);
    }
}
