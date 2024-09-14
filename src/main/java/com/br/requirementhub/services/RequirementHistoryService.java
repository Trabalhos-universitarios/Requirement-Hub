package com.br.requirementhub.services;

import com.br.requirementhub.entity.Requirement;
import com.br.requirementhub.entity.RequirementArtifact;
import com.br.requirementhub.entity.RequirementHistory;
import com.br.requirementhub.exceptions.RequirementNotFoundException;
import com.br.requirementhub.repository.RequirementHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RequirementHistoryService {


    private final RequirementHistoryRepository requirementHistoryRepository;

    public List<RequirementHistory> getHistoryByProject(Long projectId) {
        return requirementHistoryRepository.findByProjectId(projectId);
    }

    public List<RequirementHistory> getHistoryByIdentifierAndProject(String identifier, Long projectId) {
        return requirementHistoryRepository.findByIdentifierAndProjectId(identifier, projectId);
    }

    public void deleteHistory(Long id) {
        RequirementHistory history = requirementHistoryRepository.findById(id)
                .orElseThrow(() -> new RequirementNotFoundException("Requirement not found: " + id));
        requirementHistoryRepository.deleteById(id);
    }
}