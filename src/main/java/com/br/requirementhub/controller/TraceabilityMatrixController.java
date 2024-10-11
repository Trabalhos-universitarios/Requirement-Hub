package com.br.requirementhub.controller;

import com.br.requirementhub.services.TraceabilityMatrixService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/matrix", produces = "application/json")
@RequiredArgsConstructor
public class TraceabilityMatrixController {

    private final TraceabilityMatrixService traceabilityMatrixService;

    @Operation(summary = "Generate traceability matrix")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Traceability matrix generated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/generate/{projectId}")
    public ResponseEntity<String> generateMatrix(@PathVariable Long projectId) throws Exception {
        traceabilityMatrixService.initializeMatrix(projectId);
        traceabilityMatrixService.markAllDependencies(projectId);
        return ResponseEntity.ok(traceabilityMatrixService.toJson());
    }
}
