package com.br.requirementhub.dtos.requirement;

import lombok.Data;
import java.util.List;

@Data
public class RequirementResponseDTO {
    private Long id;
    private String identifier;
    private String name;
    private String description;
    private String version;
    private String author;
    private String source;
    private String risk;
    private String priority;
    private String responsible;
    private String type;
    private String status;
    private Integer effort;
    private String release;
    private String dependency;
    private Long projectId;
    private List<Long> artifactIds;
}