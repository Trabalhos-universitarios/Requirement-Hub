package com.br.requirementhub.dtos.projectArtifact;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ProjectArtifactRequestDTO {
    private String name;
    private String fileName;
    private String type;
    private Long size;
    private String contentBase64;
    private byte[] content;
    private Long projectId;
}

