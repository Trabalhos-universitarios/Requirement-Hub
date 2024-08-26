package com.br.requirementhub.services;

import com.br.requirementhub.entity.Requirement;
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

        for (String identifier : identifiers) {
            List<Object> row = new ArrayList<>(Collections.nCopies(identifiers.size() + 1, null));
            row.set(0, identifier);
            matrizDeRastreabilidadeArray.add(row);
        }
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
    }

    private Long getRequirementIdByIdentifier(String identifier, Long projectId) {
        List<Requirement> requirements = requirementRepository.findByIdentifierAndProjectId(identifier, projectId);
        logger.debug("Found {} requirements with identifier '{}' and projectId '{}'", requirements.size(), identifier, projectId);
        if (requirements.isEmpty()) {
            throw new EntityNotFoundException("No requirements found with identifier '" + identifier + "' and projectId '" + projectId + "'");
        }
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