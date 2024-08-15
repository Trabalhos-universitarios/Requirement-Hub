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

    // Retorna todos os artefatos
    public List<ProjectArtifactResponseDTO> findAll() {
        return repository.findAll().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    // Retorna um artefato específico pelo ID
    public ProjectArtifactResponseDTO findById(Long id) {
        return repository.findById(id)
                .map(this::convertToResponseDTO)
                .orElse(null);
    }

    // Retorna todos os artefatos relacionados a um projectId específico
    public List<ProjectArtifactResponseDTO> findArtifactsByProjectId(Long projectId) {
        return repository.findByProjectId(projectId).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    // Salva um novo artefato ou atualiza um existente
    public ProjectArtifactResponseDTO save(ProjectArtifactRequestDTO dto) throws IOException {
        ProjectArtifact artifact = convertToEntity(dto);
        artifact = repository.save(artifact);
        return convertToResponseDTO(artifact);
    }

    // Deleta um artefato pelo ID
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    // Deleta todos os artefatos relacionados a um projectId específico
    public void deleteArtifactsByProjectId(Long projectId) {
        repository.deleteByProjectId(projectId);
    }

    // Converte a entidade ProjectArtifact para ProjectArtifactResponseDTO
    private ProjectArtifactResponseDTO convertToResponseDTO(ProjectArtifact artifact) {
        return new ProjectArtifactResponseDTO(
                artifact.getId(),
                artifact.getName(),
                artifact.getFileName(),
                artifact.getType(),
                artifact.getSize(),
                artifact.getContent(),
                artifact.getProject().getId()
        );
    }

    // Converte ProjectArtifactRequestDTO para a entidade ProjectArtifact
    private ProjectArtifact convertToEntity(ProjectArtifactRequestDTO dto) throws IOException {
        ProjectArtifact artifact = new ProjectArtifact();
        artifact.setName(dto.getName());
        artifact.setFileName(dto.getFileName());
        artifact.setType(dto.getType());
        artifact.setSize(dto.getSize());
        artifact.setContent(dto.getContent());

        Project project = projectRepository.findById(dto.getProjectId())
                .orElseThrow(() -> new ArithmeticException("Project not found"));
        artifact.setProject(project);

        return artifact;
    }
}
