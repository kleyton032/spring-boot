package com.sistema.cobranca.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sistema.cobranca.model.Titulo;

public interface TitulosRepository extends JpaRepository<Titulo, Long> {

}
