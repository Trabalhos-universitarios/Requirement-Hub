package com.br.requirementhub.services;

import com.br.requirementhub.dtos.requirementArtifact.RequirementArtifactRequestDTO;
import com.br.requirementhub.dtos.requirementArtifact.RequirementArtifactResponseDTO;
import com.br.requirementhub.entity.Requirement;
import com.br.requirementhub.entity.RequirementArtifact;
import com.br.requirementhub.exceptions.RequirementArtifactNotFoundException;
import com.br.requirementhub.exceptions.RequirementAlreadyExistException;
import com.br.requirementhub.repository.RequirementArtifactRepository;
import com.br.requirementhub.repository.RequirementRepository;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
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

    public List<RequirementArtifactResponseDTO> findArtifactsByRequirementId(Long requirementId) {
        return repository.findByRequirementId(requirementId).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public RequirementArtifactResponseDTO save(RequirementArtifactRequestDTO requestDTO) throws IOException {
        verifyAlreadyExistsArtifact(requestDTO);
        RequirementArtifact artifact = convertToEntity(requestDTO);
        generateArtifactIdentifier(artifact);
        artifact = repository.save(artifact);
        return convertToResponseDTO(artifact);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    private void verifyAlreadyExistsArtifact(RequirementArtifactRequestDTO requestDTO) throws IOException {
        final Requirement requirement = convertToEntity(requestDTO).getRequirementId();
        Optional<RequirementArtifact> findArtifact = repository.findByNameAndRequirementIdAndType(requestDTO.getName(), requirement, requestDTO.getType().getName());

        if (findArtifact.isPresent()) {
            throw new RequirementAlreadyExistException("This artifact requirement already exists!");
        }
    }

    private void generateArtifactIdentifier(RequirementArtifact requirementArtifact) throws IOException {
        List<RequirementArtifact> existingArtifact = repository.findAll();
        int newIdNumber = findFirstAvailableIdentifierNumber(existingArtifact, requirementArtifact.getIdentifier());
        requirementArtifact.setIdentifier(requirementArtifact.getIdentifier() + "-" + String.format("%04d", newIdNumber));
    }

    private int findFirstAvailableIdentifierNumber(List<RequirementArtifact> requirements, String identifier) {
        Pattern pattern = Pattern.compile(identifier + "-(\\d+)$");
        List<Integer> usedNumbers = requirements.stream()
                .map(RequirementArtifact::getIdentifier)
                .map(ident -> {
                    Matcher matcher = pattern.matcher(ident);
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

    public RequirementArtifactResponseDTO update(Long id, RequirementArtifactRequestDTO requestDTO) throws IOException {
        Optional<RequirementArtifact> existingArtifactOpt = repository.findById(id);
        if (existingArtifactOpt.isPresent()) {
            RequirementArtifact existingArtifact = existingArtifactOpt.get();
            existingArtifact.setName(requestDTO.getName());
            existingArtifact.setDescription(requestDTO.getDescription());
            existingArtifact.setFile(requestDTO.getFile());

            RequirementArtifact updatedArtifact = repository.save(existingArtifact);

            return convertToDTO(updatedArtifact);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found with id: " + id);
        }
    }

    private RequirementArtifact convertToEntity(RequirementArtifactRequestDTO dto) throws IOException {
        RequirementArtifact artifact = new RequirementArtifact();
        artifact.setName(dto.getName());
        artifact.setType(dto.getType().getName());
        artifact.setIdentifier(dto.getType().getIdentifier());
        artifact.setDescription(dto.getDescription());
        artifact.setFile(dto.getFile());
        Requirement requirement = requirementRepository.findById(dto.getRequirementId())
                .orElseThrow(() -> new RequirementAlreadyExistException("Requirement not found with id: " + dto.getRequirementId()));
        artifact.setRequirementId(requirement);
        return artifact;
    }

    public RequirementArtifactResponseDTO convertToResponseDTO(RequirementArtifact artifact) {
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

    private RequirementArtifactResponseDTO convertToDTO(RequirementArtifact artifact) {
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
}