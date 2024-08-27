package com.br.requirementhub.services;

import com.br.requirementhub.entity.Requirement;
import com.br.requirementhub.entity.RequirementArtifact;
import com.br.requirementhub.repository.RequirementArtifactRepository;
import com.br.requirementhub.repository.RequirementRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TraceabilityMatrixService {

    private static final Logger logger = LoggerFactory.getLogger(TraceabilityMatrixService.class);

    private final RequirementRepository requirementRepository;
    private final RequirementArtifactRepository requirementArtifactRepository;

    private List<List<Object>> matrizDeRastreabilidadeArray;

    public void initializeMatrix(Long projectId) {
        List<Requirement> requirements = requirementRepository.findByProjectRelated_Id(projectId);
        List<String> identifiers = requirements.stream()
                .map(Requirement::getIdentifier)
                .sorted()
                .collect(Collectors.toList());

        matrizDeRastreabilidadeArray = new ArrayList<>();

        List<Object> header = new ArrayList<>();
        header.add(null);
        header.addAll(identifiers);
        matrizDeRastreabilidadeArray.add(header);

        for (Requirement requirement : requirements) {
            List<Object> row = new ArrayList<>(Collections.nCopies(identifiers.size() + 1, null));
            row.set(0, requirement.getIdentifier());
            matrizDeRastreabilidadeArray.add(row);

            List<RequirementArtifact> artifacts = requirementArtifactRepository.findByRequirementId(requirement.getId());
            for (RequirementArtifact artifact : artifacts) {
                List<Object> artifactRow = new ArrayList<>(Collections.nCopies(identifiers.size() + 1, null));
                artifactRow.set(0, artifact.getIdentifier());
                matrizDeRastreabilidadeArray.add(artifactRow);
            }
        }

        matrizDeRastreabilidadeArray = matrizDeRastreabilidadeArray.stream()
                .sorted(Comparator.comparing(row -> (String) row.get(0), Comparator.nullsFirst(Comparator.naturalOrder())))
                .collect(Collectors.toList());
    }

    public void markAllDependencies(Long projectId) {
        if (matrizDeRastreabilidadeArray == null) {
            throw new IllegalStateException("Matrix not initialized. Call initializeMatrix() first.");
        }
        List<Requirement> requirements = requirementRepository.findByProjectRelated_Id(projectId);
        for (Requirement requirement : requirements) {
            markDependencies(requirement.getId(), requirement.getProjectRelated().getId());
        }
    }

    public void markDependencies(Long requirementId, Long projectId) {
        Set<Long> dependencyIds = requirementRepository.findDependencyIdsByRequirementId(requirementId);
        for (int i = 1; i < matrizDeRastreabilidadeArray.size(); i++) {
            List<Object> row = matrizDeRastreabilidadeArray.get(i);
            if (row.get(0) != null && getRequirementIdByIdentifier((String) row.get(0), projectId).equals(requirementId)) {
                for (int j = 1; j < row.size(); j++) {
                    if (matrizDeRastreabilidadeArray.get(0).get(j) != null && dependencyIds.contains(getRequirementIdByIdentifier((String) matrizDeRastreabilidadeArray.get(0).get(j), projectId))) {
                        row.set(j, "X");
                    }
                }
            }
        }

        List<RequirementArtifact> artifacts = requirementArtifactRepository.findByRequirementId(requirementId);
        for (RequirementArtifact artifact : artifacts) {
            for (int i = 1; i < matrizDeRastreabilidadeArray.size(); i++) {
                List<Object> row = matrizDeRastreabilidadeArray.get(i);
                if (row.get(0) != null && row.get(0).equals(artifact.getIdentifier())) {
                    for (int j = 1; j < row.size(); j++) {
                        if (matrizDeRastreabilidadeArray.get(0).get(j) != null && getRequirementIdByIdentifier((String) matrizDeRastreabilidadeArray.get(0).get(j), projectId).equals(requirementId)) {
                            row.set(j, "X");
                        }
                    }
                }
            }
        }
    }

    private Long getRequirementIdByIdentifier(String identifier, Long projectId) {
        List<Requirement> requirements = requirementRepository.findByIdentifierAndProjectId(identifier, projectId);
        if (requirements.isEmpty()) {
            List<RequirementArtifact> artifacts = requirementArtifactRepository.findByIdentifier(identifier);
            logger.debug("Found {} artifacts with identifier '{}'", artifacts.size(), identifier);
            if (artifacts.isEmpty()) {
                throw new EntityNotFoundException("No requirements or artifacts found with identifier '" + identifier + "' and projectId '" + projectId + "'");
            }
            if (artifacts.size() != 1) {
                throw new IncorrectResultSizeDataAccessException(1, artifacts.size());
            }
            return artifacts.get(0).getRequirementId().getId();
        }
        logger.debug("Found {} requirements with identifier '{}' and projectId '{}'", requirements.size(), identifier, projectId);
        if (requirements.size() != 1) {
            throw new IncorrectResultSizeDataAccessException(1, requirements.size());
        }
        return requirements.get(0).getId();
    }

    public List<List<Object>> getMatrizDeRastreabilidadeArray() {
        return matrizDeRastreabilidadeArray;
    }

    public String toJson() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(getMatrizDeRastreabilidadeArray());
    }
}