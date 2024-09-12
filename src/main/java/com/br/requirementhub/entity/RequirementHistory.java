package com.br.requirementhub.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "requirement_history")
public class RequirementHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String identifier;

    private String name;

    private String description;

    private String version;

    private Long author;

    private String risk;

    private String priority;

    private String type;

    private String status;

    private Integer effort;

    private Long requirementId;

    private Long projectId;

    @CreationTimestamp
    private LocalDateTime modification_date;
}
