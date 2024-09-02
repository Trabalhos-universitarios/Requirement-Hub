package com.br.requirementhub.repository;

import com.br.requirementhub.entity.Project;
import com.br.requirementhub.entity.Requirement;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    Optional<Project> findByNameAndManager(String projectName, String managerName);
}
