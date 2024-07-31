package com.br.requirementhub.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
public class ProjectResponseDTO {

    private Long id;
    private String name;
    private String manager;
    private String status;
    private String typeProject;
    private String requirementsAnalyst;
    private String businessAnalyst;
    private String commonUser;
    private String description;
    private String version;
    private Date creationDate;
    private Date lastUpdate;

}
