package com.br.requirementhub.services;

import com.br.requirementhub.dtos.user.UserRequestDTO;
import com.br.requirementhub.dtos.user.UserResponseDTO;
import com.br.requirementhub.entity.User;
import com.br.requirementhub.enums.Role;
import com.br.requirementhub.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Comparator;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    public List<UserResponseDTO> getAllUsers() {
        return repository.findAll().stream()
                .map(this::convertToResponseDTO)
                .sorted(Comparator.comparing(UserResponseDTO::getRole)) // Ordena os usuários restantes por 'role'
                .collect(Collectors.toList());
    }

    public List<UserResponseDTO> findByRole(Role role) {
        return repository.findByRole(role).stream()
                .map(user -> new UserResponseDTO(user.getId(), user.getName(), user.getRole()))
                .collect(Collectors.toList());
    }

    private UserResponseDTO convertToResponseDTO(User user) {
        return new UserResponseDTO(user.getId(), user.getName(), user.getRole());
    }

    private User convertToEntity(UserRequestDTO dto) {
        User user = new User();
        user.setName(dto.getName());
        user.setRole(dto.getRole());
        return user;
    }
}