package com.br.requirementhub.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "requirement")
public class Requirement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "identifier")
    private String identifier;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "version")
    private String version;

    @Column(name = "author")
    private String author;

    @Column(name = "source")
    private String source;

    @Column(name = "risk")
    private String risk;

    @Column(name = "priority")
    private String priority;

    @Column(name = "responsible")
    private String responsible;

    @Column(name = "type")
    private String type;

    @Column(name = "status")
    private String status;

    @Column(name = "effort")
    private Integer effort;

    @Column(name = "release")
    private String release;

    @Column(name = "dependency")
    private String dependency;
    
    @ManyToOne
    @JoinColumn(name = "id_projeto", nullable = false)
    private Project project;

//    @OneToMany(mappedBy = "requirement", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<RequirementArtifact> artifacts;

    @OneToMany(mappedBy = "requirement", cascade = CascadeType.ALL)
    private List<RequirementArtifact> artifacts = new ArrayList<>();
}