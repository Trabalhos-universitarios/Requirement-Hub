package com.br.requirementhub.dtos.requirement;

import java.util.Set;
import lombok.Data;

@Data
public class RequirementResponseDTO {
    private Long id;
    private String identifier;
    private String name;
    private String description;
    private Double version;
    private String author;
    private String risk;
    private String priority;
    private String type;
    private String status;
    private Integer effort;
    private Long projectId;
    private String dateCreated;
    private Set<Long> artifactIds;
    private Set<Long> responsibleIds;
    private Set<Long> dependencyIds;
    private Set<Long> stakeholderIds;
}