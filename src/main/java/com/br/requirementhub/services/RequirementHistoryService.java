package com.br.requirementhub.services;

import com.br.requirementhub.entity.RequirementHistory;
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
}