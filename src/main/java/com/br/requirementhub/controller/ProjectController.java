package com.br.requirementhub.controller;

import com.br.requirementhub.dtos.ProjectRequestDTO;
import com.br.requirementhub.dtos.ProjectResponseDTO;
import com.br.requirementhub.services.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/project")
public class ProjectController {

    private final ProjectService service;

    @PostMapping("")
    public ResponseEntity<ProjectResponseDTO> create(@RequestBody ProjectRequestDTO request) {
        final ProjectResponseDTO output = service.create(request);
        return ResponseEntity.status(HttpStatus.OK).body(output);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ProjectResponseDTO>> list() {
        return ResponseEntity.ok(service.list());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponseDTO> find(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }
}
