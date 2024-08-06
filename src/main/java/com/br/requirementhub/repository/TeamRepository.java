package com.br.requirementhub.repository;

import com.br.requirementhub.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

    @Query("SELECT t FROM Team t WHERE t.project.id = :projectId AND t.user.role = :role")
    List<Team> findByProjectIdAndUserRole(@Param("projectId") Long projectId, @Param("role") com.br.requirementhub.enums.Role role);
}