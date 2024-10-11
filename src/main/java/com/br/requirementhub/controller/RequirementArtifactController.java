package com.br.requirementhub.controller;

import com.br.requirementhub.dtos.requirementArtifact.RequirementArtifactRequestDTO;
import com.br.requirementhub.dtos.requirementArtifact.RequirementArtifactResponseDTO;
import com.br.requirementhub.repository.RequirementArtifactRepository;
import com.br.requirementhub.services.RequirementArtifactService;
import com.br.requirementhub.services.RequirementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(path = "/requirement-artifacts", produces = "application/json")
@RequiredArgsConstructor
public class RequirementArtifactController {

    private final RequirementArtifactService service;
    private final RequirementService Requirementservice;
    private final RequirementArtifactRepository repository;

    @Operation(summary = "Create a new artifact")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Artifact created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "409", description = "Artifact already exists"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public RequirementArtifactResponseDTO create(@RequestBody RequirementArtifactRequestDTO requestDTO) throws IOException {
        RequirementArtifactRequestDTO dto = new RequirementArtifactRequestDTO();
        return service.save(requestDTO);
    }

    @Operation(summary = "Get all artifacts")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Artifacts retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public List<RequirementArtifactResponseDTO> getAll() {
        return service.findAll();
    }

    @Operation(summary = "Get artifact by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Artifact retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public RequirementArtifactResponseDTO getById(@PathVariable Long id) {
        return service.findById(id);
    }

    @Operation(summary = "Get artifacts by requirement id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Artifacts retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/by-requirement/{requirementId}")
    public ResponseEntity<List<RequirementArtifactResponseDTO>> getArtifactsByRequirementId(@PathVariable Long requirementId) {
        List<RequirementArtifactResponseDTO> artifacts = service.findArtifactsByRequirementId(requirementId);
        return ResponseEntity.ok(artifacts);
    }

    @Operation(summary = "Get artifacts by project id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Artifacts retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/by-project/{projectId}")
    public ResponseEntity<List<RequirementArtifactResponseDTO>> getArtifactsByProjectId(@PathVariable Long projectId) {
        List<RequirementArtifactResponseDTO> artifacts = Requirementservice.getArtifactsByProjectId(projectId);
        return ResponseEntity.ok(artifacts);
    }


    @PutMapping("/{id}")
    public ResponseEntity<RequirementArtifactResponseDTO> update(@PathVariable Long id, @RequestBody RequirementArtifactRequestDTO request) throws IOException {
        RequirementArtifactResponseDTO updatedProject = service.update(id, request);
        return ResponseEntity.ok(updatedProject);
    }

    @DeleteMapping("/{id}")
    public void deleteArtifact(@PathVariable Long id) {
        service.deleteById(id);
    }
}
