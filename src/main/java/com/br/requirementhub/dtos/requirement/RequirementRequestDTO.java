package com.br.requirementhub.dtos.requirement;

import com.br.requirementhub.entity.Project;
import com.br.requirementhub.entity.Requirement;
import com.br.requirementhub.entity.Stakeholder;
import com.br.requirementhub.entity.User;
import java.util.List;
import lombok.Data;

@Data
public class RequirementRequestDTO {
    private Long id;
    private Long author;
    private String identifier;
    private String name;
    private Double version;
    private String risk;
    private String priority;
    private String type;
    private Integer effort;
    private Long developerAssigned;
    private String description;
    private Project projectRelated;
    private List<Stakeholder> stakeholders;
    private List<User> responsible;
    private List<Requirement> dependencies;
}
