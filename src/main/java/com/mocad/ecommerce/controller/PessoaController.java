package com.mocad.ecommerce.controller;

import com.mocad.ecommerce.ExceptionEcommerce;
import com.mocad.ecommerce.model.PessoaJuridica;
import com.mocad.ecommerce.repository.PessoaRepository;
import com.mocad.ecommerce.service.PessoaUserService;
import com.mocad.ecommerce.utils.ValidaCNPJ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PessoaController {
	@Autowired
	private PessoaRepository pessoaRepository;

	@Autowired
	private PessoaUserService pessoaUserService;

	@ResponseBody
	@PostMapping(value = "**/salvarPJ")
	public ResponseEntity<PessoaJuridica> salvarPJ(@RequestBody PessoaJuridica pessoaJuridica) throws ExceptionEcommerce {
		if (pessoaJuridica == null) {
			throw new ExceptionEcommerce("Pessoa Juridica não pode ser nula");
		}
		if (pessoaJuridica.getId() == null && pessoaRepository.existeCnpjCadastrado(pessoaJuridica.getCnpj()) != null) {
			throw new ExceptionEcommerce("CNPJ já cadastrado");
		}

		if (pessoaJuridica.getId() == null && pessoaRepository.existeInscEstadualCadastrado(pessoaJuridica.getInscEstadual()) != null) {
			throw new ExceptionEcommerce("Inscrição Estadual já cadastrada");
		}

		if (!ValidaCNPJ.isCNPJ(pessoaJuridica.getCnpj())) {
			throw new ExceptionEcommerce("CNPJ" + pessoaJuridica.getCnpj() + " inválido.");
		}

		pessoaJuridica = pessoaUserService.salvarPessoaJuridica(pessoaJuridica);

		return ResponseEntity.ok(pessoaJuridica);
	}
    
}
