package com.br.requirementhub.controller;


import com.br.requirementhub.dtos.requirement.RequirementRequestDTO;
import com.br.requirementhub.dtos.requirement.RequirementResponseDTO;
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

    @GetMapping
    public ResponseEntity<List<RequirementResponseDTO>> getAllRequirements() {
        return ResponseEntity.ok(service.getAllRequirements());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RequirementResponseDTO> getRequirementById(@PathVariable Long id) {
        RequirementResponseDTO requirement = service.getRequirementById(id);
        return requirement != null ? ResponseEntity.ok(requirement) : ResponseEntity.notFound().build();
    }

    @GetMapping("/requirement-id/{id}")
    public ResponseEntity<List<RequirementResponseDTO>> getRequirementDataToUpdate(@PathVariable Long id) {
        List<RequirementResponseDTO> requirement = service.getRequirementDataToUpdate(id);

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
