package com.br.requirementhub.dtos.requirementArtifact;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RequirementArtifactResponseDTO {
    private Long id;
    private String identify;
    private String type;
    private String description;
    private byte[] artifact;
    private Long requirementId;
}