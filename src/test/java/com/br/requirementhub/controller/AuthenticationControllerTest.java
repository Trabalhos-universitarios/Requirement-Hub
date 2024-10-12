package com.br.requirementhub.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.br.requirementhub.dtos.auth.AuthenticationRequestDTO;
import com.br.requirementhub.dtos.auth.AuthenticationResponseDTO;
import com.br.requirementhub.entity.User;
import com.br.requirementhub.services.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

class AuthenticationControllerTest {

    @Mock
    private AuthenticationService service;

    @InjectMocks
    private AuthenticationController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerUserSuccessfully() {
        User user = new User();
        AuthenticationResponseDTO response = new AuthenticationResponseDTO();
        when(service.register(any(User.class))).thenReturn(response);

        ResponseEntity<AuthenticationResponseDTO> result = controller.register(user);

        assertEquals(ResponseEntity.ok(response), result);
        verify(service, times(1)).register(user);
    }

    @Test
    void authenticateUserSuccessfully() {
        AuthenticationRequestDTO request = new AuthenticationRequestDTO();
        AuthenticationResponseDTO response = new AuthenticationResponseDTO();
        when(service.login(any(AuthenticationRequestDTO.class))).thenReturn(response);

        ResponseEntity<AuthenticationResponseDTO> result = controller.authenticate(request);

        assertEquals(ResponseEntity.ok(response), result);
        verify(service, times(1)).login(request);
    }

    @Test
    void deleteUserSuccessfully() {
        Long userId = 1L;
        doNothing().when(service).deleteUser(userId);

        ResponseEntity<Void> result = controller.deleteUser(userId);

        assertEquals(ResponseEntity.noContent().build(), result);
        verify(service, times(1)).deleteUser(userId);
    }
}
