package com.br.requirementhub.repository;

import com.br.requirementhub.entity.Project;
import com.br.requirementhub.entity.ProjectArtifact;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectArtifactRepository extends JpaRepository<ProjectArtifact, Long> {

    @Query("SELECT pa FROM ProjectArtifact pa WHERE pa.projectId.id = :projectId")
    List<ProjectArtifact> findByProjectId(@Param("projectId") Long projectId);

    @Modifying
    @Query("DELETE FROM ProjectArtifact pa WHERE pa.projectId.id = :projectId")
    void deleteByProjectId(@Param("projectId") Long projectId);

    Optional<ProjectArtifact> findByFileNameAndProjectId(String fileName, Project projectId);
}

