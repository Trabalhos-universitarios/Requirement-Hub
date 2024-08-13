package com.br.requirementhub.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "requirement_artifact")
public class RequirementArtifact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String identify;

    private String type;

    private String description;

    @Column(name = "artifact_file")
    private byte[] artifact_file;

    @ManyToOne
    @JoinColumn(name = "id_requisito", nullable = false)
    private Requirement requirement;
}