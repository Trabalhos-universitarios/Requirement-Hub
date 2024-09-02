package com.br.requirementhub.controller;

import com.br.requirementhub.services.TraceabilityMatrixService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/matrix")
@RequiredArgsConstructor
public class TraceabilityMatrixController {

    private final TraceabilityMatrixService traceabilityMatrixService;

    @GetMapping("/generate/{projectId}")
    public ResponseEntity<String> generateMatrix(@PathVariable Long projectId) throws Exception {
        traceabilityMatrixService.initializeMatrix(projectId);
        traceabilityMatrixService.markAllDependencies(projectId);
        return ResponseEntity.ok(traceabilityMatrixService.toJson());
    }
}