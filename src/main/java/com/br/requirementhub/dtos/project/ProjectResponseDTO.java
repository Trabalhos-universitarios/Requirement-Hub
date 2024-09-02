package com.br.requirementhub.dtos.project;

import com.br.requirementhub.utils.DateUtils;
import java.util.Date;
import java.util.List;
import lombok.Data;

@Data
public class ProjectResponseDTO {

    private Long id;
    private String name;
    private String manager;
    private String status;
    private List<String> requirementAnalysts;
    private List<String> businessAnalysts;
    private List<String> commonUsers;
    private String description;
    private String version;
    private String creationDate;
    private String lastUpdate;
    private List<Long> requirementIds;


    public void setCreationDate(Date creationDate) {
        if (creationDate != null) {
            this.creationDate = DateUtils.formatDate(creationDate.getTime());
        } else {
            this.creationDate = null;
        }
    }

    public void setLastUpdate(Date lastUpdate) {
        if (lastUpdate != null) {
            this.lastUpdate = DateUtils.formatDate(lastUpdate.getTime());
        } else {
            this.lastUpdate = null;
        }
    }
}