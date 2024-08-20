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

    @GetMapping("/initialize")
    public ResponseEntity<Void> initializeMatrix() {
        traceabilityMatrixService.initializeMatrix();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/mark-dependencies")
    public ResponseEntity<Void> markAllDependencies() {
        traceabilityMatrixService.markAllDependencies();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/json")
    public ResponseEntity<String> getMatrixAsJson() throws Exception {
        return ResponseEntity.ok(traceabilityMatrixService.toJson());
    }
}