package com.estudantes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.estudantes.entity.Escola;

@Repository
public interface EscolaRepository extends JpaRepository<Escola, Long> {
    @Query("SELECT e FROM Escola e WHERE (:nome IS NULL OR LOWER(e.nome) LIKE LOWER(CONCAT('%', CAST(:nome AS string), '%')))")
    List<Escola> findByNomeContainingIgnoreCase(@Param("nome") String nome);
} 