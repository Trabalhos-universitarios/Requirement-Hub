package com.br.requirementhub.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.br.requirementhub.dtos.project.ProjectRequestDTO;
import com.br.requirementhub.dtos.project.ProjectResponseDTO;
import com.br.requirementhub.services.ProjectService;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

class ProjectControllerTest {

    @Mock
    private ProjectService service;

    @InjectMocks
    private ProjectController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createProjectSuccessfully() throws IOException {
        ProjectRequestDTO request = new ProjectRequestDTO();
        ProjectResponseDTO response = new ProjectResponseDTO();
        when(service.create(any(ProjectRequestDTO.class))).thenReturn(response);

        ResponseEntity<ProjectResponseDTO> result = controller.create(request);

        assertEquals(ResponseEntity.status(201).body(response), result);
        verify(service, times(1)).create(any(ProjectRequestDTO.class));
    }

    @Test
    void getAllProjectsSuccessfully() {
        List<ProjectResponseDTO> projects = Collections.singletonList(new ProjectResponseDTO());
        when(service.list()).thenReturn(projects);

        ResponseEntity<List<ProjectResponseDTO>> result = controller.list();

        assertEquals(ResponseEntity.ok(projects), result);
        verify(service, times(1)).list();
    }

    @Test
    void getProjectsByUserIdSuccessfully() {
        Long userId = 1L;
        List<ProjectResponseDTO> projects = Collections.singletonList(new ProjectResponseDTO());
        when(service.listProjectsByUserId(userId)).thenReturn(projects);

        ResponseEntity<List<ProjectResponseDTO>> result = controller.list(userId);

        assertEquals(ResponseEntity.ok(projects), result);
        verify(service, times(1)).listProjectsByUserId(userId);
    }

    @Test
    void getProjectsByManagerIdSuccessfully() {
        Long managerId = 1L;
        List<ProjectResponseDTO> projects = Collections.singletonList(new ProjectResponseDTO());
        when(service.listProjectsByManagerId(managerId)).thenReturn(projects);

        ResponseEntity<List<ProjectResponseDTO>> result = controller.lists(managerId);

        assertEquals(ResponseEntity.ok(projects), result);
        verify(service, times(1)).listProjectsByManagerId(managerId);
    }

    @Test
    void getProjectByIdSuccessfully() {
        Long id = 1L;
        ProjectResponseDTO project = new ProjectResponseDTO();
        when(service.findById(id)).thenReturn(project);

        ResponseEntity<ProjectResponseDTO> result = controller.find(id);

        assertEquals(ResponseEntity.ok(project), result);
        verify(service, times(1)).findById(id);
    }

    @Test
    void updateProjectSuccessfully() throws IOException {
        Long id = 1L;
        ProjectRequestDTO request = new ProjectRequestDTO();
        ProjectResponseDTO updatedProject = new ProjectResponseDTO();
        when(service.update(id, request)).thenReturn(updatedProject);

        ResponseEntity<ProjectResponseDTO> result = controller.update(id, request);

        assertEquals(ResponseEntity.ok(updatedProject), result);
        verify(service, times(1)).update(id, request);
    }

    @Test
    void deleteProjectSuccessfully() {
        Long id = 1L;
        doNothing().when(service).deleteById(id);

        ResponseEntity<Void> result = controller.delete(id);

        assertEquals(ResponseEntity.noContent().build(), result);
        verify(service, times(1)).deleteById(id);
    }
}
