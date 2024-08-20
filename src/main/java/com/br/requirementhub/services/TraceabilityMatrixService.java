package com.br.requirementhub.services;

import com.br.requirementhub.entity.Requirement;
import com.br.requirementhub.repository.RequirementRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TraceabilityMatrixService {

    private final RequirementRepository requirementRepository;

    private List<List<Object>> matrizDeRastreabilidadeArray;

    public void initializeMatrix() {
        List<Requirement> requirements = requirementRepository.findAll();
        List<String> identifiers = requirements.stream()
                .map(Requirement::getIdentifier)
                .collect(Collectors.toList());

        matrizDeRastreabilidadeArray = new ArrayList<>();

        // Cabeçalho
        List<Object> header = new ArrayList<>();
        header.add(null);
        header.addAll(identifiers);
        matrizDeRastreabilidadeArray.add(header);

        // Linhas
        for (String identifier : identifiers) {
            List<Object> row = new ArrayList<>(Collections.nCopies(identifiers.size() + 1, null));
            row.set(0, identifier);
            matrizDeRastreabilidadeArray.add(row);
        }
    }

    public void markAllDependencies() {
        if (matrizDeRastreabilidadeArray == null) {
            throw new IllegalStateException("Matrix not initialized. Call initializeMatrix() first.");
        }
        List<Requirement> requirements = requirementRepository.findAll();
        for (Requirement requirement : requirements) {
            markDependencies(requirement.getId());
        }
    }

    public void markDependencies(Long requirementId) {
        Set<Long> dependencyIds = requirementRepository.findDependencyIdsByRequirementId(requirementId);
        for (int i = 1; i < matrizDeRastreabilidadeArray.size(); i++) {
            List<Object> row = matrizDeRastreabilidadeArray.get(i);
            if (row.get(0) != null && getRequirementIdByIdentifier((String) row.get(0)).equals(requirementId)) {
                for (int j = 1; j < row.size(); j++) {
                    if (matrizDeRastreabilidadeArray.get(0).get(j) != null && dependencyIds.contains(getRequirementIdByIdentifier((String) matrizDeRastreabilidadeArray.get(0).get(j)))) {
                        row.set(j, "X");
                    }
                }
            }
        }
    }

    private Long getRequirementIdByIdentifier(String identifier) {
        return requirementRepository.findByIdentifier(identifier)
                .orElseThrow(() -> new EntityNotFoundException("Requirement not found with identifier: " + identifier))
                .getId();
    }

    public List<List<Object>> getMatrizDeRastreabilidadeArray() {
        return matrizDeRastreabilidadeArray;
    }

    public String toJson() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(getMatrizDeRastreabilidadeArray());
    }
}