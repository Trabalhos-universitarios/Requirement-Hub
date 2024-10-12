package com.br.requirementhub.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.br.requirementhub.dtos.requirementArtifact.RequirementArtifactRequestDTO;
import com.br.requirementhub.dtos.requirementArtifact.RequirementArtifactResponseDTO;
import com.br.requirementhub.services.RequirementArtifactService;
import com.br.requirementhub.services.RequirementService;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

class RequirementArtifactControllerTest {

    @Mock
    private RequirementArtifactService service;

    @Mock
    private RequirementService requirementService;

    @InjectMocks
    private RequirementArtifactController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createArtifactSuccessfully() throws IOException {
        RequirementArtifactRequestDTO requestDTO = new RequirementArtifactRequestDTO();
        RequirementArtifactResponseDTO responseDTO = new RequirementArtifactResponseDTO();
        when(service.save(any(RequirementArtifactRequestDTO.class))).thenReturn(responseDTO);

        RequirementArtifactResponseDTO result = controller.create(requestDTO);

        assertEquals(responseDTO, result);
        verify(service, times(1)).save(requestDTO);
    }

    @Test
    void getAllArtifactsSuccessfully() {
        List<RequirementArtifactResponseDTO> responseDTOs = Collections.singletonList(new RequirementArtifactResponseDTO());
        when(service.findAll()).thenReturn(responseDTOs);

        List<RequirementArtifactResponseDTO> result = controller.getAll();

        assertEquals(responseDTOs, result);
        verify(service, times(1)).findAll();
    }

    @Test
    void getArtifactByIdSuccessfully() {
        Long id = 1L;
        RequirementArtifactResponseDTO responseDTO = new RequirementArtifactResponseDTO();
        when(service.findById(id)).thenReturn(responseDTO);

        RequirementArtifactResponseDTO result = controller.getById(id);

        assertEquals(responseDTO, result);
        verify(service, times(1)).findById(id);
    }

    @Test
    void getArtifactsByRequirementIdSuccessfully() {
        Long requirementId = 1L;
        List<RequirementArtifactResponseDTO> responseDTOs = Collections.singletonList(new RequirementArtifactResponseDTO());
        when(service.findArtifactsByRequirementId(requirementId)).thenReturn(responseDTOs);

        ResponseEntity<List<RequirementArtifactResponseDTO>> result = controller.getArtifactsByRequirementId(requirementId);

        assertEquals(ResponseEntity.ok(responseDTOs), result);
        verify(service, times(1)).findArtifactsByRequirementId(requirementId);
    }

    @Test
    void getArtifactsByProjectIdSuccessfully() {
        Long projectId = 1L;
        List<RequirementArtifactResponseDTO> responseDTOs = Collections.singletonList(new RequirementArtifactResponseDTO());
        when(requirementService.getArtifactsByProjectId(projectId)).thenReturn(responseDTOs);

        ResponseEntity<List<RequirementArtifactResponseDTO>> result = controller.getArtifactsByProjectId(projectId);

        assertEquals(ResponseEntity.ok(responseDTOs), result);
        verify(requirementService, times(1)).getArtifactsByProjectId(projectId);
    }

    @Test
    void updateArtifactSuccessfully() throws IOException {
        Long id = 1L;
        RequirementArtifactRequestDTO requestDTO = new RequirementArtifactRequestDTO();
        RequirementArtifactResponseDTO responseDTO = new RequirementArtifactResponseDTO();
        when(service.update(id, requestDTO)).thenReturn(responseDTO);

        ResponseEntity<RequirementArtifactResponseDTO> result = controller.update(id, requestDTO);

        assertEquals(ResponseEntity.ok(responseDTO), result);
        verify(service, times(1)).update(id, requestDTO);
    }

    @Test
    void deleteArtifactSuccessfully() {
        Long id = 1L;
        doNothing().when(service).deleteById(id);

        controller.deleteArtifact(id);

        verify(service, times(1)).deleteById(id);
    }
}
