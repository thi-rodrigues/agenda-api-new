package com.github.thirodrigues.agendaapi.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.github.thirodrigues.agendaapi.model.entity.Contato;

@Repository
public interface ContatoRepository extends JpaRepository<Contato, Integer> {

}
