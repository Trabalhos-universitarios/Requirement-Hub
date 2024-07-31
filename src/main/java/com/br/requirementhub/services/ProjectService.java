package com.br.requirementhub.services;

import com.br.requirementhub.dtos.ProjectRequestDTO;
import com.br.requirementhub.dtos.ProjectResponseDTO;
import com.br.requirementhub.model.Project;
import com.br.requirementhub.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;

    public ProjectResponseDTO create(ProjectRequestDTO requestDTO) {
        Project project = convertToEntity(requestDTO);
        Project savedProject = projectRepository.save(project);
        return convertToDTO(savedProject);
    }

    public List<ProjectResponseDTO> list() {
        List<Project> projects = projectRepository.findAll();
        return projects.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ProjectResponseDTO findById(Long id) {
        Optional<Project> project = projectRepository.findById(id);
        if (project.isPresent()) {
            return convertToDTO(project.get());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found with id: " + id);
        }
    }

    private ProjectResponseDTO convertToDTO(Project project) {
        ProjectResponseDTO dto = new ProjectResponseDTO();
        dto.setId(project.getId());
        dto.setName(project.getName());
        dto.setManager(project.getManager());
        dto.setStatus(project.getStatus());
        dto.setTypeProject(project.getTypeProject());
        dto.setRequirementsAnalyst(project.getRequirementsAnalyst());
        dto.setBusinessAnalyst(project.getBusinessAnalyst());
        dto.setCommonUser(project.getCommonUser());
        dto.setDescription(project.getDescription());
        dto.setVersion(project.getVersion());
        dto.setCreationDate(project.getCreationDate());
        dto.setLastUpdate(project.getLastUpdate());
        return dto;
    }

    private Project convertToEntity(ProjectRequestDTO dto) {
        Project project = new Project();
        project.setName(dto.getName());
        project.setManager(dto.getManager());
        project.setStatus(dto.getStatus());
        project.setTypeProject(dto.getTypeProject());
        project.setRequirementsAnalyst(dto.getRequirementsAnalyst());
        project.setBusinessAnalyst(dto.getBusinessAnalyst());
        project.setCommonUser(dto.getCommonUser());
        project.setDescription(dto.getDescription());
        project.setVersion(dto.getVersion());
        project.setCreationDate(dto.getCreationDate());
        project.setLastUpdate(dto.getLastUpdate());
        return project;
    }
}
