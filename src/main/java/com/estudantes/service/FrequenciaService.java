package com.estudantes.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.estudantes.dto.FrequenciaDTO;
import com.estudantes.entity.Aluno;
import com.estudantes.entity.Frequencia;
import com.estudantes.repository.AlunoRepository;
import com.estudantes.repository.FrequenciaRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
@Transactional
public class FrequenciaService {
    private final FrequenciaRepository frequenciaRepository;
    private final AlunoRepository alunoRepository;

    public FrequenciaService(FrequenciaRepository frequenciaRepository, AlunoRepository alunoRepository) {
        this.frequenciaRepository = frequenciaRepository;
        this.alunoRepository = alunoRepository;
    }

    public FrequenciaDTO registrarFrequencia(FrequenciaDTO frequenciaDTO) {
        Aluno aluno = alunoRepository.findById(frequenciaDTO.alunoId())
                .orElseThrow(() -> new EntityNotFoundException("Aluno não encontrado com ID: " + frequenciaDTO.alunoId()));

        Frequencia frequencia = new Frequencia();
        frequencia.setAluno(aluno);
        frequencia.setData(frequenciaDTO.data());
        frequencia.setPresente(frequenciaDTO.presente());
        frequencia.setObservacao(frequenciaDTO.observacao());

        frequencia = frequenciaRepository.save(frequencia);
        return new FrequenciaDTO(
            frequencia.getId(),
            frequencia.getAluno().getId(),
            frequencia.getData(),
            frequencia.getPresente(),
            frequencia.getObservacao()
        );
    }

    public List<FrequenciaDTO> buscarFrequenciasPorAluno(Long alunoId) {
        return frequenciaRepository.findByAlunoId(alunoId).stream()
                .map(frequencia -> new FrequenciaDTO(
                    frequencia.getId(),
                    frequencia.getAluno().getId(),
                    frequencia.getData(),
                    frequencia.getPresente(),
                    frequencia.getObservacao()
                ))
                .toList();
    }

    public List<FrequenciaDTO> buscarFrequenciasPorPeriodo(Long alunoId, LocalDate dataInicio, LocalDate dataFim) {
        return frequenciaRepository.findByAlunoIdAndDataBetween(alunoId, dataInicio, dataFim).stream()
                .map(frequencia -> new FrequenciaDTO(
                    frequencia.getId(),
                    frequencia.getAluno().getId(),
                    frequencia.getData(),
                    frequencia.getPresente(),
                    frequencia.getObservacao()
                ))
                .toList();
    }

    public FrequenciaDTO atualizarFrequencia(Long id, FrequenciaDTO frequenciaDTO) {
        Frequencia frequencia = frequenciaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Frequência não encontrada com ID: " + id));

        frequencia.setPresente(frequenciaDTO.presente());
        frequencia.setObservacao(frequenciaDTO.observacao());

        frequencia = frequenciaRepository.save(frequencia);
        return new FrequenciaDTO(
            frequencia.getId(),
            frequencia.getAluno().getId(),
            frequencia.getData(),
            frequencia.getPresente(),
            frequencia.getObservacao()
        );
    }

    public void deletarFrequencia(Long id) {
        if (!frequenciaRepository.existsById(id)) {
            throw new EntityNotFoundException("Frequência não encontrada com ID: " + id);
        }
        frequenciaRepository.deleteById(id);
    }
} 