package com.estudantes.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record FrequenciaDTO(
    Long id,
    @NotNull(message = "ID do aluno é obrigatório")
    Long alunoId,
    @NotNull(message = "Data é obrigatória")
    LocalDate data,
    @NotNull(message = "Presença é obrigatória")
    Boolean presente,
    @Size(max = 500, message = "Observação deve ter no máximo 500 caracteres")
    String observacao
) {} 