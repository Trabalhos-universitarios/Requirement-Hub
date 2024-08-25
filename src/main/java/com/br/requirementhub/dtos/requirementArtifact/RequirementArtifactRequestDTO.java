package com.br.requirementhub.dtos.requirementArtifact;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class RequirementArtifactRequestDTO {
    private String name;
    private RequirementArtifactTypeAndIdentifierDTO type;
    private String description;
    private String file;
    private Long requirementId;
}