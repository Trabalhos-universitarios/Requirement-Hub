package com.br.requirementhub.controller;

import static com.br.requirementhub.enums.Role.ANALISTA_DE_NEGOCIO;
import static com.br.requirementhub.enums.Role.ANALISTA_DE_REQUISITOS;
import static com.br.requirementhub.enums.Role.GERENTE_DE_PROJETOS;
import static com.br.requirementhub.enums.Role.USUARIO_COMUM;

import com.br.requirementhub.dtos.user.UserResponseDTO;
import com.br.requirementhub.services.UserService;
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
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @GetMapping("/managers")
    public List<UserResponseDTO> getManagers() {
        return service.findByRole(GERENTE_DE_PROJETOS);
    }

    @GetMapping("/requirement-analysts")
    public List<UserResponseDTO> getRequirementAnalysts() {
        return service.findByRole(ANALISTA_DE_REQUISITOS);
    }

    @GetMapping("/business-analysts")
    public List<UserResponseDTO> getBusinessAnalysts() {
        return service.findByRole(ANALISTA_DE_NEGOCIO);
    }

    @GetMapping("/common-users")
    public List<UserResponseDTO> getCommonUsers() {
        return service.findByRole(USUARIO_COMUM);
    }

    @GetMapping("/{id}")
    public UserResponseDTO getUserById(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping("/all")
    public List<UserResponseDTO> getAllUsers() {
        return service.getAllUsers();
    }

    @GetMapping("/notifications/{id}")
    public List<Long> getNotifications(@PathVariable Long id) {
        return service.getUserNotifications(id);
    }

    @PatchMapping("/{id}/image")
    public UserResponseDTO updateUserImage(@PathVariable Long id, @RequestBody String image) {
        return service.updateUserImage(id, image);
    }

    @DeleteMapping("/notifications/{userId}/{requirementId}")
    public void deleteNotification(
            @Param("user_id") @PathVariable Long userId,
            @Param("requirement_id") @PathVariable Long requirementId) {
        service.deleteNotification(userId, requirementId);
    }
}
