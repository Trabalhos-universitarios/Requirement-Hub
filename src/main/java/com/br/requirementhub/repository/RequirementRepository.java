package com.br.requirementhub.repository;

import com.br.requirementhub.entity.Project;
import com.br.requirementhub.entity.Requirement;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequirementRepository extends JpaRepository<Requirement, Long> {

    Optional<Requirement> findByProjectRelatedAndName(Project projectRelated, String name);

    List<Requirement> findByProjectRelated(Project projectRelated);
}