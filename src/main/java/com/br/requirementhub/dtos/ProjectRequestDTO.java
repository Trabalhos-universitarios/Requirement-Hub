package com.br.requirementhub.dtos;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProjectRequestDTO {

    private String name;
    private String manager;
    private String status;
    private List<Long> requirementAnalysts;
    private List<Long> businessAnalysts;
    private List<Long> commonUsers;
    private String description;
    private String version;
    private boolean draft;

}
