package com.br.requirementhub.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.br.requirementhub.dtos.stakeholder.StakeholderResponseDto;
import com.br.requirementhub.services.StakeholderService;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

class StakeholderControllerTest {

    @Mock
    private StakeholderService service;

    @InjectMocks
    private StakeholderController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllStakeholdersSuccessfully() {
        Set<StakeholderResponseDto> stakeholders = new HashSet<>();
        when(service.getAllStakeholders()).thenReturn(stakeholders);

        ResponseEntity<Set<StakeholderResponseDto>> result = controller.getAllStakeholders();

        assertEquals(ResponseEntity.ok(stakeholders), result);
        verify(service, times(1)).getAllStakeholders();
    }

    @Test
    void getAllStakeholdersEmptySet() {
        when(service.getAllStakeholders()).thenReturn(Collections.emptySet());

        ResponseEntity<Set<StakeholderResponseDto>> result = controller.getAllStakeholders();

        assertEquals(ResponseEntity.ok(Collections.emptySet()), result);
        verify(service, times(1)).getAllStakeholders();
    }
}
