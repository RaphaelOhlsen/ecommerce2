package com.mocad.ecommerce.controller;

import com.mocad.ecommerce.ExceptionEcommerce;
import com.mocad.ecommerce.model.Endereco;
import com.mocad.ecommerce.model.PessoaFisica;
import com.mocad.ecommerce.model.PessoaJuridica;
import com.mocad.ecommerce.model.dto.CepDTO;
import com.mocad.ecommerce.repository.EnderecoRepository;
import com.mocad.ecommerce.repository.PessoaFisicaRepository;
import com.mocad.ecommerce.repository.PessoaRepository;
import com.mocad.ecommerce.service.PessoaUserService;
import com.mocad.ecommerce.utils.ValidaCNPJ;
import com.mocad.ecommerce.utils.ValidaCPF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class PessoaController {
	@Autowired
	private PessoaRepository pessoaRepository;

	@Autowired
	private PessoaUserService pessoaUserService;

	@Autowired
	private EnderecoRepository enderecoRepository;

	@Autowired
	private PessoaFisicaRepository pessoaFisicaRepository;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@ResponseBody
	@GetMapping(value = "**/consultaPfNome/{nome}")
	public ResponseEntity<List<PessoaFisica>> consultaPfNome(@PathVariable("nome") String nome) {

		List<PessoaFisica> fisicas = pessoaFisicaRepository.pesquisaPorNomePF(nome.trim().toUpperCase());

//		jdbcTemplate.execute("begin; update tabela_acesso_end_potin set qtd_acesso_end_point = qtd_acesso_end_point + 1 where nome_end_point = 'END-POINT-NOME-PESSOA-FISICA'; commit;");

		return ResponseEntity.ok(fisicas);
	}


	@ResponseBody
	@GetMapping(value = "**/consultaCpf/{cpf}")
	public ResponseEntity<List<PessoaFisica>> consultaCpf(@PathVariable("cpf") String cpf) {

		List<PessoaFisica> fisicas = pessoaFisicaRepository.pesquisaPorCpfPF(cpf);

		return ResponseEntity.ok(fisicas);
	}


	@ResponseBody
	@GetMapping(value = "**/consultaPjNome/{nome}")
	public ResponseEntity<List<PessoaJuridica>> consultaPjNome(@PathVariable("nome") String nome) {

		List<PessoaJuridica> juridicas = pessoaRepository.pesquisaPorNomePJ(nome.trim().toUpperCase());

		return new ResponseEntity(juridicas, HttpStatus.OK);
	}

	@ResponseBody
	@GetMapping(value = "**/consultaCnpj/{cnpj}")
	public ResponseEntity<List<PessoaJuridica>> consultaCnpj(@PathVariable("cnpj") String cnpj) {

		List<PessoaJuridica> juridicas = pessoaRepository.existeCnpjCadastradoList(cnpj);

		return ResponseEntity.ok(juridicas);
	}

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

		if (pessoaJuridica.getId() == null || pessoaJuridica.getId() <= 0) {

			for (int p = 0; p < pessoaJuridica.getEnderecos().size(); p++) {

				CepDTO cepDTO = pessoaUserService.consultaCep(pessoaJuridica.getEnderecos().get(p).getCep());

				pessoaJuridica.getEnderecos().get(p).setBairro(cepDTO.getBairro());
				pessoaJuridica.getEnderecos().get(p).setCidade(cepDTO.getLocalidade());
				pessoaJuridica.getEnderecos().get(p).setComplemento(cepDTO.getComplemento());
				pessoaJuridica.getEnderecos().get(p).setRuaLogra(cepDTO.getLogradouro());
				pessoaJuridica.getEnderecos().get(p).setUf(cepDTO.getUf());

			}
		} else {

			for (int p = 0; p < pessoaJuridica.getEnderecos().size(); p++) {

				Endereco enderecoTemp =  enderecoRepository.findById(pessoaJuridica.getEnderecos().get(p).getId()).get();

				if (!enderecoTemp.getCep().equals(pessoaJuridica.getEnderecos().get(p).getCep())) {

					CepDTO cepDTO = pessoaUserService.consultaCep(pessoaJuridica.getEnderecos().get(p).getCep());

					pessoaJuridica.getEnderecos().get(p).setBairro(cepDTO.getBairro());
					pessoaJuridica.getEnderecos().get(p).setCidade(cepDTO.getLocalidade());
					pessoaJuridica.getEnderecos().get(p).setComplemento(cepDTO.getComplemento());
					pessoaJuridica.getEnderecos().get(p).setRuaLogra(cepDTO.getLogradouro());
					pessoaJuridica.getEnderecos().get(p).setUf(cepDTO.getUf());
				}
			}
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
