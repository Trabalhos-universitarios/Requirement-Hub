package com.br.requirementhub.controller;

import com.br.requirementhub.dtos.UserDTO;
import com.br.requirementhub.enums.Role;
import com.br.requirementhub.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/gerentes")
    public List<UserDTO> getGerentes() {
        return userService.findByRole(Role.GERENTE_DE_PROJETOS).stream()
                .map(user -> new UserDTO(user.getId(), user.getName(), user.getRole()))
                .collect(Collectors.toList());
    }

    @GetMapping("/analistas-requisitos")
    public List<UserDTO> getAnalistasRequisitos() {
        return userService.findByRole(Role.ANALISTA_DE_REQUISITOS).stream()
                .map(user -> new UserDTO(user.getId(), user.getName(), user.getRole()))
                .collect(Collectors.toList());
    }

    @GetMapping("/analistas-negocio")
    public List<UserDTO> getAnalistasNegocio() {
        return userService.findByRole(Role.ANALISTA_DE_NEGOCIO).stream()
                .map(user -> new UserDTO(user.getId(), user.getName(), user.getRole()))
                .collect(Collectors.toList());
    }

    @GetMapping("/usuarios-comuns")
    public List<UserDTO> getUsuariosComuns() {
        return userService.findByRole(Role.USUARIO_COMUM).stream()
                .map(user -> new UserDTO(user.getId(), user.getName(), user.getRole()))
                .collect(Collectors.toList());
    }
}