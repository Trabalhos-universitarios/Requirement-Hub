package com.br.requirementhub.dtos.projectArtifact;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProjectArtifactResponseDTO {
    private Long id;
    private String name;
    private String fileName;
    private String type;
    private Long size;
    private byte[] content;
    private Long projectId;
}

