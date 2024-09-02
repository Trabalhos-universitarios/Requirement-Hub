package com.br.requirementhub.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "PROJECT")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String manager;

    private String status;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String version;

    @Column(name = "creation_date")
    private Date creationDate;

    @Column(name = "last_update")
    private Date lastUpdate;

    private boolean draft;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Team> teams;

    @OneToMany(mappedBy = "projectRelated", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Requirement> requirements;

    public Long getId() {return id;}
}