package com.mocad.ecommerce.repository;

import com.mocad.ecommerce.model.CupDesc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CupDescRepository extends JpaRepository<CupDesc, Long> {


    @Query("select c from CupDesc c where c.empresa.id = ?1")
    List<CupDesc> buscarCupDescPorEmpresa(Long idEmpresa);
}
