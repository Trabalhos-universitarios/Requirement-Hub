package com.br.requirementhub.services;

import com.br.requirementhub.dtos.requirement.RequirementRequestDTO;
import com.br.requirementhub.dtos.requirement.RequirementResponseDTO;
import com.br.requirementhub.entity.Project;
import com.br.requirementhub.entity.Requirement;
import com.br.requirementhub.entity.RequirementArtifact;
import com.br.requirementhub.entity.Stakeholder;
import com.br.requirementhub.entity.User;
import com.br.requirementhub.enums.Status;
import com.br.requirementhub.exceptions.ProjectNotFoundException;
import com.br.requirementhub.exceptions.RequirementAlreadyExistException;
import com.br.requirementhub.repository.ProjectRepository;
import com.br.requirementhub.repository.RequirementRepository;
import com.br.requirementhub.repository.StakeHolderRepository;
import com.br.requirementhub.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.Objects;

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

    public List<RequirementResponseDTO> getRequirementsByProjectRelated(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with id: " + id));

        List<Requirement> requirements = requirementRepository.findByProjectRelated(project);

        return requirements.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public RequirementResponseDTO createRequirement(RequirementRequestDTO requirementRequestDTO) {
        this.verifyAlreadyExistsRequirement(requirementRequestDTO);

        Requirement requirement = convertToEntity(requirementRequestDTO);

        requirement.setAuthor(getAuthor(requirementRequestDTO.getAuthor()));
        requirement.setResponsible(getResponsible(requirementRequestDTO.getResponsible()));
        requirement.setStakeholders(getStakeholders(requirementRequestDTO.getStakeholders()));

        // Inicializar o Set de dependências se for nulo ou vazio
        requirement.setDependencies(getRequirementsRelated(requirementRequestDTO.getDependencies()));

        // Gerar identificador único antes de salvar
        generateRequirementIdentifier(requirement);

        requirement = requirementRepository.save(requirement);

        return convertToResponseDTO(requirement);
    }

    private void generateRequirementIdentifier(Requirement requirement) {
        List<Requirement> existingRequirements = requirementRepository.findByProjectRelated(requirement.getProjectRelated());

        // Encontrar o primeiro número vago baseado no tipo de identificador (RF ou RNF)
        int newIdNumber = findFirstAvailableIdentifierNumber(existingRequirements, requirement.getType());

        String identifierPrefix = transformRequirementIdentifier(requirement.getType());
        requirement.setIdentifier(identifierPrefix + "-" + String.format("%04d", newIdNumber));
        requirement.setVersion(1.0);
        requirement.setStatus(Status.CREATED.toString());
    }

    private int findFirstAvailableIdentifierNumber(List<Requirement> requirements, String type) {
        String identifierPrefix = transformRequirementIdentifier(type);
        Pattern pattern = Pattern.compile(identifierPrefix + "-(\\d+)$");
        List<Integer> usedNumbers = requirements.stream()
                .map(Requirement::getIdentifier)
                .map(identifier -> {
                    Matcher matcher = pattern.matcher(identifier);
                    return matcher.find() ? Integer.parseInt(matcher.group(1)) : null;
                })
                .filter(Objects::nonNull)
                .sorted()
                .collect(Collectors.toList());

        for (int i = 1; i <= usedNumbers.size(); i++) {
            if (!usedNumbers.contains(i)) {
                return i;
            }
        }
        return usedNumbers.size() + 1;
    }

    private String transformRequirementIdentifier(String type) {
        if (Objects.equals(type, "Funcional")) {
            return "RF";
        } else if (Objects.equals(type, "Não Funcional")) {
            return "RNF";
        }
        return type;
    }

    private void verifyAlreadyExistsRequirement(RequirementRequestDTO request) {
        Optional<Requirement> findProject = requirementRepository
                .findByProjectRelatedAndName(request.getProjectRelated(), request.getName());

        if (findProject.isPresent()) {
            throw new RequirementAlreadyExistException("This requirement already exists!");
        }
    }

    private Set<User> getResponsible(Set<User> users) {
        Set<User> managedUsers = new HashSet<>();
        for (User user : users) {
            User managedUser = userRepository.findById(user.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Responsible not found: " + user.getId()));
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
        if (dependencies == null || dependencies.isEmpty()) {
            return new HashSet<>(); // Retorna um Set vazio se não houver dependências
        }
        Set<Requirement> managedRequirements = new HashSet<>();
        for (Requirement dependency : dependencies) {
            Requirement managedRequirement = requirementRepository.findById(dependency.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Requirement not found: " + dependency.getId()));
            managedRequirements.add(managedRequirement);
        }
        return managedRequirements;
    }

    private Long getAuthor(Long name) {
        Optional<User> authorId = Optional.ofNullable(userRepository.findById(name)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + name)));
        return authorId.map(User::getId).orElse(null);
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
        dto.setAuthor(String.valueOf(requirement.getAuthor()));
        dto.setRisk(requirement.getRisk());
        dto.setPriority(requirement.getPriority());
        dto.setType(requirement.getType());
        dto.setStatus(requirement.getStatus());
        dto.setEffort(requirement.getEffort());
        dto.setProjectId(requirement.getProjectRelated().getId());
        dto.setDateCreated(requirement.getDateCreated().toString());
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
                .orElseThrow(
                        () -> new ProjectNotFoundException("Project not found with id: " + dto.getProjectRelated()));
        requirement.setProjectRelated(project);
        return requirement;
    }
}
