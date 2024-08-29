package com.br.requirementhub.controller;


import com.br.requirementhub.dtos.requirement.RequirementRequestDTO;
import com.br.requirementhub.dtos.requirement.RequirementResponseDTO;
import com.br.requirementhub.dtos.requirementArtifact.RequirementArtifactResponseDTO;
import com.br.requirementhub.entity.RequirementArtifact;
import com.br.requirementhub.repository.RequirementArtifactRepository;
import com.br.requirementhub.services.RequirementArtifactService;
import com.br.requirementhub.services.RequirementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/requirements")
public class RequirementController {

    private final RequirementService service;
    private final RequirementArtifactService artifactService;
    private final RequirementArtifactRepository requirementArtifactRepository;

    @GetMapping
    public ResponseEntity<List<RequirementResponseDTO>> getAllRequirements() {
        return ResponseEntity.ok(service.getAllRequirements());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RequirementResponseDTO> getRequirementById(@PathVariable Long id) {
        RequirementResponseDTO requirement = service.getRequirementById(id);
        return requirement != null ? ResponseEntity.ok(requirement) : ResponseEntity.notFound().build();
    }

    @GetMapping("/requirementByProjectRelated/{identifier}/{projectId}")
    public ResponseEntity<RequirementResponseDTO> getRequirementByIdentifierAndProjectRelated(@PathVariable String identifier, @PathVariable Long projectId) {
        List<RequirementResponseDTO> requirements = service.getRequirementsByProjectRelated(projectId);
        RequirementResponseDTO requirement = requirements.stream()
                .filter(req -> identifier.equals(req.getIdentifier()))
                .findFirst()
                .orElse(null);
        return requirement != null ? ResponseEntity.ok(requirement) : ResponseEntity.notFound().build();
    }

    @GetMapping("/artifactRequirementByProjectRelated/{identifier}/{projectId}")
    public ResponseEntity<RequirementArtifactResponseDTO> getRequirementArtifactByIdentifierAndProjectRelated(@PathVariable String identifier, @PathVariable Long projectId) {
        List<RequirementResponseDTO> requirements = service.getRequirementsByProjectRelated(projectId);

        for (RequirementResponseDTO requirement : requirements) {
            List<RequirementArtifactResponseDTO> artifacts = artifactService.findArtifactsByRequirementId(requirement.getId());

            for (RequirementArtifactResponseDTO artifact : artifacts) {
                if (identifier.equals(artifact.getIdentifier())) {

                    RequirementArtifact requirementArtifact = requirementArtifactRepository.findByIdentifierArtifact(identifier);

                    RequirementArtifactResponseDTO responseDTO = artifactService.convertToResponseDTO(requirementArtifact);
                    return ResponseEntity.ok(responseDTO);
                }
            }
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/project-id/{id}")
    public ResponseEntity<List<RequirementResponseDTO>> getRequirementByProjectRelated(@PathVariable Long id) {
        List<RequirementResponseDTO> requirement = service.getRequirementsByProjectRelated(id);
        return requirement != null ? ResponseEntity.ok( requirement) : ResponseEntity.notFound().build();
    }

    @GetMapping("/byproject/{id}")
    public ResponseEntity<List<RequirementResponseDTO>> listRequirementsByProjectId(@PathVariable Long id) {
        List<RequirementResponseDTO> requirement = service.listByProjectId(id);
        return requirement != null ? ResponseEntity.ok( requirement) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<RequirementResponseDTO> createRequirement(@RequestBody RequirementRequestDTO requirementRequestDTO) {
        RequirementResponseDTO createdRequirement = service.createRequirement(requirementRequestDTO);
        return ResponseEntity.status(201).body(createdRequirement);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RequirementResponseDTO> updateRequirement(@PathVariable Long id, @RequestBody RequirementRequestDTO requirementRequestDTO) {
        RequirementResponseDTO updatedRequirement = service.updateRequirement(id, requirementRequestDTO);
        return updatedRequirement != null ? ResponseEntity.ok(updatedRequirement) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRequirement(@PathVariable Long id) {
        service.deleteRequirement(id);
        return ResponseEntity.noContent().build();
    }
}
