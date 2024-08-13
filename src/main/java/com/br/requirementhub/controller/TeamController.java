package com.br.requirementhub.controller;


import com.br.requirementhub.dtos.team.TeamResponseDTO;

import com.br.requirementhub.services.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/team")
public class TeamController {

    @Autowired
    private TeamService service;

    @GetMapping("/{id}")
    public ResponseEntity<List<TeamResponseDTO>> getTeam(@PathVariable Long id) {
        List<TeamResponseDTO> teamResponseDTOs = service.findByProjectId(id);
        return ResponseEntity.ok(teamResponseDTOs);
    }
}
