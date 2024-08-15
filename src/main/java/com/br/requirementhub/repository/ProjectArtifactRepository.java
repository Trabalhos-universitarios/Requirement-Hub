package com.br.requirementhub.repository;

import com.br.requirementhub.entity.ProjectArtifact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectArtifactRepository extends JpaRepository<ProjectArtifact, Long> {
}

