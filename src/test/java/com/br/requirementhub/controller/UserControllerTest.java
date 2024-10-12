package com.br.requirementhub.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.br.requirementhub.dtos.user.UserResponseDTO;
import com.br.requirementhub.services.UserService;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class UserControllerTest {

    @Mock
    private UserService service;

    @InjectMocks
    private UserController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getManagersSuccessfully() {
        UserResponseDTO responseDTO = new UserResponseDTO();
        when(service.findByRole(any())).thenReturn(Collections.singletonList(responseDTO));

        List<UserResponseDTO> result = controller.getManagers();

        assertEquals(1, result.size());
        verify(service, times(1)).findByRole(any());
    }

    @Test
    void getRequirementAnalystsSuccessfully() {
        UserResponseDTO responseDTO = new UserResponseDTO();
        when(service.findByRole(any())).thenReturn(Collections.singletonList(responseDTO));

        List<UserResponseDTO> result = controller.getRequirementAnalysts();

        assertEquals(1, result.size());
        verify(service, times(1)).findByRole(any());
    }

    @Test
    void getBusinessAnalystsSuccessfully() {
        UserResponseDTO responseDTO = new UserResponseDTO();
        when(service.findByRole(any())).thenReturn(Collections.singletonList(responseDTO));

        List<UserResponseDTO> result = controller.getBusinessAnalysts();

        assertEquals(1, result.size());
        verify(service, times(1)).findByRole(any());
    }

    @Test
    void getCommonUsersSuccessfully() {
        UserResponseDTO responseDTO = new UserResponseDTO();
        when(service.findByRole(any())).thenReturn(Collections.singletonList(responseDTO));

        List<UserResponseDTO> result = controller.getCommonUsers();

        assertEquals(1, result.size());
        verify(service, times(1)).findByRole(any());
    }

    @Test
    void getUserByIdSuccessfully() {
        Long userId = 1L;
        UserResponseDTO responseDTO = new UserResponseDTO();
        when(service.getById(userId)).thenReturn(responseDTO);

        UserResponseDTO result = controller.getUserById(userId);

        assertEquals(responseDTO, result);
        verify(service, times(1)).getById(userId);
    }

    @Test
    void getAllUsersSuccessfully() {
        UserResponseDTO responseDTO = new UserResponseDTO();
        when(service.getAllUsers()).thenReturn(Collections.singletonList(responseDTO));

        List<UserResponseDTO> result = controller.getAllUsers();

        assertEquals(1, result.size());
        verify(service, times(1)).getAllUsers();
    }

    @Test
    void getUserNotificationsSuccessfully() {
        Long userId = 1L;
        when(service.getUserNotifications(userId)).thenReturn(Collections.singletonList(1L));

        List<Long> result = controller.getNotifications(userId);

        assertEquals(1, result.size());
        verify(service, times(1)).getUserNotifications(userId);
    }

    @Test
    void updateUserImageSuccessfully() {
        Long userId = 1L;
        String image = "imageData";
        UserResponseDTO responseDTO = new UserResponseDTO();
        when(service.updateUserImage(userId, image)).thenReturn(responseDTO);

        UserResponseDTO result = controller.updateUserImage(userId, image);

        assertEquals(responseDTO, result);
        verify(service, times(1)).updateUserImage(userId, image);
    }

    @Test
    void deleteNotificationSuccessfully() {
        Long userId = 1L;
        Long requirementId = 2L;

        doNothing().when(service).deleteNotification(userId, requirementId);

        controller.deleteNotification(userId, requirementId);

        verify(service, times(1)).deleteNotification(userId, requirementId);
    }
}
