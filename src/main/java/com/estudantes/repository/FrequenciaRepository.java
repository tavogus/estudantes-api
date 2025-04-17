package com.estudantes.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.estudantes.entity.Frequencia;

@Repository
public interface FrequenciaRepository extends JpaRepository<Frequencia, Long> {
    List<Frequencia> findByAlunoId(Long alunoId);

    @Query("SELECT f FROM Frequencia f WHERE " +
           "(:alunoId IS NULL OR f.aluno.id = :alunoId) " +
           "AND f.data BETWEEN :dataInicio AND :dataFim")
    List<Frequencia> findByAlunoIdAndDataBetween(
            @Param("alunoId") Long alunoId,
            @Param("dataInicio") LocalDate dataInicio,
            @Param("dataFim") LocalDate dataFim);
} 