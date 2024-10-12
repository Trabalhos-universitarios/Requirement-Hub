package com.br.requirementhub.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.br.requirementhub.dtos.comments.CommentsRequestDto;
import com.br.requirementhub.dtos.requirement.RequirementRequestDTO;
import com.br.requirementhub.dtos.requirement.RequirementResponseDTO;
import com.br.requirementhub.dtos.requirement.RequirementUpdateRequestDTO;
import com.br.requirementhub.dtos.requirementArtifact.RequirementArtifactResponseDTO;
import com.br.requirementhub.entity.RequirementArtifact;
import com.br.requirementhub.repository.RequirementArtifactRepository;
import com.br.requirementhub.services.RequirementArtifactService;
import com.br.requirementhub.services.RequirementService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

public class RequirementControllerTest {

    @Mock
    private RequirementService service;

    @Mock
    private RequirementArtifactService artifactService;

    @Mock
    private RequirementArtifactRepository requirementArtifactRepository;

    @InjectMocks
    private RequirementController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllRequirementsSuccessfully() {
        List<RequirementResponseDTO> responseDTOs = Collections.singletonList(new RequirementResponseDTO());
        when(service.getAllRequirements()).thenReturn(responseDTOs);

        ResponseEntity<List<RequirementResponseDTO>> result = controller.getAllRequirements();

        assertEquals(ResponseEntity.ok(responseDTOs), result);
        verify(service, times(1)).getAllRequirements();
    }

    @Test
    void getRequirementByIdSuccessfully() {
        Long id = 1L;
        RequirementResponseDTO responseDTO = new RequirementResponseDTO();
        when(service.getRequirementById(id)).thenReturn(responseDTO);

        ResponseEntity<RequirementResponseDTO> result = controller.getRequirementById(id);

        assertEquals(ResponseEntity.ok(responseDTO), result);
        verify(service, times(1)).getRequirementById(id);
    }

    @Test
    void getRequirementByIdNotFound() {
        Long id = 1L;
        when(service.getRequirementById(id)).thenReturn(null);

        ResponseEntity<RequirementResponseDTO> result = controller.getRequirementById(id);

        assertEquals(ResponseEntity.notFound().build(), result);
        verify(service, times(1)).getRequirementById(id);
    }

    @Test
    void getRequirementDataToUpdateSuccessfully() {
        Long id = 1L;
        List<RequirementResponseDTO> responseDTO = new ArrayList<>();
        when(service.getRequirementDataToUpdate(id)).thenReturn(responseDTO);

        ResponseEntity<List<RequirementResponseDTO>> result = controller.getRequirementDataToUpdate(id);

        assertEquals(ResponseEntity.ok(responseDTO), result);
        verify(service, times(1)).getRequirementDataToUpdate(id);
    }

    @Test
    void getRequirementDataToUpdateNotFound() {
        Long id = 1L;
        when(service.getRequirementDataToUpdate(id)).thenReturn(null);

        ResponseEntity<List<RequirementResponseDTO>> result = controller.getRequirementDataToUpdate(id);

        assertEquals(ResponseEntity.notFound().build(), result);
        verify(service, times(1)).getRequirementDataToUpdate(id);
    }

    @Test
    void getRequirementByIdentifierAndProjectRelatedSuccessfully() {
        String identifier = "RF-001";
        Long projectId = 1L;

        RequirementResponseDTO responseDTO = new RequirementResponseDTO();
        responseDTO.setIdentifier(identifier);
        when(service.getRequirementByIdentifierAndProjectRelated(projectId)).thenReturn(List.of(responseDTO));

        ResponseEntity<RequirementResponseDTO> result = controller.getRequirementByIdentifierAndProjectRelated(identifier, projectId);
        assertEquals(ResponseEntity.ok(responseDTO), result);
        verify(service, times(1)).getRequirementByIdentifierAndProjectRelated(projectId);
    }


    @Test
    void getRequirementByIdentifierAndProjectRelatedNotFound() {
        String identifier = "identifier";
        Long projectId = 1L;
        when(service.getRequirementByIdentifierAndProjectRelated(projectId)).thenReturn(Collections.emptyList());

        ResponseEntity<RequirementResponseDTO> result = controller.getRequirementByIdentifierAndProjectRelated(identifier, projectId);

        assertEquals(ResponseEntity.notFound().build(), result);
        verify(service, times(1)).getRequirementByIdentifierAndProjectRelated(projectId);
    }

    @Test
    void createRequirementSuccessfully() {
        RequirementRequestDTO requestDTO = new RequirementRequestDTO();
        RequirementResponseDTO responseDTO = new RequirementResponseDTO();
        when(service.createRequirement(requestDTO)).thenReturn(responseDTO);

        ResponseEntity<RequirementResponseDTO> result = controller.createRequirement(requestDTO);

        assertEquals(ResponseEntity.status(201).body(responseDTO), result);
        verify(service, times(1)).createRequirement(requestDTO);
    }

    @Test
    void updateRequirementNotFound() {
        Long id = 1L;
        RequirementUpdateRequestDTO requestDTO = new RequirementUpdateRequestDTO();
        when(service.updateRequirement(id, requestDTO)).thenReturn(null);

        ResponseEntity<RequirementResponseDTO> result = controller.updateRequirement(id, requestDTO);

        assertEquals(ResponseEntity.notFound().build(), result);
        verify(service, times(1)).updateRequirement(id, requestDTO);
    }

    @Test
    void deleteRequirementSuccessfully() {
        Long id = 1L;
        doNothing().when(service).deleteRequirement(id);

        ResponseEntity<Void> result = controller.deleteRequirement(id);

        assertEquals(ResponseEntity.noContent().build(), result);
        verify(service, times(1)).deleteRequirement(id);
    }

    @Test
    void sendToApprovalFlowSuccessfully() {
        RequirementRequestDTO requestDTO = new RequirementRequestDTO();
        RequirementResponseDTO responseDTO = new RequirementResponseDTO();
        when(service.sendToApprovalFlow(requestDTO)).thenReturn(responseDTO);

        ResponseEntity<RequirementResponseDTO> result = controller.sendToApprovalFlow(requestDTO);

        assertEquals(ResponseEntity.status(201).body(responseDTO), result);
        verify(service, times(1)).sendToApprovalFlow(requestDTO);
    }

    @Test
    void listRequirementsByProjectIdSuccessfully() {
        Long id = 1L;
        List<RequirementResponseDTO> requirements = List.of(new RequirementResponseDTO());
        when(service.listByProjectId(id)).thenReturn(requirements);

        ResponseEntity<List<RequirementResponseDTO>> result = controller.listRequirementsByProjectId(id);

        assertEquals(ResponseEntity.ok(requirements), result);
        verify(service, times(1)).listByProjectId(id);
    }

    @Test
    void listRequirementsByProjectIdNotFound() {
        Long id = 1L;
        when(service.listByProjectId(id)).thenReturn(Collections.emptyList());

        ResponseEntity<List<RequirementResponseDTO>> result = controller.listRequirementsByProjectId(id);

        assertEquals(ResponseEntity.ok(Collections.emptyList()), result);
        verify(service, times(1)).listByProjectId(id);
    }

    @Test
    void getRequirementsByIdsSuccessfully() {
        List<Long> ids = List.of(1L, 2L);
        List<RequirementResponseDTO> requirements = List.of(new RequirementResponseDTO());
        when(service.getRequirementsByIds(ids)).thenReturn(requirements);

        List<RequirementResponseDTO> result = controller.getRequirementsByIds(ids);

        assertEquals(requirements, result);
        verify(service, times(1)).getRequirementsByIds(ids);
    }

    @Test
    void getRequirementsByIdsEmpty() {
        List<Long> ids = List.of();
        List<RequirementResponseDTO> requirements = Collections.emptyList();
        when(service.getRequirementsByIds(ids)).thenReturn(requirements);

        List<RequirementResponseDTO> result = controller.getRequirementsByIds(ids);

        assertEquals(requirements, result);
        verify(service, times(1)).getRequirementsByIds(ids);
    }

    @Test
    void getAllRequirementResponsiblesEmpty() {
        List<Object[]> responsibles = Collections.emptyList();
        when(service.getAllRequirementResponsible()).thenReturn(responsibles);

        ResponseEntity<List<Object[]>> result = controller.getAllRequirementResponsibles();

        assertEquals(ResponseEntity.ok(responsibles), result);
        verify(service, times(1)).getAllRequirementResponsible();
    }

    @Test
    void sendToApprovalRequirementSuccessfully() {
        Long id = 1L;
        RequirementResponseDTO responseDTO = new RequirementResponseDTO();
        when(service.sendToApprovalFlowRequirementId(id)).thenReturn(responseDTO);

        ResponseEntity<RequirementResponseDTO> result = controller.sendToApprovalRequirement(id);

        assertEquals(ResponseEntity.ok(responseDTO), result);
        verify(service, times(1)).sendToApprovalFlowRequirementId(id);
    }

    @Test
    void blockedRequirementSuccessfully() {
        Long id = 1L;
        RequirementResponseDTO responseDTO = new RequirementResponseDTO();
        when(service.blockedRequirementId(id)).thenReturn(responseDTO);

        ResponseEntity<RequirementResponseDTO> result = controller.blockedRequirement(id);

        assertEquals(ResponseEntity.ok(responseDTO), result);
        verify(service, times(1)).blockedRequirementId(id);
    }

    @Test
    void refuseRequirementSuccessfully() {
        Long id = 1L;
        CommentsRequestDto commentsRequestDto = new CommentsRequestDto();
        RequirementResponseDTO responseDTO = new RequirementResponseDTO();
        when(service.refuseRequirement(id, commentsRequestDto)).thenReturn(responseDTO);

        ResponseEntity<RequirementResponseDTO> result = controller.refuseRequirement(id, commentsRequestDto);

        assertEquals(ResponseEntity.ok(responseDTO), result);
        verify(service, times(1)).refuseRequirement(id, commentsRequestDto);
    }

    @Test
    void approveRequirementSuccessfully() {
        Long id = 1L;
        RequirementResponseDTO responseDTO = new RequirementResponseDTO();
        when(service.approveRequirement(id)).thenReturn(responseDTO);

        ResponseEntity<RequirementResponseDTO> result = controller.approveRequirement(id);

        assertEquals(ResponseEntity.ok(responseDTO), result);
        verify(service, times(1)).approveRequirement(id);
    }

    @Test
    void getRequirementByProjectRelatedSuccessfully() {
        Long id = 1L;
        List<RequirementResponseDTO> requirements = List.of(new RequirementResponseDTO());
        when(service.getRequirementByIdentifierAndProjectRelated(id)).thenReturn(requirements);

        ResponseEntity<List<RequirementResponseDTO>> result = controller.getRequirementByProjectRelated(id);

        assertEquals(ResponseEntity.ok(requirements), result);
        verify(service, times(1)).getRequirementByIdentifierAndProjectRelated(id);
    }

    @Test
    void getRequirementByProjectRelatedNotFound() {
        Long id = 1L;
        when(service.getRequirementByIdentifierAndProjectRelated(id)).thenReturn(null);

        ResponseEntity<List<RequirementResponseDTO>> result = controller.getRequirementByProjectRelated(id);

        assertEquals(ResponseEntity.notFound().build(), result);
        verify(service, times(1)).getRequirementByIdentifierAndProjectRelated(id);
    }

    @Test
    void getRequirementArtifactByIdentifierAndProjectRelatedSuccessfully() {
        String identifier = "artifact-001";
        Long projectId = 1L;
        RequirementResponseDTO requirement = new RequirementResponseDTO();
        requirement.setId(1L);
        RequirementArtifactResponseDTO artifact = new RequirementArtifactResponseDTO();
        artifact.setIdentifier(identifier);
        RequirementArtifact requirementArtifact = new RequirementArtifact();
        when(service.getRequirementByIdentifierAndProjectRelated(projectId)).thenReturn(List.of(requirement));
        when(artifactService.findArtifactsByRequirementId(requirement.getId())).thenReturn(List.of(artifact));
        when(requirementArtifactRepository.findByIdentifierArtifact(identifier)).thenReturn(requirementArtifact);
        when(artifactService.convertToResponseDTO(requirementArtifact)).thenReturn(artifact);

        ResponseEntity<RequirementArtifactResponseDTO> result = controller.getRequirementArtifactByIdentifierAndProjectRelated(identifier, projectId);

        assertEquals(ResponseEntity.ok(artifact), result);
        verify(service, times(1)).getRequirementByIdentifierAndProjectRelated(projectId);
        verify(artifactService, times(1)).findArtifactsByRequirementId(requirement.getId());
        verify(requirementArtifactRepository, times(1)).findByIdentifierArtifact(identifier);
        verify(artifactService, times(1)).convertToResponseDTO(requirementArtifact);
    }

    @Test
    void getRequirementArtifactByIdentifierAndProjectRelatedNotFound() {
        String identifier = "artifact-001";
        Long projectId = 1L;
        RequirementResponseDTO requirement = new RequirementResponseDTO();
        requirement.setId(1L);
        when(service.getRequirementByIdentifierAndProjectRelated(projectId)).thenReturn(List.of(requirement));
        when(artifactService.findArtifactsByRequirementId(requirement.getId())).thenReturn(Collections.emptyList());

        ResponseEntity<RequirementArtifactResponseDTO> result = controller.getRequirementArtifactByIdentifierAndProjectRelated(identifier, projectId);

        assertEquals(ResponseEntity.notFound().build(), result);
        verify(service, times(1)).getRequirementByIdentifierAndProjectRelated(projectId);
        verify(artifactService, times(1)).findArtifactsByRequirementId(requirement.getId());
    }
}
