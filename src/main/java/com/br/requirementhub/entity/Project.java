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
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "manager")
    private String manager;

    @Column(name = "status")
    private String status;

    @Column(name = "description")
    private String description;

    @Column(name = "version")
    private String version;

    @Column(name = "creation_date")
    private Date creationDate;

    @Column(name = "last_update")
    private Date lastUpdate;

    @Column(name = "draft")
    private boolean draft;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Team> teams;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Requirement> requirements;
}