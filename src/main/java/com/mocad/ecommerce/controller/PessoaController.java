package com.mocad.ecommerce.controller;

import com.mocad.ecommerce.ExceptionEcommerce;
import com.mocad.ecommerce.model.PessoaFisica;
import com.mocad.ecommerce.model.PessoaJuridica;
import com.mocad.ecommerce.model.dto.CepDTO;
import com.mocad.ecommerce.repository.PessoaRepository;
import com.mocad.ecommerce.service.PessoaUserService;
import com.mocad.ecommerce.utils.ValidaCNPJ;
import com.mocad.ecommerce.utils.ValidaCPF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class PessoaController {
	@Autowired
	private PessoaRepository pessoaRepository;

	@Autowired
	private PessoaUserService pessoaUserService;

	@ResponseBody
	@GetMapping(value = "**/consultaCep/{cep}")
	public ResponseEntity<CepDTO> consultaCep(@PathVariable("cep") String cep){
		return ResponseEntity.ok(pessoaUserService.consultaCep(cep));
	}

	@ResponseBody
	@PostMapping(value = "**/salvarPJ")
	public ResponseEntity<PessoaJuridica> salvarPJ(@RequestBody @Valid PessoaJuridica pessoaJuridica) throws ExceptionEcommerce {
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

	@ResponseBody
	@PostMapping(value = "**/salvarPF")
	public ResponseEntity<PessoaFisica> salvarPF(@RequestBody PessoaFisica pessoaFisica) throws ExceptionEcommerce {
		if (pessoaFisica == null) {
			throw new ExceptionEcommerce("Pessoa Física não pode ser nula");
		}

		if (!ValidaCPF.isCPF(pessoaFisica.getCpf())) {
			throw new ExceptionEcommerce("CPF" + pessoaFisica.getCpf() + " inválido.");
		}

		if (pessoaFisica.getId() == null && pessoaRepository.existeCpfCadastrado(pessoaFisica.getCpf()) != null) {
			throw new ExceptionEcommerce("CPF " + pessoaFisica.getCpf() + "já cadastrado");
		}



		pessoaFisica = pessoaUserService.salvarPessoaFisica(pessoaFisica);

		return ResponseEntity.ok(pessoaFisica);
	}
    
}
