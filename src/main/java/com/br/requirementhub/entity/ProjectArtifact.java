package com.br.requirementhub.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "project_artifact")
@Data
public class ProjectArtifact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String type;

    @Column(name = "artifact_file")
    private byte[] artifact_file;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;
}


