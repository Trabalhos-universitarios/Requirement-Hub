package com.br.requirementhub.dtos.requirement;

import com.br.requirementhub.entity.Project;
import com.br.requirementhub.entity.Requirement;
import com.br.requirementhub.entity.Stakeholder;
import com.br.requirementhub.entity.User;
import java.util.Set;
import lombok.Data;

@Data
public class RequirementRequestDTO {
    private Long id;
    private String identifier;
    private String name;
    private Double version;
    private String risk;
    private String priority;
    private String type;
    private Integer effort;
    private String description;
    private Project projectRelated;
    private Set<Stakeholder> stakeholders;
    private Set<User> responsible;
    private Set<Requirement> dependencies;
}