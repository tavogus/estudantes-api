package com.estudantes.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.estudantes.dto.FrequenciaDTO;
import com.estudantes.service.FrequenciaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/frequencias")
public class FrequenciaController {
    private final FrequenciaService frequenciaService;

    public FrequenciaController(FrequenciaService frequenciaService) {
        this.frequenciaService = frequenciaService;
    }

    @PostMapping
    public ResponseEntity<FrequenciaDTO> registrarFrequencia(@Valid @RequestBody FrequenciaDTO frequenciaDTO) {
        return new ResponseEntity<>(frequenciaService.registrarFrequencia(frequenciaDTO), HttpStatus.CREATED);
    }

    @GetMapping("/aluno/{alunoId}")
    public ResponseEntity<List<FrequenciaDTO>> buscarFrequenciasPorAluno(@PathVariable Long alunoId) {
        return ResponseEntity.ok(frequenciaService.buscarFrequenciasPorAluno(alunoId));
    }

    @GetMapping("/aluno/{alunoId}/periodo")
    public ResponseEntity<List<FrequenciaDTO>> buscarFrequenciasPorPeriodo(
            @PathVariable Long alunoId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {
        return ResponseEntity.ok(frequenciaService.buscarFrequenciasPorPeriodo(alunoId, dataInicio, dataFim));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FrequenciaDTO> atualizarFrequencia(
            @PathVariable Long id,
            @Valid @RequestBody FrequenciaDTO frequenciaDTO) {
        return ResponseEntity.ok(frequenciaService.atualizarFrequencia(id, frequenciaDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarFrequencia(@PathVariable Long id) {
        frequenciaService.deletarFrequencia(id);
        return ResponseEntity.noContent().build();
    }
} 