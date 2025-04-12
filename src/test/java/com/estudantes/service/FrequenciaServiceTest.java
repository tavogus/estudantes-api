package com.estudantes.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.estudantes.dto.FrequenciaDTO;
import com.estudantes.entity.Aluno;
import com.estudantes.entity.Frequencia;
import com.estudantes.repository.AlunoRepository;
import com.estudantes.repository.FrequenciaRepository;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class FrequenciaServiceTest {

    @Mock
    private FrequenciaRepository frequenciaRepository;

    @Mock
    private AlunoRepository alunoRepository;

    @Mock
    private AlunoService alunoService;

    @InjectMocks
    private FrequenciaService frequenciaService;

    private FrequenciaDTO frequenciaDTO;
    private Frequencia frequencia;
    private Aluno aluno;

    @BeforeEach
    void setUp() {
        aluno = new Aluno();
        aluno.setId(1L);
        aluno.setNome("João Silva");
        aluno.setAlerta(false);

        frequencia = new Frequencia();
        frequencia.setId(1L);
        frequencia.setAluno(aluno);
        frequencia.setData(LocalDate.of(2024, 3, 20));
        frequencia.setPresente(true);
        frequencia.setObservacao("Aluno presente");

        frequenciaDTO = new FrequenciaDTO(
            1L,
            1L,
            LocalDate.of(2024, 3, 20),
            true,
            "Aluno presente"
        );
    }

    @Test
    void registrarFrequencia_DeveRetornarFrequenciaDTO_QuandoDadosValidos() {
        when(alunoRepository.findById(1L)).thenReturn(Optional.of(aluno));
        when(frequenciaRepository.save(any(Frequencia.class))).thenReturn(frequencia);

        FrequenciaDTO result = frequenciaService.registrarFrequencia(frequenciaDTO);

        assertNotNull(result);
        assertEquals(frequenciaDTO.id(), result.id());
        assertEquals(frequenciaDTO.alunoId(), result.alunoId());
        assertEquals(frequenciaDTO.data(), result.data());
        assertEquals(frequenciaDTO.presente(), result.presente());
        assertEquals(frequenciaDTO.observacao(), result.observacao());
        verify(frequenciaRepository).save(any(Frequencia.class));
    }

    @Test
    void registrarFrequencia_DeveLancarExcecao_QuandoAlunoNaoExiste() {
        when(alunoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            frequenciaService.registrarFrequencia(frequenciaDTO);
        });
    }

    @Test
    void buscarFrequenciasPorAluno_DeveRetornarListaDeFrequencias() {
        when(frequenciaRepository.findByAlunoId(1L)).thenReturn(List.of(frequencia));

        List<FrequenciaDTO> result = frequenciaService.buscarFrequenciasPorAluno(1L);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(frequenciaDTO.id(), result.get(0).id());
        assertEquals(frequenciaDTO.alunoId(), result.get(0).alunoId());
        assertEquals(frequenciaDTO.data(), result.get(0).data());
    }

    @Test
    void buscarFrequenciasPorPeriodo_DeveRetornarListaDeFrequencias() {
        LocalDate dataInicio = LocalDate.of(2024, 3, 1);
        LocalDate dataFim = LocalDate.of(2024, 3, 31);
        
        when(frequenciaRepository.findByAlunoIdAndDataBetween(1L, dataInicio, dataFim))
            .thenReturn(List.of(frequencia));

        List<FrequenciaDTO> result = frequenciaService.buscarFrequenciasPorPeriodo(1L, dataInicio, dataFim);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(frequenciaDTO.id(), result.get(0).id());
        assertEquals(frequenciaDTO.alunoId(), result.get(0).alunoId());
        assertEquals(frequenciaDTO.data(), result.get(0).data());
    }

    @Test
    void atualizarFrequencia_DeveRetornarFrequenciaDTOAtualizado_QuandoDadosValidos() {
        when(frequenciaRepository.findById(1L)).thenReturn(Optional.of(frequencia));
        when(frequenciaRepository.save(any(Frequencia.class))).thenReturn(frequencia);

        FrequenciaDTO result = frequenciaService.atualizarFrequencia(1L, frequenciaDTO);

        assertNotNull(result);
        assertEquals(frequenciaDTO.id(), result.id());
        assertEquals(frequenciaDTO.alunoId(), result.alunoId());
        assertEquals(frequenciaDTO.data(), result.data());
        assertEquals(frequenciaDTO.presente(), result.presente());
        assertEquals(frequenciaDTO.observacao(), result.observacao());
        verify(frequenciaRepository).save(any(Frequencia.class));
    }

    @Test
    void atualizarFrequencia_DeveLancarExcecao_QuandoFrequenciaNaoExiste() {
        when(frequenciaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            frequenciaService.atualizarFrequencia(1L, frequenciaDTO);
        });
    }

    @Test
    void deletarFrequencia_DeveDeletarFrequencia_QuandoFrequenciaExiste() {
        when(frequenciaRepository.existsById(1L)).thenReturn(true);
        doNothing().when(frequenciaRepository).deleteById(1L);

        assertDoesNotThrow(() -> {
            frequenciaService.deletarFrequencia(1L);
        });

        verify(frequenciaRepository).deleteById(1L);
    }

    @Test
    void deletarFrequencia_DeveLancarExcecao_QuandoFrequenciaNaoExiste() {
        when(frequenciaRepository.existsById(1L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> {
            frequenciaService.deletarFrequencia(1L);
        });
    }

    @Test
    void verificarAlunoComMaisDe15FaltasNoMes_DeveRetornarTrueEAtualizarAlerta_QuandoTemMaisDe15Faltas() {
        LocalDate data = LocalDate.of(2024, 3, 20);
        LocalDate primeiroDiaDoMes = data.withDayOfMonth(1);
        LocalDate ultimoDiaDoMes = data.withDayOfMonth(data.lengthOfMonth());

        when(alunoRepository.existsById(1L)).thenReturn(true);
        
        // Criando 16 frequências com presente = false (faltas)
        List<Frequencia> frequencias = List.of(
            createFrequencia(false), createFrequencia(false), createFrequencia(false),
            createFrequencia(false), createFrequencia(false), createFrequencia(false),
            createFrequencia(false), createFrequencia(false), createFrequencia(false),
            createFrequencia(false), createFrequencia(false), createFrequencia(false),
            createFrequencia(false), createFrequencia(false), createFrequencia(false),
            createFrequencia(false)
        );
        
        when(frequenciaRepository.findByAlunoIdAndDataBetween(1L, primeiroDiaDoMes, ultimoDiaDoMes))
            .thenReturn(frequencias);

        boolean result = frequenciaService.verificarAlunoComMaisDe15FaltasNoMes(1L, data);

        assertTrue(result);
        verify(alunoService).atualizarAlertaAluno(1L, true);
    }

    @Test
    void verificarAlunoComMaisDe15FaltasNoMes_DeveRetornarFalseEAtualizarAlerta_QuandoTemMenosDe15Faltas() {
        LocalDate data = LocalDate.of(2024, 3, 20);
        LocalDate primeiroDiaDoMes = data.withDayOfMonth(1);
        LocalDate ultimoDiaDoMes = data.withDayOfMonth(data.lengthOfMonth());

        when(alunoRepository.existsById(1L)).thenReturn(true);
        
        // Criando 10 frequências com presente = false (faltas)
        List<Frequencia> frequencias = List.of(
            createFrequencia(false), createFrequencia(false), createFrequencia(false),
            createFrequencia(false), createFrequencia(false), createFrequencia(false),
            createFrequencia(false), createFrequencia(false), createFrequencia(false),
            createFrequencia(false)
        );
        
        when(frequenciaRepository.findByAlunoIdAndDataBetween(1L, primeiroDiaDoMes, ultimoDiaDoMes))
            .thenReturn(frequencias);

        boolean result = frequenciaService.verificarAlunoComMaisDe15FaltasNoMes(1L, data);

        assertFalse(result);
        verify(alunoService).atualizarAlertaAluno(1L, false);
    }

    @Test
    void verificarAlunoComMaisDe15FaltasNoMes_DeveLancarExcecao_QuandoAlunoNaoExiste() {
        LocalDate data = LocalDate.of(2024, 3, 20);
        
        when(alunoRepository.existsById(1L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> {
            frequenciaService.verificarAlunoComMaisDe15FaltasNoMes(1L, data);
        });
    }

    private Frequencia createFrequencia(boolean presente) {
        Frequencia frequencia = new Frequencia();
        frequencia.setAluno(aluno);
        frequencia.setData(LocalDate.of(2024, 3, 20));
        frequencia.setPresente(presente);
        frequencia.setObservacao(presente ? "Aluno presente" : "Aluno ausente");
        return frequencia;
    }
} 