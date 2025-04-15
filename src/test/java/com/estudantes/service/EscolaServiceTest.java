package com.estudantes.service;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.estudantes.dto.EscolaDTO;
import com.estudantes.entity.Escola;
import com.estudantes.repository.EscolaRepository;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class EscolaServiceTest {

    @Mock
    private EscolaRepository escolaRepository;

    @InjectMocks
    private EscolaService escolaService;

    private EscolaDTO escolaDTO;
    private Escola escola;

    @BeforeEach
    void setUp() {
        escola = new Escola();
        escola.setId(1L);
        escola.setNome("Escola Municipal São Paulo");
        escola.setEndereco("Rua das Flores, 123");
        escola.setTelefone("(11) 1234-5678");

        escolaDTO = new EscolaDTO(
            1L,
            "Escola Municipal São Paulo",
            "Rua das Flores, 123",
            "(11) 1234-5678"
        );
    }

    @Test
    void criarEscola_DeveRetornarEscolaDTO_QuandoDadosValidos() {
        when(escolaRepository.save(any(Escola.class))).thenReturn(escola);

        EscolaDTO result = escolaService.criarEscola(escolaDTO);

        assertNotNull(result);
        assertEquals(escolaDTO.id(), result.id());
        assertEquals(escolaDTO.nome(), result.nome());
        assertEquals(escolaDTO.endereco(), result.endereco());
        assertEquals(escolaDTO.telefone(), result.telefone());
        verify(escolaRepository).save(any(Escola.class));
    }

    @Test
    void buscarEscolaPorId_DeveRetornarEscolaDTO_QuandoEscolaExiste() {
        when(escolaRepository.findById(1L)).thenReturn(Optional.of(escola));

        EscolaDTO result = escolaService.buscarEscolaPorId(1L);

        assertNotNull(result);
        assertEquals(escolaDTO.id(), result.id());
        assertEquals(escolaDTO.nome(), result.nome());
        assertEquals(escolaDTO.endereco(), result.endereco());
        assertEquals(escolaDTO.telefone(), result.telefone());
    }

    @Test
    void buscarEscolaPorId_DeveLancarExcecao_QuandoEscolaNaoExiste() {
        when(escolaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            escolaService.buscarEscolaPorId(1L);
        });
    }

    @Test
    void listarEscolas_DeveRetornarListaDeEscolas_SemFiltro() {
        when(escolaRepository.findByNomeContainingIgnoreCase(eq(null)))
            .thenReturn(List.of(escola));

        List<EscolaDTO> result = escolaService.listarEscolas(null);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(escolaDTO.id(), result.get(0).id());
        assertEquals(escolaDTO.nome(), result.get(0).nome());
        assertEquals(escolaDTO.endereco(), result.get(0).endereco());
        assertEquals(escolaDTO.telefone(), result.get(0).telefone());
    }

    @Test
    void listarEscolas_DeveRetornarListaDeEscolas_ComFiltroNome() {
        when(escolaRepository.findByNomeContainingIgnoreCase(eq("municipal")))
            .thenReturn(List.of(escola));

        List<EscolaDTO> result = escolaService.listarEscolas("municipal");

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(escolaDTO.id(), result.get(0).id());
        assertEquals(escolaDTO.nome(), result.get(0).nome());
        assertEquals(escolaDTO.endereco(), result.get(0).endereco());
        assertEquals(escolaDTO.telefone(), result.get(0).telefone());
    }

    @Test
    void atualizarEscola_DeveRetornarEscolaDTOAtualizado_QuandoDadosValidos() {
        when(escolaRepository.findById(1L)).thenReturn(Optional.of(escola));
        when(escolaRepository.save(any(Escola.class))).thenReturn(escola);

        EscolaDTO result = escolaService.atualizarEscola(1L, escolaDTO);

        assertNotNull(result);
        assertEquals(escolaDTO.id(), result.id());
        assertEquals(escolaDTO.nome(), result.nome());
        assertEquals(escolaDTO.endereco(), result.endereco());
        assertEquals(escolaDTO.telefone(), result.telefone());
        verify(escolaRepository).save(any(Escola.class));
    }

    @Test
    void atualizarEscola_DeveLancarExcecao_QuandoEscolaNaoExiste() {
        when(escolaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            escolaService.atualizarEscola(1L, escolaDTO);
        });
    }

    @Test
    void deletarEscola_DeveDeletarEscola_QuandoEscolaExiste() {
        when(escolaRepository.existsById(1L)).thenReturn(true);
        doNothing().when(escolaRepository).deleteById(1L);

        assertDoesNotThrow(() -> {
            escolaService.deletarEscola(1L);
        });

        verify(escolaRepository).deleteById(1L);
    }

    @Test
    void deletarEscola_DeveLancarExcecao_QuandoEscolaNaoExiste() {
        when(escolaRepository.existsById(1L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> {
            escolaService.deletarEscola(1L);
        });
    }
} 