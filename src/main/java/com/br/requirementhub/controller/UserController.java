package com.br.requirementhub.controller;

import static com.br.requirementhub.enums.Role.ANALISTA_DE_NEGOCIO;
import static com.br.requirementhub.enums.Role.ANALISTA_DE_REQUISITOS;
import static com.br.requirementhub.enums.Role.GERENTE_DE_PROJETOS;
import static com.br.requirementhub.enums.Role.USUARIO_COMUM;

import com.br.requirementhub.dtos.user.UserResponseDTO;
import com.br.requirementhub.services.UserService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @GetMapping("/managers")
    public List<UserResponseDTO> getManagers() {
        return service.findByRole(GERENTE_DE_PROJETOS).stream()
                .map(user -> new UserResponseDTO(user.getId(), user.getName(), user.getRole()))
                .collect(Collectors.toList());
    }

    @GetMapping("/requirement-analysts")
    public List<UserResponseDTO> getRequirementAnalysts() {
        return service.findByRole(ANALISTA_DE_REQUISITOS).stream()
                .map(user -> new UserResponseDTO(user.getId(), user.getName(), user.getRole()))
                .collect(Collectors.toList());
    }

    @GetMapping("/business-analysts")
    public List<UserResponseDTO> getBusinessAnalysts() {
        return service.findByRole(ANALISTA_DE_NEGOCIO).stream()
                .map(user -> new UserResponseDTO(user.getId(), user.getName(), user.getRole()))
                .collect(Collectors.toList());
    }

    @GetMapping("/common-users")
    public List<UserResponseDTO> getCommonUsers() {
        return service.findByRole(USUARIO_COMUM).stream()
                .map(user -> new UserResponseDTO(user.getId(), user.getName(), user.getRole()))
                .collect(Collectors.toList());
    }
}