package com.br.requirementhub.dtos.requirementArtifact;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class RequirementArtifactRequestDTO {
    private String identify;
    private String type;
    private String description;
    private MultipartFile artifact_file;
    private Long requirementId;
}