package com.estudantes.dto;

import java.time.LocalDate;

import com.estudantes.entity.Aluno.TipoBeneficio;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

public record AlunoDTO(
    Long id,
    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
    String nome,
    @NotBlank(message = "CPF é obrigatório")
    @Size(min = 11, max = 11, message = "CPF deve ter 11 dígitos")
    String cpf,
    @NotNull(message = "Data de nascimento é obrigatória")
    @Past(message = "Data de nascimento deve ser no passado")
    LocalDate dataNascimento,
    @NotBlank(message = "Endereço é obrigatório")
    @Size(min = 5, max = 200, message = "Endereço deve ter entre 5 e 200 caracteres")
    String endereco,
    @NotBlank(message = "Telefone é obrigatório")
    @Size(min = 10, max = 20, message = "Telefone deve ter entre 10 e 20 caracteres")
    String telefone,
    @NotNull(message = "Tipo de benefício é obrigatório")
    TipoBeneficio tipoBeneficio,
    @NotNull(message = "ID da escola é obrigatório")
    Long escolaId
) {} 