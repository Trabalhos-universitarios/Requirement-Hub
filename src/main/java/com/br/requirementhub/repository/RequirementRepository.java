package com.br.requirementhub.repository;

import com.br.requirementhub.entity.Project;
import com.br.requirementhub.entity.Requirement;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface RequirementRepository extends JpaRepository<Requirement, Long> {

    Optional<Requirement> findByProjectRelatedAndName(Project projectRelated, String name);

    List<Requirement> findByProjectRelated(Project projectRelated);

    @Query(value = "SELECT requirement_id AS requirementId, dependency_id AS dependencyId FROM requirement_dependency WHERE requirement_id = :requirementId OR dependency_id = :requirementId", nativeQuery = true)
    Set<Long> findDependencyIdsByRequirementId(Long requirementId);

    @Query("SELECT r FROM Requirement r WHERE r.identifier = :identifier AND r.projectRelated.id = :projectId")
    List<Requirement> findByIdentifierAndProjectId(@Param("identifier") String identifier, @Param("projectId") Long projectId);

    @Query("SELECT r FROM Requirement r WHERE r.projectRelated.id = :projectId")
    List<Requirement> findByProjectRelated_Id(@Param("projectId") Long projectId);

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

    @Query(value = "SELECT rr.requirement_id, rr.user_id, u.name FROM requirement_responsible rr JOIN _user u ON rr.user_id = u.id", nativeQuery = true)
    List<Object[]> findAllRequirementResponsibles();
}