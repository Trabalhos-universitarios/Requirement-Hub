package com.br.requirementhub.services;

import com.br.requirementhub.dtos.requirement.RequirementRequestDTO;
import com.br.requirementhub.dtos.requirement.RequirementResponseDTO;
import com.br.requirementhub.entity.Project;
import com.br.requirementhub.entity.Requirement;
import com.br.requirementhub.entity.RequirementArtifact;
import com.br.requirementhub.entity.Stakeholder;
import com.br.requirementhub.entity.User;
import com.br.requirementhub.exceptions.ProjectNotFoundException;
import com.br.requirementhub.exceptions.RequirementException;
import com.br.requirementhub.repository.ProjectRepository;
import com.br.requirementhub.repository.RequirementRepository;
import com.br.requirementhub.repository.StakeHolderRepository;
import com.br.requirementhub.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RequirementService {

    private final RequirementRepository requirementRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final StakeHolderRepository stakeholderRepository;

    public List<RequirementResponseDTO> getAllRequirements() {
        return requirementRepository.findAll().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public RequirementResponseDTO getRequirementById(Long id) {
        return requirementRepository.findById(id)
                .map(this::convertToResponseDTO)
                .orElse(null);
    }

    public RequirementResponseDTO createRequirement(RequirementRequestDTO requirementRequestDTO) {
        this.getRequirementByNameAndDescription(requirementRequestDTO);

        Requirement requirement = convertToEntity(requirementRequestDTO);

        requirement.setResponsible(getResponsible(requirementRequestDTO.getResponsible()));
        requirement.setStakeholders(getStakeholders(requirementRequestDTO.getStakeholders()));
        requirement.setDependencies(getRequirementsRelated(requirementRequestDTO.getDependencies()));

        requirement = requirementRepository.save(requirement);
        requirement.autoCompleteData();
        requirement = requirementRepository.save(requirement);
        return convertToResponseDTO(requirement);
    }

    private void getRequirementByNameAndDescription(RequirementRequestDTO request) {
        Optional<Requirement> foundName = requirementRepository.findByName(request.getName());
        Optional<Requirement> foundDescription = requirementRepository.findByDescription(request.getDescription());
        if (foundName.isPresent() && foundDescription.isPresent()) {
            throw new RequirementException("This requirement already exists!");
        }
    }

    private Set<User> getResponsible(Set<User> users) {
        Set<User> managedUsers = new HashSet<>();
        for (User user : users) {
            User managedUser = userRepository.findById(user.getId())
                    .orElseThrow(() -> new EntityNotFoundException("User not found: " + user.getId()));
            managedUsers.add(managedUser);
        }
        return managedUsers;
    }

    private Set<Stakeholder> getStakeholders(Set<Stakeholder> stakeholders) {
        Set<Stakeholder> managedStakeholders = new HashSet<>();
        for (Stakeholder stakeholder : stakeholders) {
            Stakeholder managedStakeholder = stakeholderRepository.findById(stakeholder.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Stakeholder not found: " + stakeholder.getId()));
            managedStakeholders.add(managedStakeholder);
        }
        return managedStakeholders;
    }

    private Set<Requirement> getRequirementsRelated(Set<Requirement> dependencies) {
        Set<Requirement> managedRequirements = new HashSet<>();
        for (Requirement dependency : dependencies) {
            Requirement managedRequirement = requirementRepository.findById(dependency.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Requirement not found: " + dependency.getId()));
            managedRequirements.add(managedRequirement);
        }
        return managedRequirements;
    }

    public RequirementResponseDTO updateRequirement(Long id, RequirementRequestDTO requirementRequestDTO) {
        if (requirementRepository.existsById(id)) {
            Requirement requirement = convertToEntity(requirementRequestDTO);
            requirement.setId(id);
            requirement = requirementRepository.save(requirement);
            return convertToResponseDTO(requirement);
        } else {
            throw new ProjectNotFoundException("Project not found with id: " + id);
        }
    }

    public void deleteRequirement(Long id) {
        requirementRepository.deleteById(id);
    }

    private RequirementResponseDTO convertToResponseDTO(Requirement requirement) {
        RequirementResponseDTO dto = new RequirementResponseDTO();
        dto.setId(requirement.getId());
        dto.setIdentifier(requirement.getIdentifier());
        dto.setName(requirement.getName());
        dto.setDescription(requirement.getDescription());
        dto.setVersion(requirement.getVersion());
        dto.setAuthor(requirement.getAuthor());
        dto.setRisk(requirement.getRisk());
        dto.setPriority(requirement.getPriority());
        dto.setType(requirement.getType());
        dto.setStatus(requirement.getStatus());
        dto.setEffort(requirement.getEffort());
        dto.setRelease(requirement.getRelease());
        dto.setProjectId(requirement.getProjectRelated().getId());
        dto.setDateCreated(requirement.getDateCreated());
        dto.setArtifactIds(requirement.getArtifacts().stream()
                .map(RequirementArtifact::getId)
                .collect(Collectors.toSet()));
        dto.setResponsibleIds(requirement.getResponsible().stream()
                .map(User::getId)
                .collect(Collectors.toSet()));
        dto.setDependencyIds(requirement.getDependencies().stream()
                .map(Requirement::getId)
                .collect(Collectors.toSet()));
        dto.setStakeholderIds(requirement.getStakeholders().stream()
                .map(Stakeholder::getId)
                .collect(Collectors.toSet()));
        return dto;
    }

    private Requirement convertToEntity(RequirementRequestDTO dto) {
        Requirement requirement = new Requirement();
        requirement.setName(dto.getName());
        requirement.setDescription(dto.getDescription());
        requirement.setRisk(dto.getRisk());
        requirement.setPriority(dto.getPriority());
        requirement.setType(dto.getType());
        requirement.setEffort(dto.getEffort());
        Project project = projectRepository.findById(dto.getProjectRelated().getId())
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with id: " + dto.getProjectRelated()));
        requirement.setProjectRelated(project);
        return requirement;
    }
}