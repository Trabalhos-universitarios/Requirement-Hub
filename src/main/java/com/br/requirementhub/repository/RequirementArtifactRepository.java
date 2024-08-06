package com.br.requirementhub.repository;

import com.br.requirementhub.entity.RequirementArtifact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequirementArtifactRepository extends JpaRepository<RequirementArtifact, Long> {
}