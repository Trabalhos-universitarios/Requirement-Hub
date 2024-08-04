package com.br.requirementhub.dtos.project;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ProjectResponseDTO {

    private Long id;
    private String name;
    private String manager;
    private String status;
    private List<String> requirementAnalysts;
    private List<String> businessAnalysts;
    private List<String> commonUsers;
    private String description;
    private String version;
    private Date creationDate;
    private Date lastUpdate;
    private List<Long> requirementIds;
}