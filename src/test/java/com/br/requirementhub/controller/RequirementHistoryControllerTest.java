package com.br.requirementhub.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.br.requirementhub.entity.RequirementHistory;
import com.br.requirementhub.services.RequirementHistoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

class RequirementHistoryControllerTest {

    @Mock
    private RequirementHistoryService requirementHistoryService;

    @InjectMocks
    private RequirementHistoryController requirementHistoryController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getHistoryByProjectSuccessfully() {
        Long projectId = 1L;
        List<RequirementHistory> historyList = List.of(new RequirementHistory());
        when(requirementHistoryService.getHistoryByProject(projectId)).thenReturn(historyList);

        List<RequirementHistory> result = requirementHistoryController.getHistoryByProject(projectId);

        assertEquals(historyList, result);
        verify(requirementHistoryService, times(1)).getHistoryByProject(projectId);
    }

    @Test
    void getHistoryByProjectEmpty() {
        Long projectId = 1L;
        when(requirementHistoryService.getHistoryByProject(projectId)).thenReturn(Collections.emptyList());

        List<RequirementHistory> result = requirementHistoryController.getHistoryByProject(projectId);

        assertEquals(Collections.emptyList(), result);
        verify(requirementHistoryService, times(1)).getHistoryByProject(projectId);
    }

    @Test
    void getHistoryByIdentifierAndProjectSuccessfully() {
        String identifier = "identifier";
        Long projectId = 1L;
        List<RequirementHistory> historyList = List.of(new RequirementHistory());
        when(requirementHistoryService.getHistoryByIdentifierAndProject(identifier, projectId)).thenReturn(historyList);

        List<RequirementHistory> result = requirementHistoryController.getHistoryByIdentifierAndProject(identifier, projectId);

        assertEquals(historyList, result);
        verify(requirementHistoryService, times(1)).getHistoryByIdentifierAndProject(identifier, projectId);
    }

    @Test
    void getHistoryByIdentifierAndProjectEmpty() {
        String identifier = "identifier";
        Long projectId = 1L;
        when(requirementHistoryService.getHistoryByIdentifierAndProject(identifier, projectId)).thenReturn(Collections.emptyList());

        List<RequirementHistory> result = requirementHistoryController.getHistoryByIdentifierAndProject(identifier, projectId);

        assertEquals(Collections.emptyList(), result);
        verify(requirementHistoryService, times(1)).getHistoryByIdentifierAndProject(identifier, projectId);
    }

    @Test
    void deleteRequirementSuccessfully() {
        Long id = 1L;
        doNothing().when(requirementHistoryService).deleteHistory(id);

        ResponseEntity<Void> result = requirementHistoryController.deleteRequirement(id);

        assertEquals(ResponseEntity.ok().build(), result);
        verify(requirementHistoryService, times(1)).deleteHistory(id);
    }
}
