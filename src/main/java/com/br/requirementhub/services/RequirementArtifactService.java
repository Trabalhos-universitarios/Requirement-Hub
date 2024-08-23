package com.br.requirementhub.services;

import com.br.requirementhub.dtos.requirementArtifact.RequirementArtifactRequestDTO;
import com.br.requirementhub.dtos.requirementArtifact.RequirementArtifactResponseDTO;
import com.br.requirementhub.entity.Requirement;
import com.br.requirementhub.entity.RequirementArtifact;
import com.br.requirementhub.exceptions.RequirementArtifactNotFoundException;
import com.br.requirementhub.exceptions.RequirementAlreadyExistException;
import com.br.requirementhub.repository.RequirementArtifactRepository;
import com.br.requirementhub.repository.RequirementRepository;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
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

    public RequirementArtifactResponseDTO findById(Long id) {
        Optional<RequirementArtifact> artifactOpt = repository.findById(id);
        if (artifactOpt.isPresent()) {
            return convertToResponseDTO(artifactOpt.get());
        } else {
            throw new RequirementArtifactNotFoundException("Artifact not found with id: " + id);
        }
    }

    public RequirementArtifactResponseDTO save(RequirementArtifactRequestDTO requestDTO) throws IOException {
        verifyAlreadyExistsArtifact(requestDTO);
        RequirementArtifact artifact = convertToEntity(requestDTO);
        generateArtifactIdentifier(artifact);
        artifact = repository.save(artifact);
        return convertToResponseDTO(artifact);
    }

    private void verifyAlreadyExistsArtifact(RequirementArtifactRequestDTO requestDTO) throws IOException {
        final Requirement requirement = convertToEntity(requestDTO).getRequirementId();
        Optional<RequirementArtifact> findProject = repository.findByNameAndRequirementId(requestDTO.getName(), requirement);
        if (findProject.isPresent()) {
            throw new RequirementAlreadyExistException("This artifact requirement already exists!");
        }
    }

    private void generateArtifactIdentifier(RequirementArtifact requirementArtifact) {
        List<RequirementArtifact> existingArtifact = repository.findAll();

        int newIdNumber = findFirstAvailableIdentifierNumber(existingArtifact, requirementArtifact.getType());

        requirementArtifact.setIdentifier(requirementArtifact.getType() + "-" + String.format("%04d", newIdNumber));
    }

    private int findFirstAvailableIdentifierNumber(List<RequirementArtifact> requirements, String type) {
        Pattern pattern = Pattern.compile(type + "-(\\d+)$");
        List<Integer> usedNumbers = requirements.stream()
                .map(RequirementArtifact::getIdentifier)
                .map(identifier -> {

                    System.out.println("IDENTIFIER: " + identifier);

                    Matcher matcher = pattern.matcher(identifier);

                    System.out.println("MATCHER: " + matcher);

                    System.out.println("MATCHER.FIND: " + matcher.find());

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

    private RequirementArtifactResponseDTO convertToResponseDTO(RequirementArtifact artifact) {
        return new RequirementArtifactResponseDTO(
                artifact.getId(),
                artifact.getIdentifier(),
                artifact.getName(),
                artifact.getType(),
                artifact.getDescription(),
                artifact.getFile(),
                artifact.getRequirementId().getId()
        );
    }

    private RequirementArtifact convertToEntity(RequirementArtifactRequestDTO dto) throws IOException {
        RequirementArtifact artifact = new RequirementArtifact();
        artifact.setName(dto.getName());
        artifact.setType(dto.getType());
        artifact.setDescription(dto.getDescription());
        artifact.setFile(dto.getFile());
        Requirement requirement = requirementRepository.findById(dto.getRequirementId())
                .orElseThrow(() -> new RequirementAlreadyExistException("Requirement not found with id: " + dto.getRequirementId()));
        artifact.setRequirementId(requirement);
        return artifact;
    }
}