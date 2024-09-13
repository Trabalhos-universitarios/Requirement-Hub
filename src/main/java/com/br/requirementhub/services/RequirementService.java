package com.br.requirementhub.services;

import com.br.requirementhub.dtos.requirement.RequirementRequestDTO;
import com.br.requirementhub.dtos.requirement.RequirementResponseDTO;
import com.br.requirementhub.dtos.requirement.RequirementUpdateRequestDTO;
import com.br.requirementhub.dtos.requirementArtifact.RequirementArtifactResponseDTO;
import com.br.requirementhub.entity.*;
import com.br.requirementhub.enums.Status;
import com.br.requirementhub.exceptions.ProjectNotFoundException;
import com.br.requirementhub.exceptions.RequirementAlreadyExistException;
import com.br.requirementhub.exceptions.RequirementNotFoundException;
import com.br.requirementhub.exceptions.UserNotFoundException;
import com.br.requirementhub.repository.ProjectRepository;
import com.br.requirementhub.repository.RequirementArtifactRepository;
import com.br.requirementhub.repository.RequirementRepository;
import com.br.requirementhub.repository.StakeHolderRepository;
import com.br.requirementhub.repository.UserRepository;
import com.br.requirementhub.repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@Transactional
@RequiredArgsConstructor
public class RequirementService {

    private final RequirementRepository requirementRepository;

    private final ProjectRepository projectRepository;

    private final UserRepository userRepository;

    private final CommentsRepository commentsRepository;

    private final StakeHolderRepository stakeholderRepository;

    private final RequirementArtifactRepository requirementArtifactRepository;

    private final RequirementArtifactService requirementArtifactService;

    private final RequirementHistoryRepository requirementHistoryRepository;

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

    public List<RequirementResponseDTO> getRequirementDataToUpdate(Long id) {
        Optional<Requirement> requirements = Optional.ofNullable(requirementRepository.findById(id)
                .orElseThrow(() -> new RequirementNotFoundException("Requirement not found with id: " + id)));

        return requirements.stream()
                .map(this::convertToRelationshipDTO)
                .collect(Collectors.toList());
    }

    public List<RequirementResponseDTO> listByProjectId(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with id: " + id));

        List<Requirement> requirements = requirementRepository.findByProjectRelated(project);

        return requirements.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<RequirementArtifactResponseDTO> getArtifactsByProjectId(Long projectId) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with id: " + projectId));

        List<Requirement> requirements = requirementRepository.findByProjectRelated(project);

        List<RequirementArtifactResponseDTO> artifactResponseDTOs = new ArrayList<>();

        for (Requirement requirement : requirements) {
            List<RequirementArtifact> artifacts =
                    requirementArtifactRepository.findByRequirementId(requirement.getId());

            for (RequirementArtifact artifact : artifacts) {
                RequirementArtifactResponseDTO dto = requirementArtifactService.convertToResponseDTO(artifact);
                artifactResponseDTOs.add(dto);
            }
        }
        return artifactResponseDTOs;
    }

    public RequirementResponseDTO createRequirement(RequirementRequestDTO requirementRequestDTO) {
        this.verifyAlreadyExistsRequirement(requirementRequestDTO, "create");

        Requirement requirement = convertToEntity(requirementRequestDTO);

        requirement.setAuthor(getAuthor(requirementRequestDTO.getAuthor()));
        requirement.setResponsible(getResponsible(requirementRequestDTO.getResponsible()));
        requirement.setStakeholders(getStakeholders(requirementRequestDTO.getStakeholders()));

        requirement.setDependencies(getRequirementsRelated(requirementRequestDTO.getDependencies()));

        generateRequirementIdentifier(requirement);
        createVersion(requirement);
        createStatus(requirement);

        requirement = requirementRepository.save(requirement);

        return convertToResponseDTO(requirement);
    }

    private static void createVersion(Requirement requirementAfter) {
        requirementAfter.setVersion("1.0");
    }

    private static void createStatus(Requirement requirementAfter) {
        requirementAfter.setStatus(Status.CREATED.toString());
    }

    public RequirementResponseDTO sendToApprovalFlowRequirementId(Long id) {
        Requirement requirement = requirementRepository.findById(id)
                .orElseThrow(() -> new RequirementNotFoundException("Requirement not found: " + id));

        requirement.setStatus(Status.PENDING.toString());
        requirement = requirementRepository.save(requirement);

        this.createNotificationToManager(requirement);

        return convertToResponseDTO(requirement);
    }

    public RequirementResponseDTO sendToApprovalFlow(RequirementRequestDTO requirementRequestDTO) {
        this.verifyAlreadyExistsRequirement(requirementRequestDTO, "flow");

        Requirement requirement = convertToEntity(requirementRequestDTO);

        requirement.setAuthor(getAuthor(requirementRequestDTO.getAuthor()));
        requirement.setResponsible(getResponsible(requirementRequestDTO.getResponsible()));
        requirement.setStakeholders(getStakeholders(requirementRequestDTO.getStakeholders()));

        requirement.setDependencies(getRequirementsRelated(requirementRequestDTO.getDependencies()));

        generateRequirementIdentifier(requirement);
        createVersion(requirement);
        requirement.setStatus(Status.PENDING.toString());

        requirement = requirementRepository.save(requirement);
        this.createNotificationToManager(requirement);

        return convertToResponseDTO(requirement);
    }

    private void generateRequirementIdentifier(Requirement requirement) {
        List<Requirement> existingRequirements =
                requirementRepository.findByProjectRelated(requirement.getProjectRelated());

        int newIdNumber = findFirstAvailableIdentifierNumber(existingRequirements, requirement.getType());

        String identifierPrefix = transformRequirementIdentifier(requirement.getType());
        requirement.setIdentifier(identifierPrefix + "-" + String.format("%04d", newIdNumber));
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
                .toList();

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

    private void verifyAlreadyExistsRequirement(RequirementRequestDTO request, String action) {
        Optional<Requirement> findProject = requirementRepository
                .findByProjectRelatedAndName(request.getProjectRelated(), request.getName());

        if (findProject.isPresent() && action.equals("create")) {
            throw new RequirementAlreadyExistException("This requirement already exists!");
        } else if (findProject.isEmpty() && action.equals("update")) {
            throw new RequirementNotFoundException("Requirement not found with id: " + request.getId());
        } else if (findProject.isPresent() && action.equals("flow")) {
            throw new RequirementNotFoundException("This requirement already exists!");
        }
    }

    private List<User> getResponsible(Set<User> users) {
        List<User> managedUsers = new ArrayList<>();
        for (User user : users) {
            User managedUser = userRepository.findById(user.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Responsible not found: " + user.getId()));
            managedUsers.add(managedUser);
        }
        return managedUsers;
    }

    private List<Stakeholder> getStakeholders(Set<Stakeholder> stakeholders) {
        List<Stakeholder> managedStakeholders = new ArrayList<>();
        for (Stakeholder stakeholder : stakeholders) {
            Stakeholder managedStakeholder = stakeholderRepository.findById(stakeholder.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Stakeholder not found: " + stakeholder.getId()));
            managedStakeholders.add(managedStakeholder);
        }
        return managedStakeholders;
    }

    private List<Requirement> getRequirementsRelated(Set<Requirement> dependencies) {
        if (dependencies == null || dependencies.isEmpty()) {
            return new ArrayList<>(); // Retorna um Set vazio se não houver dependências
        }
        List<Requirement> managedRequirements = new ArrayList<>();
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

    public List<RequirementResponseDTO> getRequirementsByIds(List<Long> ids) {
        List<Requirement> requirement = requirementRepository.findAllById(ids);

        return requirement.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public void deleteRequirement(Long id) {
        Requirement requirement = requirementRepository.findById(id)
                .orElseThrow(() -> new RequirementNotFoundException("Requirement not found: " + id));

        requirementRepository.deleteDependenciesByRequirementId(id);
        requirementRepository.deleteResponsibleByRequirementId(id);
        requirementRepository.deleteStakeholderByRequirementId(id);

        List<RequirementArtifact> requirementArtifact = requirementArtifactRepository.findByRequirementId(requirement);
        deleteArtifactRelated(requirementArtifact);

        requirementRepository.deleteById(id);
    }

    private void deleteArtifactRelated(List<RequirementArtifact> requirement) {
        for (RequirementArtifact artifact : requirement) {
            requirementArtifactRepository.deleteById(artifact.getId());
        }
    }

    public RequirementResponseDTO updateRequirement(Long id, RequirementUpdateRequestDTO requirementRequestDTO) {

        Requirement requirementBefore = requirementRepository.findById(id)
                .orElseThrow(() -> new RequirementNotFoundException("Requirement not found: " + id));

        if (requirementBefore != null) {
            Requirement requirementAfter = convertToEntityToUpdate(requirementRequestDTO);
            requirementAfter.setId(id);
            requirementAfter.setAuthor(requirementBefore.getAuthor());
            requirementAfter.setResponsible(getResponsible(requirementRequestDTO.getResponsible()));
            requirementAfter.setStakeholders(getStakeholders(requirementRequestDTO.getStakeholders()));
            requirementAfter.setDependencies(getRequirementsRelated(requirementRequestDTO.getDependencies()));
            requirementAfter.setIdentifier(requirementBefore.getIdentifier());

            this.updateVersion(requirementBefore, requirementAfter);
            this.updateStatus(requirementBefore, requirementAfter, "update");


            if (!requirementBefore.getStatus().equals(Status.CREATED.toString())) {
                RequirementHistory history = convertToHistory(requirementBefore);
                requirementHistoryRepository.save(history);
            }

            requirementAfter = requirementRepository.save(requirementAfter);
            return convertToResponseDTO(requirementAfter);
        } else {
            throw new ProjectNotFoundException("Project not found with id: " + id);
        }
    }

    private void updateVersion(Requirement requirementBefore, Requirement requirementAfter) {

        if (requirementBefore.getStatus().equals(Status.CREATED.toString())) {
            requirementAfter.setVersion("1.0");
            return;
        }

        incrementVersion(requirementBefore, requirementAfter);
    }

    private void incrementVersion(Requirement requirementBefore, Requirement requirementAfter) {

        if (requirementBefore.getStatus() == null) {
            createVersion(requirementAfter);
            return;
        }

        if (requirementBefore.getStatus().equals(Status.PENDING.toString()) || requirementBefore.getStatus().equals(Status.CREATED.toString())) {
            incrementVersionMinor(requirementBefore, requirementAfter);
        } else if (requirementBefore.getStatus().equals(Status.ACTIVE.toString())) {
            incrementVersionMajor(requirementBefore, requirementAfter);
        }  else if (requirementBefore.getStatus().equals(Status.REJECTED.toString())) {
            requirementAfter.setVersion(requirementBefore.getVersion());
        }
    }

    private static void incrementVersionMajor(Requirement requirementBefore,
                                              Requirement requirementAfter) {
        String[] parts = requirementBefore.getVersion().split("\\.");
        int majorVersion = Integer.parseInt(parts[0]);
        majorVersion += 1;

        final String newVersion = majorVersion + ".0";

        requirementAfter.setVersion(newVersion);
    }

    private static void incrementVersionMinor(Requirement requirementBefore, Requirement requirementAfter) {
        String[] parts = requirementBefore.getVersion().split("\\.");
        int majorVersion = Integer.parseInt(parts[0]);
        int minorVersion = Integer.parseInt(parts[1]);

        minorVersion += 1;

        final String newVersion = majorVersion + "." + minorVersion;

        requirementAfter.setVersion(newVersion);
    }

    private void updateStatus(Requirement requirementBefore, Requirement requirementAfter, String action) {

        if (requirementBefore.getStatus() == null) {
            requirementAfter.setStatus(Status.CREATED.toString());
            return;
        }

        switch (action) {
            case "update":
                updateStatusWhenActionIsUpdate(requirementBefore, requirementAfter);
                break;
            case "reject":
                requirementAfter.setStatus(Status.REJECTED.toString());
                break;
            case "approve":
                requirementAfter.setStatus(Status.ACTIVE.toString());
                break;
            case "delete":
                requirementAfter.setStatus(Status.BLOCKED.toString());
                break;
            default:
                requirementAfter.setStatus(Status.CREATED.toString());
                break;
        }
    }

    private void updateStatusWhenActionIsUpdate(Requirement requirementBefore,
                                                Requirement requirementAfter) {
        if (requirementBefore.getStatus().equals(Status.PENDING.toString())) {
            requirementAfter.setStatus(Status.PENDING.toString());
            this.createNotificationToManager(requirementBefore);
        } else if (requirementBefore.getStatus().equals(Status.ACTIVE.toString())) {
            requirementAfter.setStatus(Status.PENDING.toString());
            this.createNotificationToManager(requirementBefore);
        } else if (requirementBefore.getStatus().equals(Status.REJECTED.toString())) {
            requirementAfter.setStatus(Status.PENDING.toString());
            this.createNotificationToManager(requirementBefore);
        } else if (requirementBefore.getStatus().equals(Status.CREATED.toString())) {
            requirementAfter.setStatus(Status.CREATED.toString());
        }
    }

    private void createNotificationToManager(Requirement requirementBefore) {
        Project projectRelated = this.projectRepository.getReferenceById(requirementBefore.getProjectRelated().getId());
        String managerProjectRelated = projectRelated.getManager();

        if (managerProjectRelated != null) {
            Optional<User> manager = Optional.ofNullable(this.userRepository.findByName(managerProjectRelated).orElseThrow(
                            () -> new UserNotFoundException("Manager not found: " + managerProjectRelated)));

            Long managerId = null;

            if (manager.isPresent()) {
                managerId = manager.get().getId();
            }

            System.out.println(managerId);

            List<Long> comments = commentsRepository.findUserIdsByRequirementId(requirementBefore.getId());

            if (managerId != null && comments.isEmpty()) {
                this.userRepository.addNotificationToUser(managerId, requirementBefore.getId());
            } else {
                System.out.println("Notificação existente para o gerente ID: " + managerId);
            }
        }
    }

    public List<Object[]> getAllRequirementResponsibles() {
        return requirementRepository.findAllRequirementResponsibles();
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
        return dto;
    }

    private RequirementResponseDTO convertToRelationshipDTO(Requirement requirement) {
        RequirementResponseDTO dto = new RequirementResponseDTO();

        List<Requirement> requirements = requirement.getDependencies();
        List<User> responsible = requirement.getResponsible();
        List<Stakeholder> stakeholders = requirement.getStakeholders();

        dto.setArtifactIds(requirement.getArtifacts().stream()
                .map(RequirementArtifact::getId)
                .collect(Collectors.toList()));
        dto.setResponsibleIds(responsible.stream()
                .map(User::getId)
                .collect(Collectors.toList()));
        dto.setDependencyIds(requirements.stream()
                .map(Requirement::getId)
                .collect(Collectors.toList()));
        dto.setStakeholderIds(stakeholders.stream()
                .map(Stakeholder::getId)
                .collect(Collectors.toList()));
        return dto;
    }

    private RequirementHistory convertToHistory(Requirement requirement) {
        RequirementHistory history = new RequirementHistory();
        history.setIdentifier(requirement.getIdentifier());
        history.setName(requirement.getName());
        history.setDescription(requirement.getDescription());
        history.setVersion(requirement.getVersion());
        history.setAuthor(requirement.getAuthor());
        history.setRisk(requirement.getRisk());
        history.setPriority(requirement.getPriority());
        history.setType(requirement.getType());
        history.setStatus(requirement.getStatus());
        history.setEffort(requirement.getEffort());
        history.setRequirementId(requirement.getId());
        history.setProjectId(requirement.getProjectRelated().getId());
        return history;
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

    private Requirement convertToEntityToUpdate(RequirementUpdateRequestDTO dto) {
        Requirement requirement = new Requirement();
        requirement.setName(dto.getName());
        requirement.setDescription(dto.getDescription());
        requirement.setRisk(dto.getRisk());
        requirement.setPriority(dto.getPriority());
        requirement.setType(dto.getType());
        requirement.setEffort(dto.getEffort());
        Project project = projectRepository.findById(dto.getProjectRelated())
                .orElseThrow(
                        () -> new ProjectNotFoundException("Project not found with id: " + dto.getProjectRelated()));
        requirement.setProjectRelated(project);
        return requirement;
    }
}
