package com.br.requirementhub.repository;

import com.br.requirementhub.entity.RequirementHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequirementHistoryRepository extends JpaRepository<RequirementHistory, Long> {
}