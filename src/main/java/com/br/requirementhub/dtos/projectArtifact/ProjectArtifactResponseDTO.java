package com.br.requirementhub.dtos.projectArtifact;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProjectArtifactResponseDTO {
    private Long id;
    private String name;
    private String type;
    private byte[] artifact;
    private Long projectId;
}

