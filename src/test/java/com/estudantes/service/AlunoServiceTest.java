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

import com.estudantes.dto.AlunoDTO;
import com.estudantes.entity.Aluno;
import com.estudantes.entity.Aluno.TipoBeneficio;
import com.estudantes.entity.Escola;
import com.estudantes.repository.AlunoRepository;
import com.estudantes.repository.EscolaRepository;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class AlunoServiceTest {

    @Mock
    private AlunoRepository alunoRepository;

    @Mock
    private EscolaRepository escolaRepository;

    @InjectMocks
    private AlunoService alunoService;

    private AlunoDTO alunoDTO;
    private Aluno aluno;
    private Escola escola;

    @BeforeEach
    void setUp() {
        escola = new Escola();
        escola.setId(1L);
        escola.setNome("Escola Teste");

        aluno = new Aluno();
        aluno.setId(1L);
        aluno.setNome("João Silva");
        aluno.setCpf("12345678900");
        aluno.setDataNascimento(LocalDate.of(2000, 1, 1));
        aluno.setEndereco("Rua Teste, 123");
        aluno.setTelefone("11999999999");
        aluno.setTipoBeneficio(TipoBeneficio.BOLSISTA);
        aluno.setEscola(escola);
        aluno.setAlerta(false);

        alunoDTO = new AlunoDTO(
            1L,
            "João Silva",
            "12345678900",
            LocalDate.of(2000, 1, 1),
            "Rua Teste, 123",
            "11999999999",
            TipoBeneficio.BOLSISTA,
            1L,
            false
        );
    }

    @Test
    void criarAluno_DeveRetornarAlunoDTO_QuandoDadosValidos() {
        when(escolaRepository.findById(1L)).thenReturn(Optional.of(escola));
        when(alunoRepository.existsByCpf("12345678900")).thenReturn(false);
        when(alunoRepository.save(any(Aluno.class))).thenReturn(aluno);

        AlunoDTO result = alunoService.criarAluno(alunoDTO);

        assertNotNull(result);
        assertEquals(alunoDTO.id(), result.id());
        assertEquals(alunoDTO.nome(), result.nome());
        assertEquals(alunoDTO.cpf(), result.cpf());
        assertEquals(alunoDTO.alerta(), result.alerta());
        verify(alunoRepository).save(any(Aluno.class));
    }

    @Test
    void criarAluno_DeveLancarExcecao_QuandoCPFJaExiste() {
        when(alunoRepository.existsByCpf("12345678900")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> {
            alunoService.criarAluno(alunoDTO);
        });
    }

    @Test
    void buscarAlunoPorId_DeveRetornarAlunoDTO_QuandoAlunoExiste() {
        when(alunoRepository.findById(1L)).thenReturn(Optional.of(aluno));

        AlunoDTO result = alunoService.buscarAlunoPorId(1L);

        assertNotNull(result);
        assertEquals(alunoDTO.id(), result.id());
        assertEquals(alunoDTO.nome(), result.nome());
        assertEquals(alunoDTO.alerta(), result.alerta());
    }

    @Test
    void buscarAlunoPorId_DeveLancarExcecao_QuandoAlunoNaoExiste() {
        when(alunoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            alunoService.buscarAlunoPorId(1L);
        });
    }

    @Test
    void listarAlunosPorEscola_DeveRetornarListaDeAlunos() {
        when(alunoRepository.findByEscolaId(1L)).thenReturn(List.of(aluno));

        List<AlunoDTO> result = alunoService.listarAlunosPorEscola(1L);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(alunoDTO.id(), result.get(0).id());
        assertEquals(alunoDTO.alerta(), result.get(0).alerta());
    }

    @Test
    void atualizarAluno_DeveRetornarAlunoDTOAtualizado_QuandoDadosValidos() {
        when(alunoRepository.findById(1L)).thenReturn(Optional.of(aluno));
        when(escolaRepository.findById(1L)).thenReturn(Optional.of(escola));
        when(alunoRepository.save(any(Aluno.class))).thenReturn(aluno);

        AlunoDTO result = alunoService.atualizarAluno(1L, alunoDTO);

        assertNotNull(result);
        assertEquals(alunoDTO.id(), result.id());
        assertEquals(alunoDTO.nome(), result.nome());
        assertEquals(alunoDTO.alerta(), result.alerta());
        verify(alunoRepository).save(any(Aluno.class));
    }

    @Test
    void atualizarAlertaAluno_DeveAtualizarAlerta_QuandoAlunoExiste() {
        when(alunoRepository.findById(1L)).thenReturn(Optional.of(aluno));
        when(alunoRepository.save(any(Aluno.class))).thenReturn(aluno);

        alunoService.atualizarAlertaAluno(1L, true);

        assertTrue(aluno.isAlerta());
        verify(alunoRepository).save(aluno);
    }

    @Test
    void atualizarAlertaAluno_DeveLancarExcecao_QuandoAlunoNaoExiste() {
        when(alunoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            alunoService.atualizarAlertaAluno(1L, true);
        });
    }

    @Test
    void deletarAluno_DeveDeletarAluno_QuandoAlunoExiste() {
        when(alunoRepository.existsById(1L)).thenReturn(true);
        doNothing().when(alunoRepository).deleteById(1L);

        assertDoesNotThrow(() -> {
            alunoService.deletarAluno(1L);
        });

        verify(alunoRepository).deleteById(1L);
    }

    @Test
    void deletarAluno_DeveLancarExcecao_QuandoAlunoNaoExiste() {
        when(alunoRepository.existsById(1L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> {
            alunoService.deletarAluno(1L);
        });
    }
} 