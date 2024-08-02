package com.br.requirementhub.services;

import com.br.requirementhub.entity.User;
import com.br.requirementhub.enums.Role;
import com.br.requirementhub.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<User> findByRole(Role role) {
        return userRepository.findAll().stream()
                .filter(user -> user.getRole().equals(role))
                .collect(Collectors.toList());
    }
}