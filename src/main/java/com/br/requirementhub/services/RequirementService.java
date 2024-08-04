package com.br.requirementhub.services;

import com.br.requirementhub.dtos.requirement.RequirementRequestDTO;
import com.br.requirementhub.dtos.requirement.RequirementResponseDTO;
import com.br.requirementhub.entity.Project;
import com.br.requirementhub.entity.Requirement;
import com.br.requirementhub.repository.ProjectRepository;
import com.br.requirementhub.repository.RequirementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequirementService {

    private final RequirementRepository repository;
    private final ProjectRepository projectRepository;

    public List<RequirementResponseDTO> getAllRequirements() {
        return repository.findAll().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public RequirementResponseDTO getRequirementById(Long id) {
        return repository.findById(id)
                .map(this::convertToResponseDTO)
                .orElse(null);
    }

    public RequirementResponseDTO createRequirement(RequirementRequestDTO requirementRequestDTO) {
        Requirement requirement = convertToEntity(requirementRequestDTO);
        requirement = repository.save(requirement);
        return convertToResponseDTO(requirement);
    }

    public RequirementResponseDTO updateRequirement(Long id, RequirementRequestDTO requirementRequestDTO) {
        if (repository.existsById(id)) {
            Requirement requirement = convertToEntity(requirementRequestDTO);
            requirement.setId(id);
            requirement = repository.save(requirement);
            return convertToResponseDTO(requirement);
        }
        return null;
    }

    public void deleteRequirement(Long id) {
        repository.deleteById(id);
    }

    private RequirementResponseDTO convertToResponseDTO(Requirement requirement) {
        RequirementResponseDTO dto = new RequirementResponseDTO();
        dto.setId(requirement.getId());
        dto.setIdentifier(requirement.getIdentifier());
        dto.setName(requirement.getName());
        dto.setDescription(requirement.getDescription());
        dto.setVersion(requirement.getVersion());
        dto.setAuthor(requirement.getAuthor());
        dto.setSource(requirement.getSource());
        dto.setRisk(requirement.getRisk());
        dto.setPriority(requirement.getPriority());
        dto.setResponsible(requirement.getResponsible());
        dto.setType(requirement.getType());
        dto.setStatus(requirement.getStatus());
        dto.setEffort(requirement.getEffort());
        dto.setRelease(requirement.getRelease());
        dto.setDependency(requirement.getDependency());
        dto.setProjectId(requirement.getProject().getId());
        dto.setArtifactIds(requirement.getArtifacts().stream()
                .map(artifact -> artifact.getId())
                .collect(Collectors.toList()));
        return dto;
    }

    private Requirement convertToEntity(RequirementRequestDTO dto) {
        Requirement requirement = new Requirement();
        requirement.setIdentifier(dto.getIdentifier());
        requirement.setName(dto.getName());
        requirement.setDescription(dto.getDescription());
        requirement.setVersion(dto.getVersion());
        requirement.setAuthor(dto.getAuthor());
        requirement.setSource(dto.getSource());
        requirement.setRisk(dto.getRisk());
        requirement.setPriority(dto.getPriority());
        requirement.setResponsible(dto.getResponsible());
        requirement.setType(dto.getType());
        requirement.setStatus(dto.getStatus());
        requirement.setEffort(dto.getEffort());
        requirement.setRelease(dto.getRelease());
        requirement.setDependency(dto.getDependency());
        requirement.setProject(fetchProjectById(dto.getProjectId()));
        return requirement;
    }

    private Project fetchProjectById(Long projectId) {
        return projectRepository.findById(projectId).orElseThrow(() -> new RuntimeException("Project not found"));
    }
}