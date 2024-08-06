package com.br.requirementhub.controller;

import com.br.requirementhub.dtos.project.ProjectRequestDTO;
import com.br.requirementhub.dtos.project.ProjectResponseDTO;
import com.br.requirementhub.entity.Project;
import com.br.requirementhub.services.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/project")
public class ProjectController {

    private final ProjectService service;

    @PostMapping("")
    public ResponseEntity<ProjectResponseDTO> create(@RequestBody ProjectRequestDTO request) throws IOException {
        ProjectResponseDTO responseDTO = service.create(request);
        return ResponseEntity.status(201).body(responseDTO);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ProjectResponseDTO>> list() {
        return ResponseEntity.ok(service.list());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponseDTO> find(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectResponseDTO> update(@PathVariable Long id, @RequestBody ProjectRequestDTO request) throws IOException {
        ProjectResponseDTO updatedProject = service.update(id, request);
        return ResponseEntity.ok(updatedProject);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
