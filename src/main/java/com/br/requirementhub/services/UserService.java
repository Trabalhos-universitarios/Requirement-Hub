package com.br.requirementhub.services;

import com.br.requirementhub.dtos.user.UserRequestDTO;
import com.br.requirementhub.dtos.user.UserResponseDTO;
import com.br.requirementhub.entity.User;
import com.br.requirementhub.enums.Role;
import com.br.requirementhub.exceptions.UserNotFoundException;
import com.br.requirementhub.repository.UserRepository;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

    public Role findUserRoleById(Long userId) {
        return repository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id: " + userId))
                .getRole();
    }

    public UserResponseDTO getById(Long id) {
        Optional<User> user = Optional.ofNullable(repository.findById(id).orElseThrow(
                () -> new UserNotFoundException("User not found")
        ));

        return user.map(this::convertToResponseDTO).orElse(null);
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