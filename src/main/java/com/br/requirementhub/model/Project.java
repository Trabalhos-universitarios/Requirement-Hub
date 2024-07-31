package com.br.requirementhub.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "PROJECTS")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "MANAGER")
    private String manager;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "TYPE_PROJECT")
    private String typeProject;

    @Column(name = "REQUIREMENTS_ANALYST")
    private String requirementsAnalyst;

    @Column(name = "BUSINESS_ANALYST")
    private String businessAnalyst;

    @Column(name = "COMMON_USER")
    private String commonUser;

    @Column(name = "DESCRIPTION", length = 3000)
    private String description;

    @Column(name = "VERSION")
    private String version;

    @Column(name = "CREATION_DATE")
    private Date creationDate;

    @Column(name = "LAST_UPDATE")
    private Date lastUpdate;


}
