package com.estudantes.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.estudantes.entity.Frequencia;

@Repository
public interface FrequenciaRepository extends JpaRepository<Frequencia, Long> {
    List<Frequencia> findByAlunoId(Long alunoId);
    List<Frequencia> findByAlunoIdAndDataBetween(Long alunoId, LocalDate dataInicio, LocalDate dataFim);
} 