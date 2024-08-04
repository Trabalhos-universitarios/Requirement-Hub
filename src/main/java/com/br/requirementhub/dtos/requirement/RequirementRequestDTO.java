package com.br.requirementhub.dtos.requirement;

import lombok.Data;

@Data
public class RequirementRequestDTO {
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
}