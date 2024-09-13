package com.br.requirementhub.repository;

import com.br.requirementhub.entity.RequirementHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequirementHistoryRepository extends JpaRepository<RequirementHistory, Long> {
    List<RequirementHistory> findByProjectId(Long projectId);
    List<RequirementHistory> findByIdentifierAndProjectId(String identifier, Long projectId);
}