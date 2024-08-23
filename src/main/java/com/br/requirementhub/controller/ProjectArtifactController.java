package com.br.requirementhub.controller;

import com.br.requirementhub.dtos.projectArtifact.ProjectArtifactRequestDTO;
import com.br.requirementhub.dtos.projectArtifact.ProjectArtifactResponseDTO;
import com.br.requirementhub.services.ProjectArtifactService;
import com.br.requirementhub.utils.DecodeBase64;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/project-artifacts")
public class ProjectArtifactController {

    private final ProjectArtifactService service;

    public ProjectArtifactController(ProjectArtifactService service) {
        this.service = service;
    }

    @GetMapping("/all")
    public List<ProjectArtifactResponseDTO> getAllArtifacts() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ProjectArtifactResponseDTO getArtifactById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping("")
    public ResponseEntity<ProjectArtifactResponseDTO> create(@RequestBody ProjectArtifactRequestDTO request) throws IOException {

        // Remove o prefixo MIME do conteúdo base64, se presente
        String contentBase64 = request.getContentBase64();
        if (contentBase64 != null && contentBase64.startsWith("data:")) {
            contentBase64 = contentBase64.split(",")[1];
        }

        // Decodifica o conteúdo base64
        byte[] content = DecodeBase64.decode(contentBase64);
        request.setContent(content);

        ProjectArtifactResponseDTO responseDTO = service.create(request);
        return ResponseEntity.status(201).body(responseDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteArtifact(@PathVariable Long id) {
        service.deleteById(id);
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable Long id) {
        ProjectArtifactResponseDTO artifact = service.findById(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + artifact.getFileName() + "\"")
                .contentType(MediaType.parseMediaType(artifact.getType()))
                .body(artifact.getContent());
    }

    @GetMapping("/image/{id}")
    public ResponseEntity<String> getImageBase64(@PathVariable Long id) {
        ProjectArtifactResponseDTO artifact = service.findById(id);

        String base64Content = "data:" + artifact.getType() + ";base64," +
                DecodeBase64.encodeToString(artifact.getContent());

        return ResponseEntity.ok(base64Content);
    }

    @GetMapping("/by-project/{projectId}")
    public ResponseEntity<List<ProjectArtifactResponseDTO>> getArtifactsByProjectId(@PathVariable Long projectId) {
        List<ProjectArtifactResponseDTO> artifacts = service.findArtifactsByProjectId(projectId);
        return ResponseEntity.ok(artifacts);
    }

    @DeleteMapping("/by-project/{projectId}")
    public ResponseEntity<Void> deleteArtifactsByProjectId(@PathVariable Long projectId) {
        service.deleteArtifactsByProjectId(projectId);
        return ResponseEntity.noContent().build();
    }
}
