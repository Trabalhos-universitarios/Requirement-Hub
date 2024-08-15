package com.br.requirementhub.entity;

import static com.br.requirementhub.enums.Status.CREATED;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PostPersist;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import lombok.Data;

@Data
@Entity
@Table(name = "requirement")
public class Requirement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String identifier;

    private String name;

    private String description;

    private Double version;

    private Long author;

    private String risk;

    private String priority;

    private String type;

    private String status;

    private Integer effort;

    @Column(name = "date_created")
    private LocalDateTime dateCreated = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "project_related", nullable = false)
    private Project projectRelated;

    @OneToMany(mappedBy = "requirement", cascade = CascadeType.ALL)
    private Set<RequirementArtifact> artifacts = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "requirement_responsible",
            joinColumns = @JoinColumn(name = "requirement_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> responsible = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "requirement_stakeholder",
            joinColumns = @JoinColumn(name = "requirement_id"),
            inverseJoinColumns = @JoinColumn(name = "stakeholder_id")
    )
    private Set<Stakeholder> stakeholders = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "requirement_dependency",
            joinColumns = @JoinColumn(name = "requirement_id"),
            inverseJoinColumns = @JoinColumn(name = "dependency_id")
    )
    private Set<Requirement> dependencies = new HashSet<>();


    @PostPersist
    public void autoCompleteData() {
        if (this.identifier == null) {
            this.identifier = transformRequirementIdentifier(this.type) + "-" + String.format("%04d", this.id);
            this.version = 1.0;
            this.status = CREATED.toString();
        }
    }

    @JsonIgnore
    private String transformRequirementIdentifier(String identifier) {
        if (Objects.equals(identifier, "Funcional")) {
            identifier = "RF";
        } else if (Objects.equals(identifier, "Não Funcional")) {
            identifier = "RNF";
        }
        return identifier;
    }
}
