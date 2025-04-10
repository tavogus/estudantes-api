package com.estudantes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.estudantes.entity.Escola;

@Repository
public interface EscolaRepository extends JpaRepository<Escola, Long> {
} 