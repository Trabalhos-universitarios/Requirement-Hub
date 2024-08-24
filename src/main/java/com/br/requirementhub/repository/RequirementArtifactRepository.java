package com.br.requirementhub.repository;

import com.br.requirementhub.entity.Requirement;
import com.br.requirementhub.entity.RequirementArtifact;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RequirementArtifactRepository extends JpaRepository<RequirementArtifact, Long> {

    @Query("SELECT ra FROM RequirementArtifact ra WHERE ra.requirementId.id = :requirementId")
    List<RequirementArtifact> findByRequirementId(@Param("requirementId") Long requirementId);

    Optional<RequirementArtifact> findByNameAndRequirementId(String name, Requirement requirementId);
}