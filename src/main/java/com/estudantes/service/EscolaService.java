package com.estudantes.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.estudantes.dto.EscolaDTO;
import com.estudantes.entity.Escola;
import com.estudantes.repository.EscolaRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
@Transactional
public class EscolaService {
    private final EscolaRepository escolaRepository;

    public EscolaService(EscolaRepository escolaRepository) {
        this.escolaRepository = escolaRepository;
    }

    public EscolaDTO criarEscola(EscolaDTO escolaDTO) {
        Escola escola = new Escola();
        escola.setNome(escolaDTO.nome());
        escola.setEndereco(escolaDTO.endereco());
        escola.setTelefone(escolaDTO.telefone());
        
        escola = escolaRepository.save(escola);
        return new EscolaDTO(escola.getId(), escola.getNome(), escola.getEndereco(), escola.getTelefone());
    }

    public EscolaDTO buscarEscolaPorId(Long id) {
        Escola escola = escolaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Escola não encontrada com ID: " + id));
        return new EscolaDTO(escola.getId(), escola.getNome(), escola.getEndereco(), escola.getTelefone());
    }

    public List<EscolaDTO> listarEscolas() {
        return escolaRepository.findAll().stream()
                .map(escola -> new EscolaDTO(escola.getId(), escola.getNome(), escola.getEndereco(), escola.getTelefone()))
                .toList();
    }

    public EscolaDTO atualizarEscola(Long id, EscolaDTO escolaDTO) {
        Escola escola = escolaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Escola não encontrada com ID: " + id));

        escola.setNome(escolaDTO.nome());
        escola.setEndereco(escolaDTO.endereco());
        escola.setTelefone(escolaDTO.telefone());

        escola = escolaRepository.save(escola);
        return new EscolaDTO(escola.getId(), escola.getNome(), escola.getEndereco(), escola.getTelefone());
    }

    public void deletarEscola(Long id) {
        if (!escolaRepository.existsById(id)) {
            throw new EntityNotFoundException("Escola não encontrada com ID: " + id);
        }
        escolaRepository.deleteById(id);
    }
} 