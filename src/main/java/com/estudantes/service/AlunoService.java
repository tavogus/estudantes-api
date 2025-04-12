package com.estudantes.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.estudantes.dto.AlunoDTO;
import com.estudantes.entity.Aluno;
import com.estudantes.entity.Escola;
import com.estudantes.repository.AlunoRepository;
import com.estudantes.repository.EscolaRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
@Transactional
public class AlunoService {
    private final AlunoRepository alunoRepository;
    private final EscolaRepository escolaRepository;

    public AlunoService(AlunoRepository alunoRepository, EscolaRepository escolaRepository) {
        this.alunoRepository = alunoRepository;
        this.escolaRepository = escolaRepository;
    }

    public AlunoDTO criarAluno(AlunoDTO alunoDTO) {
        if (alunoRepository.existsByCpf(alunoDTO.cpf())) {
            throw new IllegalArgumentException("CPF já cadastrado");
        }

        Escola escola = escolaRepository.findById(alunoDTO.escolaId())
                .orElseThrow(() -> new EntityNotFoundException("Escola não encontrada com ID: " + alunoDTO.escolaId()));

        Aluno aluno = new Aluno();
        aluno.setNome(alunoDTO.nome());
        aluno.setCpf(alunoDTO.cpf());
        aluno.setDataNascimento(alunoDTO.dataNascimento());
        aluno.setEndereco(alunoDTO.endereco());
        aluno.setTelefone(alunoDTO.telefone());
        aluno.setTipoBeneficio(alunoDTO.tipoBeneficio());
        aluno.setEscola(escola);
        aluno.setAlerta(false);

        aluno = alunoRepository.save(aluno);
        return toDTO(aluno);
    }

    public AlunoDTO buscarAlunoPorId(Long id) {
        Aluno aluno = alunoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Aluno não encontrado com ID: " + id));
        return toDTO(aluno);
    }

    public List<AlunoDTO> listarAlunosPorEscola(Long escolaId) {
        return alunoRepository.findByEscolaId(escolaId).stream()
                .map(this::toDTO)
                .toList();
    }

    public AlunoDTO atualizarAluno(Long id, AlunoDTO alunoDTO) {
        Aluno aluno = alunoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Aluno não encontrado com ID: " + id));

        Escola escola = escolaRepository.findById(alunoDTO.escolaId())
                .orElseThrow(() -> new EntityNotFoundException("Escola não encontrada com ID: " + alunoDTO.escolaId()));

        aluno.setNome(alunoDTO.nome());
        aluno.setCpf(alunoDTO.cpf());
        aluno.setDataNascimento(alunoDTO.dataNascimento());
        aluno.setEndereco(alunoDTO.endereco());
        aluno.setTelefone(alunoDTO.telefone());
        aluno.setTipoBeneficio(alunoDTO.tipoBeneficio());
        aluno.setEscola(escola);

        aluno = alunoRepository.save(aluno);
        return toDTO(aluno);
    }

    public void deletarAluno(Long id) {
        if (!alunoRepository.existsById(id)) {
            throw new EntityNotFoundException("Aluno não encontrado com ID: " + id);
        }
        alunoRepository.deleteById(id);
    }

    public void atualizarAlertaAluno(Long alunoId, boolean alerta) {
        Aluno aluno = alunoRepository.findById(alunoId)
                .orElseThrow(() -> new EntityNotFoundException("Aluno não encontrado com ID: " + alunoId));
        
        aluno.setAlerta(alerta);
        alunoRepository.save(aluno);
    }

    private AlunoDTO toDTO(Aluno aluno) {
        return new AlunoDTO(
            aluno.getId(),
            aluno.getNome(),
            aluno.getCpf(),
            aluno.getDataNascimento(),
            aluno.getEndereco(),
            aluno.getTelefone(),
            aluno.getTipoBeneficio(),
            aluno.getEscola().getId(),
            aluno.isAlerta()
        );
    }
} 