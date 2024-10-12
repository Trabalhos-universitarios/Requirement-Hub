package com.br.requirementhub.controller;

import static com.br.requirementhub.enums.Role.ANALISTA_DE_NEGOCIO;
import static com.br.requirementhub.enums.Role.ANALISTA_DE_REQUISITOS;
import static com.br.requirementhub.enums.Role.GERENTE_DE_PROJETOS;
import static com.br.requirementhub.enums.Role.USUARIO_COMUM;

import com.br.requirementhub.dtos.user.UserResponseDTO;
import com.br.requirementhub.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/user", produces = "application/json")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @Operation(summary = "Get all users by role")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/managers")
    public List<UserResponseDTO> getManagers() {
        return service.findByRole(GERENTE_DE_PROJETOS);
    }

    @Operation(summary = "Get all users by role")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/requirement-analysts")
    public List<UserResponseDTO> getRequirementAnalysts() {
        return service.findByRole(ANALISTA_DE_REQUISITOS);
    }

    @Operation(summary = "Get all users by role")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/business-analysts")
    public List<UserResponseDTO> getBusinessAnalysts() {
        return service.findByRole(ANALISTA_DE_NEGOCIO);
    }

    @Operation(summary = "Get all users by role")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/common-users")
    public List<UserResponseDTO> getCommonUsers() {
        return service.findByRole(USUARIO_COMUM);
    }

    @Operation(summary = "Get user by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public UserResponseDTO getUserById(@PathVariable Long id) {
        return service.getById(id);
    }

    @Operation(summary = "Get all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/all")
    public List<UserResponseDTO> getAllUsers() {
        return service.getAllUsers();
    }

    @Operation(summary = "Get user notifications")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notifications retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/notifications/{id}")
    public List<Long> getNotifications(@PathVariable Long id) {
        return service.getUserNotifications(id);
    }

    @Operation(summary = "Update user image")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User image updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PatchMapping("/{id}/image")
    public UserResponseDTO updateUserImage(@PathVariable Long id, @RequestBody String image) {
        return service.updateUserImage(id, image);
    }

    @Operation(summary = "Delete user notification")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notification deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/notifications/{userId}/{requirementId}")
    public void deleteNotification(
            @Param("user_id") @PathVariable Long userId,
            @Param("requirement_id") @PathVariable Long requirementId) {
        service.deleteNotification(userId, requirementId);
    }
}
