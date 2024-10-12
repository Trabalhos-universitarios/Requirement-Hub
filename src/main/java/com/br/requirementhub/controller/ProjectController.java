package com.br.requirementhub.controller;

import com.br.requirementhub.dtos.project.ProjectRequestDTO;
import com.br.requirementhub.dtos.project.ProjectResponseDTO;
import com.br.requirementhub.entity.Project;
import com.br.requirementhub.services.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/project", produces = "application/json")
public class ProjectController {

    private final ProjectService service;

    @Operation(summary = "Create a new project")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Project created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "409", description = "Project already exists"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("")
    public ResponseEntity<ProjectResponseDTO> create(@RequestBody ProjectRequestDTO request) throws IOException {
        ProjectResponseDTO responseDTO = service.create(request);
        return ResponseEntity.status(201).body(responseDTO);
    }

    @Operation(summary = "Get all projects")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Projects retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/all")
    public ResponseEntity<List<ProjectResponseDTO>> list() {
        return ResponseEntity.ok(service.list());
    }

    @Operation(summary = "Get all projects by user id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Projects retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/all/{userId}")
    public ResponseEntity<List<ProjectResponseDTO>> list(@PathVariable Long userId) {
        return ResponseEntity.ok(service.listProjectsByUserId(userId));
    }

    @Operation(summary = "Get all projects by manager id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Projects retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/all-manager/{managerId}")
    public ResponseEntity<List<ProjectResponseDTO>> lists(@PathVariable Long managerId) {
        return ResponseEntity.ok(service.listProjectsByManagerId(managerId));
    }

    @Operation(summary = "Get project by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Project retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponseDTO> find(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @Operation(summary = "Update a project")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Project updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProjectResponseDTO> update(@PathVariable Long id, @RequestBody ProjectRequestDTO request) throws IOException {
        ProjectResponseDTO updatedProject = service.update(id, request);
        return ResponseEntity.ok(updatedProject);
    }

    @Operation(summary = "Delete a project")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
