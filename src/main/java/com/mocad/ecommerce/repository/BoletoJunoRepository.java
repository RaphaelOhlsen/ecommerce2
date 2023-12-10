package com.mocad.ecommerce.repository;

import com.mocad.ecommerce.model.BoletoJuno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoletoJunoRepository extends JpaRepository<BoletoJuno, Long> {

}
