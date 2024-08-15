package com.br.requirementhub.repository;

import com.br.requirementhub.entity.ProjectArtifact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectArtifactRepository extends JpaRepository<ProjectArtifact, Long> {

    // Método para buscar artefatos por projectId
    @Query("SELECT pa FROM ProjectArtifact pa WHERE pa.project.id = :projectId")
    List<ProjectArtifact> findByProjectId(@Param("projectId") Long projectId);

    // Método para deletar artefatos por projectId
    @Modifying
    @Query("DELETE FROM ProjectArtifact pa WHERE pa.project.id = :projectId")
    void deleteByProjectId(@Param("projectId") Long projectId);
}

