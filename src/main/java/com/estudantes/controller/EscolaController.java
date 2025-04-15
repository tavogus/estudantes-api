package com.estudantes.controller;

import java.util.List;

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

import com.estudantes.dto.EscolaDTO;
import com.estudantes.service.EscolaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/escolas")
public class EscolaController {
    private final EscolaService escolaService;

    public EscolaController(EscolaService escolaService) {
        this.escolaService = escolaService;
    }

    @PostMapping
    public ResponseEntity<EscolaDTO> criarEscola(@Valid @RequestBody EscolaDTO escolaDTO) {
        return new ResponseEntity<>(escolaService.criarEscola(escolaDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EscolaDTO> buscarEscolaPorId(@PathVariable Long id) {
        return ResponseEntity.ok(escolaService.buscarEscolaPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<EscolaDTO>> listarEscolas(@RequestParam(required = false) String nome) {
        return ResponseEntity.ok(escolaService.listarEscolas(nome));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EscolaDTO> atualizarEscola(@PathVariable Long id, @Valid @RequestBody EscolaDTO escolaDTO) {
        return ResponseEntity.ok(escolaService.atualizarEscola(id, escolaDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarEscola(@PathVariable Long id) {
        escolaService.deletarEscola(id);
        return ResponseEntity.noContent().build();
    }
} 