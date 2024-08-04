package com.br.requirementhub.services;

import com.br.requirementhub.dtos.requirementArtifact.RequirementArtifactRequestDTO;
import com.br.requirementhub.dtos.requirementArtifact.RequirementArtifactResponseDTO;
import com.br.requirementhub.entity.Requirement;
import com.br.requirementhub.entity.RequirementArtifact;
import com.br.requirementhub.repository.RequirementArtifactRepository;
import com.br.requirementhub.repository.RequirementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequirementArtifactService {

    private final RequirementArtifactRepository repository;
    private final RequirementRepository requirementRepository;

    public List<RequirementArtifactResponseDTO> findAll() {
        return repository.findAll().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public RequirementArtifactResponseDTO save(RequirementArtifactRequestDTO dto) throws IOException {
        RequirementArtifact artifact = convertToEntity(dto);
        artifact = repository.save(artifact);
        return convertToResponseDTO(artifact);
    }

    private RequirementArtifactResponseDTO convertToResponseDTO(RequirementArtifact artifact) {
        return new RequirementArtifactResponseDTO(
                artifact.getId(),
                artifact.getIdentify(),
                artifact.getType(),
                artifact.getDescription(),
                artifact.getArtifact(),
                artifact.getRequirement().getId()
        );
    }

    private RequirementArtifact convertToEntity(RequirementArtifactRequestDTO dto) throws IOException {
        RequirementArtifact artifact = new RequirementArtifact();
        artifact.setIdentify(dto.getIdentify());
        artifact.setType(dto.getType());
        artifact.setDescription(dto.getDescription());
        artifact.setArtifact(dto.getArtifact().getBytes());
        Requirement requirement = requirementRepository.findById(dto.getRequirementId())
                .orElseThrow(() -> new IllegalArgumentException("Requirement not found"));
        artifact.setRequirement(requirement);
        return artifact;
    }
}