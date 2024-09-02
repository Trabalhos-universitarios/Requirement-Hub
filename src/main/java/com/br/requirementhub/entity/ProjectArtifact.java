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

    private String fileName;

    private String type;

    @Column(name = "size")
    private Long size;

    @Column(name = "content")
    private byte[] content;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project projectId;
}


