package com.br.requirementhub.repository;

import com.br.requirementhub.entity.Comments;
import com.br.requirementhub.entity.Project;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentsRepository extends JpaRepository<Comments, Long> {
    List<Comments> findByRequirementId(Long requirementId);
}

