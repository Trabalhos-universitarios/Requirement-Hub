package com.br.requirementhub.controller;

import com.br.requirementhub.entity.RequirementHistory;
import com.br.requirementhub.services.RequirementHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/requirement-history", produces = "application/json")
public class RequirementHistoryController {

    @Autowired
    private com.br.requirementhub.services.RequirementHistoryService requirementHistoryService;

    @Operation(summary = "Get all history")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "History retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{projectId}")
    public List<RequirementHistory> getHistoryByProject(@PathVariable Long projectId) {
        return requirementHistoryService.getHistoryByProject(projectId);
    }

    @Operation(summary = "Get history by identifier and project")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "History retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{identifier}/{projectId}")
    public List<RequirementHistory> getHistoryByIdentifierAndProject(@PathVariable String identifier, @PathVariable Long projectId) {
        return requirementHistoryService.getHistoryByIdentifierAndProject(identifier, projectId);
    }

    @Operation(summary = "Delete history by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "History deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRequirement(@PathVariable Long id) {
        requirementHistoryService.deleteHistory(id);
        return ResponseEntity.ok().build();
    }
}
