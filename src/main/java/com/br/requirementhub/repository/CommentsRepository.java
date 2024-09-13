package com.br.requirementhub.repository;

import com.br.requirementhub.entity.Comments;
import com.br.requirementhub.entity.Project;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentsRepository extends JpaRepository<Comments, Long> {
    List<Comments> findByRequirementId(Long requirementId);

    @Query(value = "SELECT un.user_id FROM user_notifications un WHERE un.requirement_id = :requirementId", nativeQuery = true)
    List<Long> findUserIdsByRequirementId(@Param("requirementId") Long requirementId);
}

