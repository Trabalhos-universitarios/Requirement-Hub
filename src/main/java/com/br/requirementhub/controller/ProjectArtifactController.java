package com.br.requirementhub.controller;

import com.br.requirementhub.dtos.projectArtifact.ProjectArtifactRequestDTO;
import com.br.requirementhub.dtos.projectArtifact.ProjectArtifactResponseDTO;
import com.br.requirementhub.services.ProjectArtifactService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/project-artifacts")
public class ProjectArtifactController {

    @Autowired
    private ProjectArtifactService service;

    @GetMapping
    public List<ProjectArtifactResponseDTO> getAllArtifacts() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ProjectArtifactResponseDTO getArtifactById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public ProjectArtifactResponseDTO createArtifact(
            @RequestParam("name") String name,
            @RequestParam("type") String type,
            @RequestParam("artifact_file") MultipartFile artifact,
            @RequestParam("project_id") Long projectId) throws IOException {
        ProjectArtifactRequestDTO dto = new ProjectArtifactRequestDTO();
        dto.setName(name);
        dto.setType(type);
        dto.setArtifact_file(artifact);
        dto.setProjectId(projectId);
        return service.save(dto);
    }

    @DeleteMapping("/{id}")
    public void deleteArtifact(@PathVariable Long id) {
        service.deleteById(id);
    }
}

