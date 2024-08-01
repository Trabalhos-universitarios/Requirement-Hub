package com.br.requirementhub.controller;

import com.br.requirementhub.dtos.ProjectRequestDTO;
import com.br.requirementhub.dtos.ProjectResponseDTO;
import com.br.requirementhub.services.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/project")
public class ProjectController {

    private final ProjectService service;

    @PostMapping("")
    public ResponseEntity<ProjectResponseDTO> create(@ModelAttribute ProjectRequestDTO request) throws IOException {
        return ResponseEntity.ok(service.create(request));
    }

    @GetMapping("/all")
    public ResponseEntity<List<ProjectResponseDTO>> list() {
        return ResponseEntity.ok(service.list());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponseDTO> find(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadArtifact(@PathVariable Long id) {
        ProjectResponseDTO project = service.findById(id);
        if (project == null) {
            return ResponseEntity.notFound().build();
        }
        byte[] artifact = project.getArtifact_file();
        if (artifact != null && artifact.length > 0) {
            ByteArrayResource resource = new ByteArrayResource(artifact);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=artifact")
                    .contentLength(artifact.length)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
