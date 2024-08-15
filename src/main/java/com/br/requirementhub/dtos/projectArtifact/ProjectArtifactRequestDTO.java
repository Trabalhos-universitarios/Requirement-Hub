package com.br.requirementhub.dtos.projectArtifact;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ProjectArtifactRequestDTO {
    private String name;
    private String type;
    private MultipartFile artifact_file;
    private Long projectId;

    public void setId(Long id) {
    }
}

