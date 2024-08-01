package com.br.requirementhub.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Data
@AllArgsConstructor
public class ProjectRequestDTO {

    private String name;
    private MultipartFile artifactFile;
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
