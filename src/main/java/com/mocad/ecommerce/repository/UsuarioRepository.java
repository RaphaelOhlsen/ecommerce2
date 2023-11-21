package com.mocad.ecommerce.repository;


import com.mocad.ecommerce.model.Usuario;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;


@Repository
public interface UsuarioRepository extends CrudRepository<Usuario, Long> {
	@Query(value = "select u from Usuario u where u.login = ?1")
	Usuario findUserByLogin(String login);

	@Query(value = "select u from Usuario u where u.pessoa.id = ?1 or u.login = ?2")
	Usuario findUserByPessoa(Long id, String login);

	@Query(value = "select constraint_name from information_schema.constraint_column_usage\n" +
			" where table_name = 'usuarios_acesso' and column_name = 'acesso_id'\n" +
			"and constraint_name <> 'unique_acesso_user';", nativeQuery = true)
	String consultaContraintAcesso();

	@Transactional
	@Modifying
	@Query(
			value = "insert into usuarios_acesso (usuario_id, acesso_id) values (?1, (select id from acesso where descricao = 'ROLE_USER'))",
			nativeQuery = true
	)
	void inserirAcessoUsuarioPJ(Long id);
}