package com.br.requirementhub.controller;


import com.br.requirementhub.dtos.comments.CommentsRequestDto;
import com.br.requirementhub.dtos.requirement.RequirementRequestDTO;
import com.br.requirementhub.dtos.requirement.RequirementResponseDTO;
import com.br.requirementhub.dtos.requirement.RequirementUpdateRequestDTO;
import com.br.requirementhub.dtos.requirementArtifact.RequirementArtifactResponseDTO;
import com.br.requirementhub.entity.RequirementArtifact;
import com.br.requirementhub.enums.Status;
import com.br.requirementhub.repository.RequirementArtifactRepository;
import com.br.requirementhub.services.RequirementArtifactService;
import com.br.requirementhub.services.RequirementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requirements", produces = "application/json")
public class RequirementController {

    private final RequirementService service;

    private final RequirementArtifactService artifactService;

    private final RequirementArtifactRepository requirementArtifactRepository;

    private final RequirementService requirementService;

    @Operation(summary = "Get all requirements")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Requirements retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<List<RequirementResponseDTO>> getAllRequirements() {
        return ResponseEntity.ok(service.getAllRequirements());
    }

    @Operation(summary = "Get requirement by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Requirement retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<RequirementResponseDTO> getRequirementById(@PathVariable Long id) {
        RequirementResponseDTO requirement = service.getRequirementById(id);
        return requirement != null ? ResponseEntity.ok(requirement) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Get requirement by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Requirement retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/requirement-id/{id}")
    public ResponseEntity<List<RequirementResponseDTO>> getRequirementDataToUpdate(@PathVariable Long id) {
        List<RequirementResponseDTO> requirement = service.getRequirementDataToUpdate(id);
        return requirement != null ? ResponseEntity.ok(requirement) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Get requirement by identifier and project related")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Requirement retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/requirementByProjectRelated/{identifier}/{projectId}")
    public ResponseEntity<RequirementResponseDTO> getRequirementByIdentifierAndProjectRelated(
            @PathVariable String identifier, @PathVariable Long projectId) {
        List<RequirementResponseDTO> requirements = service.getRequirementsByProjectRelated(projectId);
        RequirementResponseDTO requirement = requirements.stream()
                .filter(req -> identifier.equals(req.getIdentifier()))
                .findFirst()
                .orElse(null);
        return requirement != null ? ResponseEntity.ok(requirement) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Get requirement by project related")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Requirement retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/project-id/{id}")
    public ResponseEntity<List<RequirementResponseDTO>> getRequirementByProjectRelated(@PathVariable Long id) {
        List<RequirementResponseDTO> requirement = service.getRequirementsByProjectRelated(id);
        return requirement != null ? ResponseEntity.ok(requirement) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Get requirement artifact by identifier and project related")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Requirement artifact retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/artifactRequirementByProjectRelated/{identifier}/{projectId}")
    public ResponseEntity<RequirementArtifactResponseDTO> getRequirementArtifactByIdentifierAndProjectRelated(
            @PathVariable String identifier, @PathVariable Long projectId) {
        List<RequirementResponseDTO> requirements = service.getRequirementsByProjectRelated(projectId);

        for (RequirementResponseDTO requirement : requirements) {
            List<RequirementArtifactResponseDTO> artifacts =
                    artifactService.findArtifactsByRequirementId(requirement.getId());

            for (RequirementArtifactResponseDTO artifact : artifacts) {
                if (identifier.equals(artifact.getIdentifier())) {

                    RequirementArtifact requirementArtifact =
                            requirementArtifactRepository.findByIdentifierArtifact(identifier);

                    RequirementArtifactResponseDTO responseDTO =
                            artifactService.convertToResponseDTO(requirementArtifact);
                    return ResponseEntity.ok(responseDTO);
                }
            }
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Get requirement by project id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Requirement retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/byproject/{id}")
    public ResponseEntity<List<RequirementResponseDTO>> listRequirementsByProjectId(@PathVariable Long id) {
        List<RequirementResponseDTO> requirement = service.listByProjectId(id);
        return requirement != null ? ResponseEntity.ok(requirement) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Get requirements by ids")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Requirements retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/requirement-id")
    public List<RequirementResponseDTO> getRequirementsByIds(@RequestParam List<Long> ids) {
        return requirementService.getRequirementsByIds(ids);
    }

    @Operation(summary = "Get all requirement responsibles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Responsibles retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/all-responsibles")
    public ResponseEntity<List<Object[]>> getAllRequirementResponsibles() {
        List<Object[]> responsibles = service.getAllRequirementResponsible();
        return ResponseEntity.ok(responsibles);
    }

    @Operation(summary = "Create a new requirement")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Requirement created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "409", description = "Requirement already exists"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<RequirementResponseDTO> createRequirement(
            @RequestBody RequirementRequestDTO requirementRequestDTO) {
        RequirementResponseDTO createdRequirement = service.createRequirement(requirementRequestDTO);
        return ResponseEntity.status(201).body(createdRequirement);
    }

    @Operation(summary = "Update a requirement")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Requirement updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "404", description = "Requirement not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{id}")
    public ResponseEntity<RequirementResponseDTO> updateRequirement(
            @PathVariable Long id,
            @RequestBody RequirementUpdateRequestDTO requirementRequestDTO) {
        RequirementResponseDTO updatedRequirement = service.updateRequirement(id, requirementRequestDTO);
        return updatedRequirement != null ? ResponseEntity.ok(updatedRequirement) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Delete a requirement")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRequirement(@PathVariable Long id) {
        service.deleteRequirement(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Send requirement to approval flow")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Requirement sent to approval flow successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "404", description = "Requirement not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/flow")
    public ResponseEntity<RequirementResponseDTO> sendToApprovalFlow(
            @RequestBody RequirementRequestDTO requirementRequestDTO) {
        RequirementResponseDTO createdRequirement = service.sendToApprovalFlow(requirementRequestDTO);
        return ResponseEntity.status(201).body(createdRequirement);
    }

    @Operation(summary = "Send requirement to approval flow by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Requirement sent to approval flow successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "404", description = "Requirement not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PatchMapping("/flow/{id}")
    public ResponseEntity<RequirementResponseDTO> sendToApprovalRequirement(@PathVariable Long id) {
        RequirementResponseDTO updatedEntity = requirementService.sendToApprovalFlowRequirementId(id);
        return ResponseEntity.ok(updatedEntity);
    }

    @Operation(summary = "Update requirement status by id")
    @PatchMapping("/status/{id}/{status}")
    public ResponseEntity<RequirementResponseDTO> updateRequirementStatus(
            @PathVariable Long id,
            @PathVariable String status) {

        RequirementResponseDTO updatedEntity = requirementService.updateRequirementStatus(id, status);
        return ResponseEntity.ok(updatedEntity);
    }

    @Operation(summary = "Assign developer requirement by id")
    @PatchMapping("/assign-developer/{id}/{developerAssigned}")
    public ResponseEntity<RequirementResponseDTO> assignDeveloper(
            @PathVariable Long id,
            @PathVariable Long developerAssigned) {
        RequirementResponseDTO updatedEntity = requirementService.assignDeveloper(id, developerAssigned);
        return ResponseEntity.ok(updatedEntity);
    }


    @Operation(summary = "Blocked requirement by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Requirement blocked successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "404", description = "Requirement not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PatchMapping("/blocked/{id}")
    public ResponseEntity<RequirementResponseDTO> blockedRequirement(@PathVariable Long id) {
        RequirementResponseDTO updatedEntity = requirementService.blockedRequirementId(id);
        return ResponseEntity.ok(updatedEntity);
    }

    @Operation(summary = "Refuse requirement by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Requirement refused successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "404", description = "Requirement not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PatchMapping("/refuse/{id}")
    public ResponseEntity<RequirementResponseDTO> refuseRequirement(
            @PathVariable Long id,
            @RequestBody CommentsRequestDto commentsRequestDto
    ) {
        RequirementResponseDTO updatedEntity = requirementService.refuseRequirement(id, commentsRequestDto);
        return ResponseEntity.ok(updatedEntity);
    }

    @Operation(summary = "Approve requirement by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Requirement approved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "404", description = "Requirement not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PatchMapping("/approve/{id}")
    public ResponseEntity<RequirementResponseDTO> approveRequirement(
            @PathVariable Long id
    ) {
        RequirementResponseDTO updatedEntity = requirementService.approveRequirement(id);
        return ResponseEntity.ok(updatedEntity);
    }
}
