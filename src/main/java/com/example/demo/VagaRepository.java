package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

// Repositorio de Vagas
public interface VagaRepository extends JpaRepository<Vaga, Long> {

    List<Vaga> findByCategoria(String categoria);

    @Query("""
            select v from Vaga v
            order by
              case when v.validade is null or v.validade = '' then 1 else 0 end,
              v.validade asc
            """)
    List<Vaga> findAllOrderByValidadeAsc();

    @Query("""
            select v from Vaga v
            where upper(v.categoria) = upper(:categoria)
            order by
              case when v.validade is null or v.validade = '' then 1 else 0 end,
              v.validade asc
            """)
    List<Vaga> findByCategoriaOrderByValidadeAsc(@Param("categoria") String categoria);
}
