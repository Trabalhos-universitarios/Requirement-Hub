package com.br.requirementhub.repository;

import com.br.requirementhub.entity.Project;
import com.br.requirementhub.entity.Requirement;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface RequirementRepository extends JpaRepository<Requirement, Long> {

    Optional<Requirement> findByProjectRelatedAndName(Project projectRelated, String name);

    List<Requirement> findByProjectRelated(Project projectRelated);

    Optional<Requirement> findByIdentifier(String identifier);

    @Query("SELECT r.id.dependencyId FROM RequirementDependency r WHERE r.id.requirementId = :requirementId")
    Set<Long> findDependencyIdsByRequirementId(Long requirementId);

    List<Requirement> findByDependencies(Requirement requirement);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM requirement_dependency WHERE requirement_id = :requirementId OR dependency_id = :requirementId", nativeQuery = true)
    void deleteDependenciesByRequirementId(Long requirementId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM requirement_responsible WHERE requirement_id = :requirementId", nativeQuery = true)
    void deleteResponsibleByRequirementId(Long requirementId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM requirement_stakeholder WHERE requirement_id = :requirementId", nativeQuery = true)
    void deleteStakeholderByRequirementId(Long requirementId);
}