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

    @GetMapping("/generate")
    public ResponseEntity<String> generateMatrix() throws Exception {
        traceabilityMatrixService.initializeMatrix();
        traceabilityMatrixService.markAllDependencies();
        return ResponseEntity.ok(traceabilityMatrixService.toJson());
    }
}