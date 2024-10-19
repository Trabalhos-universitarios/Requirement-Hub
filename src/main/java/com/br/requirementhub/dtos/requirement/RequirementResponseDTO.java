package com.br.requirementhub.dtos.requirement;

import java.util.List;
import lombok.Data;

@Data
public class RequirementResponseDTO {
    private Long id;
    private String identifier;
    private String name;
    private String description;
    private String version;
    private String author;
    private String risk;
    private String priority;
    private String type;
    private String status;
    private Integer effort;
    private Long developerAssigned;
    private Long projectId;
    private String dateCreated;
    private List<Long> artifactIds;
    private List<Long> responsibleIds;
    private List<Long> dependencyIds;
    private List<Long> stakeholderIds;
}

