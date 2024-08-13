package com.br.requirementhub.dtos.team;

import lombok.Data;

@Data
public class TeamResponseDTO {
    private Long id;
    private String userName;
    private Long projectId;
    private Long userId;
}
