package com.br.requirementhub.dtos.requirement;

import com.br.requirementhub.entity.Requirement;
import com.br.requirementhub.entity.Stakeholder;
import com.br.requirementhub.entity.User;
import java.util.List;
import lombok.Data;

@Data
public class RequirementUpdateRequestDTO {
    private Long id;
    private Long author;
    private String identifier;
    private String name;
    private Double version;
    private String risk;
    private String priority;
    private String type;
    private Integer effort;
    private String description;
    private Long projectRelated;
    private List<Stakeholder> stakeholders;
    private List<User> responsible;
    private List<Requirement> dependencies;
}
