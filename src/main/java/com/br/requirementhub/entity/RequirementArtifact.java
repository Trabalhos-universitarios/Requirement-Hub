package com.br.requirementhub.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@Entity
@Table(name = "requirement_artifact")
public class RequirementArtifact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String identifier;

    private String name;

    private String type;

    private String description;

    @Column(name = "date_created")
    private LocalDateTime dateCreated = LocalDateTime.now();

    @Column(name = "file")
    private String file;

    @ManyToOne
    @JoinColumn(name = "requirement_id", nullable = false)
    private Requirement requirementId;

    @PostPersist
    public void autoCompleteData() {
        if (this.identifier == null) {
            this.identifier = this.type + "-" + String.format("%04d", this.id);
        }
    }
}