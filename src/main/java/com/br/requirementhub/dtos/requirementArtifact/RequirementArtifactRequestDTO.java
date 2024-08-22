package com.br.requirementhub.dtos.requirementArtifact;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class RequirementArtifactRequestDTO {
    private String name;
    private String type;
    private String description;
    private MultipartFile file;
    private Long requirementId;
}