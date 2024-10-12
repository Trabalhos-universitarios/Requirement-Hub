package com.br.requirementhub.controller;

import com.br.requirementhub.dtos.stakeholder.StakeholderResponseDto;
import com.br.requirementhub.services.StakeholderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/stakeholders", produces = "application/json")
public class StakeholderController {

    private final StakeholderService service;

    @Operation(summary = "Get all stakeholders")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stakeholders retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<Set<StakeholderResponseDto>> getAllStakeholders() {
        return ResponseEntity.ok(service.getAllStakeholders());
    }
}
