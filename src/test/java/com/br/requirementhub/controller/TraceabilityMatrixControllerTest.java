package com.br.requirementhub.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.br.requirementhub.services.TraceabilityMatrixService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

class TraceabilityMatrixControllerTest {

    @Mock
    private TraceabilityMatrixService traceabilityMatrixService;

    @InjectMocks
    private TraceabilityMatrixController traceabilityMatrixController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void generateMatrixSuccessfully() throws Exception {
        Long projectId = 1L;
        when(traceabilityMatrixService.toJson()).thenReturn("matrixJson");

        ResponseEntity<String> result = traceabilityMatrixController.generateMatrix(projectId);

        assertEquals(ResponseEntity.ok("matrixJson"), result);
        verify(traceabilityMatrixService, times(1)).initializeMatrix(projectId);
        verify(traceabilityMatrixService, times(1)).markAllDependencies(projectId);
    }

//    @Test
//    void generateMatrixWithInvalidProjectId() throws Exception {
//        Long projectId = 1L;
//        doThrow(new IllegalArgumentException("Invalid project ID")).when(traceabilityMatrixService).initializeMatrix(anyLong());
//
//        ResponseEntity<String> result = traceabilityMatrixController.generateMatrix(projectId);
//
//        assertEquals(ResponseEntity.badRequest().body("Invalid project ID"), result);
//        verify(traceabilityMatrixService, times(1)).initializeMatrix(projectId);
//    }
//
//    @Test
//    void generateMatrixWithInternalError() throws Exception {
//        Long projectId = 1L;
//        doThrow(new RuntimeException("Internal server error")).when(traceabilityMatrixService).initializeMatrix(anyLong());
//
//        ResponseEntity<String> result = traceabilityMatrixController.generateMatrix(projectId);
//
//        assertEquals(ResponseEntity.status(500).body("Internal server error"), result);
//        verify(traceabilityMatrixService, times(1)).initializeMatrix(projectId);
//    }
}
