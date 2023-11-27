package com.mocad.ecommerce.controller;

import com.mocad.ecommerce.model.NotaFiscalCompra;
import com.mocad.ecommerce.repository.NotaFiscalCompraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class NotaFiscalCompraController {

    @Autowired
    private NotaFiscalCompraRepository notaFiscalCompraRepository;

    @GetMapping("**/buscarNotaFiscalPorDesc")
    public ResponseEntity<List<NotaFiscalCompra>> buscarNotaFiscalPorDesc(@RequestParam("desc") String desc,
                                                                    @RequestParam("idEmpresa") Long idEmpresa) {

        List<NotaFiscalCompra> notas = notaFiscalCompraRepository.buscaNotaDesc(desc.toUpperCase().trim(),idEmpresa);

        return ResponseEntity.ok(notas);
    }

    @GetMapping("**/obterNotaFiscalCompra/{id}")
    public ResponseEntity<?> obterNotaFiscalCompra(@RequestParam("id") Long id) {
        NotaFiscalCompra notaFiscalCompra = notaFiscalCompraRepository.findById(id).orElse(null);

        if (notaFiscalCompra == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nota fiscal não encontrada");
        }

        return ResponseEntity.ok(notaFiscalCompra);
    }

    @DeleteMapping("**/deleteNotaFiscalCompraPorId/{id}")
    public ResponseEntity<?> deleteNotaFiscalCompraPorId(@PathVariable("id") Long id) {
        NotaFiscalCompra notaFiscalCompra = notaFiscalCompraRepository.findById(id).orElse(null);

        if (notaFiscalCompra == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nota fiscal não encontrada");
        }

        notaFiscalCompraRepository.deleteItemNotaFiscalCompra(id); // deleta os itens da nota fiscal
        notaFiscalCompraRepository.deleteById(id); // deleta a nota fiscal

        return ResponseEntity.ok("Nota fiscal deletada com sucesso");
    }

    @PostMapping("**/salvarNotaFiscalCompra")
    public ResponseEntity<?> salvarNotaFiscalCompra(@RequestBody @Valid NotaFiscalCompra notaFiscalCompra) {

        if (notaFiscalCompra.getId() == null) {

            if (notaFiscalCompra.getDescricaoObs() != null) {
                List<NotaFiscalCompra> notas = notaFiscalCompraRepository.
                        buscaNotaDesc(notaFiscalCompra.getDescricaoObs().toUpperCase().trim(),notaFiscalCompra.getEmpresa().getId());

                if (!notas.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                            body("Já existe uma nota fiscal de compra com essa descrição");
                }
            }

        }

        if (notaFiscalCompra.getPessoa() == null || notaFiscalCompra.getPessoa().getId() <= 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).
                    body("A pessoa Juridica que emitiu a nota fiscal deve ser informada");
        }

        if (notaFiscalCompra.getContaPagar() == null || notaFiscalCompra.getContaPagar().getId() <= 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).
                    body("A conta a pagar deve ser informada");
        }

        if (notaFiscalCompra.getEmpresa() == null || notaFiscalCompra.getEmpresa().getId() <= 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).
                    body("A empresa deve ser informada");
        }


        NotaFiscalCompra notaFiscalCompraSalva = notaFiscalCompraRepository.save(notaFiscalCompra);

        return ResponseEntity.ok(notaFiscalCompraSalva);

    }

}
