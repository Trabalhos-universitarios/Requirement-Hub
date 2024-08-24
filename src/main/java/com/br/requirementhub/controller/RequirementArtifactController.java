package com.br.requirementhub.controller;


import com.br.requirementhub.dtos.requirementArtifact.RequirementArtifactRequestDTO;
import com.br.requirementhub.dtos.requirementArtifact.RequirementArtifactResponseDTO;
import com.br.requirementhub.services.RequirementArtifactService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/requirement-artifacts")
@RequiredArgsConstructor
public class RequirementArtifactController {

    private final RequirementArtifactService service;

    @PostMapping
    public RequirementArtifactResponseDTO create(@RequestBody RequirementArtifactRequestDTO requestDTO) throws IOException {
        RequirementArtifactRequestDTO dto = new RequirementArtifactRequestDTO();
        return service.save(requestDTO);
    }

    @GetMapping
    public List<RequirementArtifactResponseDTO> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public RequirementArtifactResponseDTO getById(@PathVariable Long id) {
        return service.findById(id);
    }

    @GetMapping("/by-requirement/{requirementId}")
    public ResponseEntity<List<RequirementArtifactResponseDTO>> getArtifactsByRequirementId(@PathVariable Long requirementId) {
        List<RequirementArtifactResponseDTO> artifacts = service.findArtifactsByRequirementId(requirementId);
        return ResponseEntity.ok(artifacts);
    }
}
