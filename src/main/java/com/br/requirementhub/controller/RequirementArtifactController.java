package com.br.requirementhub.controller;


import com.br.requirementhub.dtos.requirementArtifact.RequirementArtifactRequestDTO;
import com.br.requirementhub.dtos.requirementArtifact.RequirementArtifactResponseDTO;
import com.br.requirementhub.services.RequirementArtifactService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/requirement-artifacts")
@RequiredArgsConstructor
public class RequirementArtifactController {

    private final RequirementArtifactService service;


    @PostMapping
    public RequirementArtifactResponseDTO create(
            @RequestParam("identify") String identify,
            @RequestParam("type") String type,
            @RequestParam("description") String description,
            @RequestParam("artifact") MultipartFile artifact,
            @RequestParam("requirementId") Long requirementId) throws IOException {
        RequirementArtifactRequestDTO dto = new RequirementArtifactRequestDTO();
        dto.setIdentify(identify);
        dto.setType(type);
        dto.setDescription(description);
        dto.setArtifact(artifact);
        dto.setRequirementId(requirementId);
        return service.save(dto);
    }

    @GetMapping
    public List<RequirementArtifactResponseDTO> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public RequirementArtifactResponseDTO getById(@PathVariable Long id) {
        return service.findById(id);
    }
}
