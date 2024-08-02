package com.br.requirementhub.services;

import com.br.requirementhub.dtos.AuthenticationRequestDTO;
import com.br.requirementhub.dtos.AuthenticationResponseDTO;
import com.br.requirementhub.entity.User;
import com.br.requirementhub.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthenticationService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;


    private final PasswordEncoder passwordEncoder;

    public AuthenticationService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }


    public AuthenticationResponseDTO register(User request) {
        var user = new User();
        user.setName(request.getName());
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        userRepository.save(user);
        String token = jwtService.generateToken(user, generateExtraClaims(user));
        return new AuthenticationResponseDTO(token);
    }


    public AuthenticationResponseDTO login(AuthenticationRequestDTO authenticationRequestDTO) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                authenticationRequestDTO.getUsername(), authenticationRequestDTO.getPassword()
        );
        authenticationManager.authenticate(authToken);
        User user = userRepository.findByUsername(authenticationRequestDTO.getUsername()).get();
        String jwt = jwtService.generateToken(user, generateExtraClaims(user));
        return new AuthenticationResponseDTO(jwt);
    }


    private Map<String, Object> generateExtraClaims(User user) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("name", user.getName());
        extraClaims.put("role", user.getRole().name());
        return extraClaims;
    }
}
