package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

// Isso aqui dá superpoderes pro Java: Salvar, Deletar, Buscar tudo pronto!
public interface VagaRepository extends JpaRepository<Vaga, Long> {

    // Método mágico: Só de escrever isso, o Spring já sabe como buscar por categoria
    List<Vaga> findByCategoria(String categoria);
}