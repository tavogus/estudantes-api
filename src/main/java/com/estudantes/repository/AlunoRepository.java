package com.estudantes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.estudantes.entity.Aluno;
import com.estudantes.entity.Aluno.TipoBeneficio;

@Repository
public interface AlunoRepository extends JpaRepository<Aluno, Long> {
    List<Aluno> findByEscolaId(Long escolaId);
    boolean existsByCpf(String cpf);

    @Query("SELECT a FROM Aluno a WHERE a.escola.id = :escolaId " +
           "AND (:nome IS NULL OR LOWER(a.nome) LIKE LOWER(CONCAT('%', CAST(:nome AS string), '%'))) " +
           "AND (:cpf IS NULL OR a.cpf LIKE CONCAT('%', CAST(:cpf AS string), '%')) " +
           "AND (:tipoBeneficio IS NULL OR a.tipoBeneficio = :tipoBeneficio)")
    List<Aluno> findByEscolaIdAndFilters(
            @Param("escolaId") Long escolaId,
            @Param("nome") String nome,
            @Param("cpf") String cpf,
            @Param("tipoBeneficio") TipoBeneficio tipoBeneficio);
} 