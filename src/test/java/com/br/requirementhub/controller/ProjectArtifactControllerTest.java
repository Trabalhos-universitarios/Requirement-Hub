package com.br.requirementhub.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.br.requirementhub.dtos.projectArtifact.ProjectArtifactRequestDTO;
import com.br.requirementhub.dtos.projectArtifact.ProjectArtifactResponseDTO;
import com.br.requirementhub.services.ProjectArtifactService;
import com.br.requirementhub.utils.DecodeBase64;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

class ProjectArtifactControllerTest {

    @Mock
    private ProjectArtifactService service;

    @InjectMocks
    private ProjectArtifactController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllArtifactsSuccessfully() {
        List<ProjectArtifactResponseDTO> artifacts = Collections.singletonList(new ProjectArtifactResponseDTO());
        when(service.findAll()).thenReturn(artifacts);

        List<ProjectArtifactResponseDTO> result = controller.getAllArtifacts();

        assertEquals(artifacts, result);
        verify(service, times(1)).findAll();
    }

    @Test
    void getArtifactByIdSuccessfully() {
        Long id = 1L;
        ProjectArtifactResponseDTO artifact = new ProjectArtifactResponseDTO();
        when(service.findById(id)).thenReturn(artifact);

        ProjectArtifactResponseDTO result = controller.getArtifactById(id);

        assertEquals(artifact, result);
        verify(service, times(1)).findById(id);
    }

    @Test
    void createArtifactSuccessfully() throws IOException {
        ProjectArtifactRequestDTO request = new ProjectArtifactRequestDTO();
        request.setContentBase64("data:text/plain;base64,SGVsbG8gd29ybGQ=");
        ProjectArtifactResponseDTO response = new ProjectArtifactResponseDTO();
        when(service.create(any(ProjectArtifactRequestDTO.class))).thenReturn(response);

        ResponseEntity<ProjectArtifactResponseDTO> result = controller.create(request);

        assertEquals(ResponseEntity.status(201).body(response), result);
        verify(service, times(1)).create(any(ProjectArtifactRequestDTO.class));
    }

    @Test
    void deleteArtifactSuccessfully() {
        Long id = 1L;
        doNothing().when(service).deleteById(id);

        controller.deleteArtifact(id);

        verify(service, times(1)).deleteById(id);
    }

    @Test
    void downloadFileSuccessfully() {
        Long id = 1L;
        ProjectArtifactResponseDTO artifact = new ProjectArtifactResponseDTO();
        artifact.setFileName("file.txt");
        artifact.setType("text/plain");
        artifact.setContent("Hello world".getBytes());
        when(service.findById(id)).thenReturn(artifact);

        ResponseEntity<byte[]> result = controller.downloadFile(id);

        assertEquals("attachment; filename=\"file.txt\"", result.getHeaders().getContentDisposition().toString());
        assertEquals("text/plain", result.getHeaders().getContentType().toString());
        assertEquals(artifact.getContent(), result.getBody());
        verify(service, times(1)).findById(id);
    }

    @Test
    void getImageBase64Successfully() {
        Long id = 1L;
        ProjectArtifactResponseDTO artifact = new ProjectArtifactResponseDTO();
        artifact.setType("image/png");
        artifact.setContent("imageContent".getBytes());
        when(service.findById(id)).thenReturn(artifact);

        ResponseEntity<String> result = controller.getImageBase64(id);

        String expectedBase64 = "data:image/png;base64," + DecodeBase64.encodeToString(artifact.getContent());
        assertEquals(expectedBase64, result.getBody());
        verify(service, times(1)).findById(id);
    }

    @Test
    void getArtifactsByProjectIdSuccessfully() {
        Long projectId = 1L;
        List<ProjectArtifactResponseDTO> artifacts = Collections.singletonList(new ProjectArtifactResponseDTO());
        when(service.findArtifactsByProjectId(projectId)).thenReturn(artifacts);

        ResponseEntity<List<ProjectArtifactResponseDTO>> result = controller.getArtifactsByProjectId(projectId);

        assertEquals(ResponseEntity.ok(artifacts), result);
        verify(service, times(1)).findArtifactsByProjectId(projectId);
    }

    @Test
    void deleteArtifactsByProjectIdSuccessfully() {
        Long projectId = 1L;
        doNothing().when(service).deleteArtifactsByProjectId(projectId);

        ResponseEntity<Void> result = controller.deleteArtifactsByProjectId(projectId);

        assertEquals(ResponseEntity.noContent().build(), result);
        verify(service, times(1)).deleteArtifactsByProjectId(projectId);
    }
}
