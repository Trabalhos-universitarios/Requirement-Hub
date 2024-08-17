package com.br.requirementhub.services;

import com.br.requirementhub.dtos.projectArtifact.ProjectArtifactRequestDTO;
import com.br.requirementhub.dtos.projectArtifact.ProjectArtifactResponseDTO;
import com.br.requirementhub.entity.Project;
import com.br.requirementhub.entity.ProjectArtifact;
import com.br.requirementhub.exceptions.ProjectArtifactAlreadyExistException;
import com.br.requirementhub.repository.ProjectArtifactRepository;
import com.br.requirementhub.repository.ProjectRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectArtifactService {

    private final ProjectArtifactRepository artifactRepository;
    private final ProjectRepository projectRepository;

    public List<ProjectArtifactResponseDTO> findAll() {
        return artifactRepository.findAll().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public ProjectArtifactResponseDTO findById(Long id) {
        return artifactRepository.findById(id)
                .map(this::convertToResponseDTO)
                .orElse(null);
    }

    public List<ProjectArtifactResponseDTO> findArtifactsByProjectId(Long projectId) {
        return artifactRepository.findByProjectId(projectId).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public ProjectArtifactResponseDTO create(ProjectArtifactRequestDTO dto) throws IOException {
        this.verifyAlreadyExistsArtifact(dto);
        ProjectArtifact artifact = convertToEntity(dto);
        artifact = artifactRepository.save(artifact);
        return convertToResponseDTO(artifact);
    }

    private void verifyAlreadyExistsArtifact(ProjectArtifactRequestDTO request) throws IOException {
        final Project project = convertToEntity(request).getProjectId();

        Optional<ProjectArtifact> findProject = artifactRepository
                .findByFileNameAndProjectId(request.getFileName(), project);

        if (findProject.isPresent()) {
            throw new ProjectArtifactAlreadyExistException("This requirement already exists!");
        }
    }

    public void deleteById(Long id) {
        artifactRepository.deleteById(id);
    }

    public void deleteArtifactsByProjectId(Long projectId) {
        artifactRepository.deleteByProjectId(projectId);
    }

    private ProjectArtifactResponseDTO convertToResponseDTO(ProjectArtifact artifact) {
        return new ProjectArtifactResponseDTO(
                artifact.getId(),
                artifact.getName(),
                artifact.getFileName(),
                artifact.getType(),
                artifact.getSize(),
                artifact.getContent(),
                artifact.getProjectId().getId()
        );
    }

    private ProjectArtifact convertToEntity(ProjectArtifactRequestDTO dto) throws IOException {
        ProjectArtifact artifact = new ProjectArtifact();
        artifact.setName(dto.getName());
        artifact.setFileName(dto.getFileName());
        artifact.setType(dto.getType());
        artifact.setSize(dto.getSize());
        artifact.setContent(dto.getContent());

        Project project = projectRepository.findById(dto.getProjectId())
                .orElseThrow(() -> new ArithmeticException("Project not found"));
        artifact.setProjectId(project);

        return artifact;
    }
}
