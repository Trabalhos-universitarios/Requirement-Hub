package com.br.requirementhub.dtos;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class ProjectRequestDTO {

    private String nameProject;
    private String nameProjectManager;
    private String status;
    private List<String> nameRequirementAnalyst;
    private List<String> nameBusinessAnalyst;
    private List<String> nameCommonUser;
    private String projectDescription;
    private String version;
    private Date creationDate;



    //todo REVISAR OS CAMPOS QUE VÃO SAIR DAQUI
//    private String name;
//    private MultipartFile artifactFile;
//    private String manager;
//    private String status;
//    private String typeProject;
//    private String requirementsAnalyst;
//    private String businessAnalyst;
//    private String commonUser;
//    private String description;
//    private String version;
//    private Date creationDate;
//    private Date lastUpdate;

}
