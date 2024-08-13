package com.br.requirementhub.dtos.requirement;

import com.br.requirementhub.utils.DateUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;
import lombok.Data;

@Data
public class RequirementResponseDTO {
    private Long id;
    private String identifier;
    private String name;
    private String description;
    private Double version;
    private String author;
    private String risk;
    private String priority;
    private String type;
    private String status;
    private Integer effort;
    private Long projectId;
//    @JsonFormat(
//            shape = JsonFormat.Shape.STRING,
//            pattern = "yyyy-MM-dd'T'HH:mm:ss"
//    )
//    private LocalDateTime dateCreated;
    private String dateCreated;
    private Set<Long> artifactIds;
    private Set<Long> responsibleIds;
    private Set<Long> dependencyIds;
    private Set<Long> stakeholderIds;
}