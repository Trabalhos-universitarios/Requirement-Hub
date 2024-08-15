package com.br.requirementhub.services;

import com.br.requirementhub.dtos.projectArtifact.ProjectArtifactRequestDTO;
import com.br.requirementhub.dtos.projectArtifact.ProjectArtifactResponseDTO;
import com.br.requirementhub.entity.Project;
import com.br.requirementhub.entity.ProjectArtifact;
import com.br.requirementhub.repository.ProjectArtifactRepository;
import com.br.requirementhub.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectArtifactService {

    private final ProjectArtifactRepository repository;
    private final ProjectRepository projectRepository;

    public List<ProjectArtifactResponseDTO> findAll() {
        return repository.findAll().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public ProjectArtifactResponseDTO findById(Long id) {
        return repository.findById(id)
                .map(this::convertToResponseDTO)
                .orElse(null);
    }

    public ProjectArtifactResponseDTO save(ProjectArtifactRequestDTO dto) throws IOException {
        ProjectArtifact artifact = convertToEntity(dto);
        artifact = repository.save(artifact);
        return convertToResponseDTO(artifact);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    private ProjectArtifactResponseDTO convertToResponseDTO(ProjectArtifact artifact) {
        return new ProjectArtifactResponseDTO(
                artifact.getId(),
                artifact.getName(),
                artifact.getType(),
                artifact.getArtifact_file(),
                artifact.getProject().getId()
        );
    }

    private ProjectArtifact convertToEntity(ProjectArtifactRequestDTO dto) throws IOException {
        ProjectArtifact artifact = new ProjectArtifact();
        artifact.setName(dto.getName());
        artifact.setType(dto.getType());
        artifact.setArtifact_file(dto.getArtifact_file().getBytes());
        Project project = projectRepository.findById(dto.getProjectId())
                .orElseThrow(() -> new ArithmeticException("Project not found"));
        artifact.setProject(project);
        return artifact;
    }
}

