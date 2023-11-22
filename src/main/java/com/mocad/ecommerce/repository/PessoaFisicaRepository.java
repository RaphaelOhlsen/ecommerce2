package com.mocad.ecommerce.repository;
import com.mocad.ecommerce.model.PessoaFisica;
import com.mocad.ecommerce.model.PessoaJuridica;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PessoaFisicaRepository extends CrudRepository<PessoaFisica, Long> {

}
