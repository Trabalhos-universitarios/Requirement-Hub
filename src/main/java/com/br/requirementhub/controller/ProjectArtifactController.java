package com.br.requirementhub.controller;

import com.br.requirementhub.dtos.projectArtifact.ProjectArtifactRequestDTO;
import com.br.requirementhub.dtos.projectArtifact.ProjectArtifactResponseDTO;
import com.br.requirementhub.services.ProjectArtifactService;
import com.br.requirementhub.utils.DecodeBase64;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.io.IOException;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/project-artifacts", produces = "application/json")
public class ProjectArtifactController {

    private final ProjectArtifactService service;

    public ProjectArtifactController(ProjectArtifactService service) {
        this.service = service;
    }

    @Operation(summary = "Get all artifacts")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Artifacts retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/all")
    public List<ProjectArtifactResponseDTO> getAllArtifacts() {
        return service.findAll();
    }

    @Operation(summary = "Get artifact by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Artifact retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public ProjectArtifactResponseDTO getArtifactById(@PathVariable Long id) {
        return service.findById(id);
    }

    @Operation(summary = "Create a new artifact")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Artifact created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "409", description = "Artifact already exists"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("")
    public ResponseEntity<ProjectArtifactResponseDTO> create(@RequestBody ProjectArtifactRequestDTO request) throws IOException {
        String contentBase64 = request.getContentBase64();
        if (contentBase64 != null && contentBase64.startsWith("data:")) {
            contentBase64 = contentBase64.split(",")[1];
        }
        byte[] content = DecodeBase64.decode(contentBase64);
        request.setContent(content);

        ProjectArtifactResponseDTO responseDTO = service.create(request);
        return ResponseEntity.status(201).body(responseDTO);
    }

    @Operation(summary = "Update an artifact")
    @DeleteMapping("/{id}")
    public void deleteArtifact(@PathVariable Long id) {
        service.deleteById(id);
    }

    @Operation(summary = "Download an artifact")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Artifact downloaded successfully"),
            @ApiResponse(responseCode = "404", description = "Artifact not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable Long id) {
        ProjectArtifactResponseDTO artifact = service.findById(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + artifact.getFileName() + "\"")
                .contentType(MediaType.parseMediaType(artifact.getType()))
                .body(artifact.getContent());
    }

    @Operation(summary = "Get an image as base64")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Image retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Image not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/image/{id}")
    public ResponseEntity<String> getImageBase64(@PathVariable Long id) {
        ProjectArtifactResponseDTO artifact = service.findById(id);

        String base64Content = "data:" + artifact.getType() + ";base64," +
                DecodeBase64.encodeToString(artifact.getContent());

        return ResponseEntity.ok(base64Content);
    }

    @Operation(summary = "Get artifacts by project id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Artifacts retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Project not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/by-project/{projectId}")
    public ResponseEntity<List<ProjectArtifactResponseDTO>> getArtifactsByProjectId(@PathVariable Long projectId) {
        List<ProjectArtifactResponseDTO> artifacts = service.findArtifactsByProjectId(projectId);
        return ResponseEntity.ok(artifacts);
    }

    @Operation(summary = "Delete artifacts by project id")
    @DeleteMapping("/by-project/{projectId}")
    public ResponseEntity<Void> deleteArtifactsByProjectId(@PathVariable Long projectId) {
        service.deleteArtifactsByProjectId(projectId);
        return ResponseEntity.noContent().build();
    }
}
