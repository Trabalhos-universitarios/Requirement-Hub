package com.br.requirementhub.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

import com.br.requirementhub.dtos.team.TeamResponseDTO;
import com.br.requirementhub.services.TeamService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

class TeamControllerTest {

    @Mock
    private TeamService service;

    @InjectMocks
    private TeamController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getTeamSuccessfully() {
        List<TeamResponseDTO> response = List.of(new TeamResponseDTO());
        when(service.findByProjectId(anyLong())).thenReturn(response);

        ResponseEntity<List<TeamResponseDTO>> result = controller.getTeam(1L);

        assertEquals(ResponseEntity.ok(response), result);
        verify(service, times(1)).findByProjectId(1L);
    }

    @Test
    void getTeamWithInvalidId() {
        when(service.findByProjectId(anyLong())).thenReturn(Collections.emptyList());

        ResponseEntity<List<TeamResponseDTO>> result = controller.getTeam(-1L);

        assertEquals(ResponseEntity.ok(Collections.emptyList()), result);
        verify(service, times(1)).findByProjectId(-1L);
    }

    @Test
    void getTeamWithNoTeams() {
        when(service.findByProjectId(anyLong())).thenReturn(Collections.emptyList());

        ResponseEntity<List<TeamResponseDTO>> result = controller.getTeam(2L);

        assertEquals(ResponseEntity.ok(Collections.emptyList()), result);
        verify(service, times(1)).findByProjectId(2L);
    }
}
