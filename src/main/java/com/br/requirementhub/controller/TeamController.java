package com.br.requirementhub.controller;


import com.br.requirementhub.dtos.team.TeamResponseDTO;

import com.br.requirementhub.services.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/team", produces = "application/json")
public class TeamController {

    @Autowired
    private TeamService service;

    @Operation(summary = "Get all teams by project id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Teams retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<List<TeamResponseDTO>> getTeam(@PathVariable Long id) {
        List<TeamResponseDTO> teamResponseDTOs = service.findByProjectId(id);
        return ResponseEntity.ok(teamResponseDTOs);
    }
}
