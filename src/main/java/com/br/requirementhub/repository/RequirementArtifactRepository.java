package com.br.requirementhub.repository;

import com.br.requirementhub.entity.Requirement;
import com.br.requirementhub.entity.RequirementArtifact;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequirementArtifactRepository extends JpaRepository<RequirementArtifact, Long> {

    Optional<RequirementArtifact> findByNameAndRequirementId(String name, Requirement requirementId);
}