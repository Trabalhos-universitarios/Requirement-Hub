package com.br.requirementhub.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "PROJECTS")
public class Project implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NAME", nullable = false)
    private String name;

//    @Lob
    @Column(name = "artifact_file", columnDefinition = "bytea")
    private byte[] artifactFile;

    @Column(name = "MANAGER")
    private String manager;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "PROJECT_TYPE")
    private String projectType;

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
    @Temporal(TemporalType.DATE)
    private Date creationDate;

    @Column(name = "LAST_UPDATE")
    @Temporal(TemporalType.DATE)
    private Date lastUpdate;


    //todo ---
    /*
     * DATA INICIO
     * DATA FIM
     * OUTRO PROJETO IMPACTADO
     * DATA PROGRAMADA
     * ESCOPO
     * */

}
