package com.br.requirementhub.services;

import com.br.requirementhub.dtos.project.ProjectRequestDTO;
import com.br.requirementhub.dtos.project.ProjectResponseDTO;
import com.br.requirementhub.entity.Project;
import com.br.requirementhub.entity.Team;
import com.br.requirementhub.entity.User;
import com.br.requirementhub.enums.Role;
import com.br.requirementhub.repository.ProjectRepository;
import com.br.requirementhub.repository.TeamRepository;
import com.br.requirementhub.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;


    public ProjectResponseDTO create(ProjectRequestDTO requestDTO) throws IOException {
        Project project = convertToEntity(requestDTO);
        Project savedProject = projectRepository.save(project);

        List<Team> teams = savedProject.getTeams();
        for (Team team : teams) {
            team.setProject(savedProject);
        }
        projectRepository.save(savedProject);

        ProjectResponseDTO responseDTO = new ProjectResponseDTO();
        responseDTO.setId(savedProject.getId());
        responseDTO.setName(savedProject.getName());
        return responseDTO;
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
        dto.setDescription(project.getDescription());
        dto.setVersion(project.getVersion());
        dto.setCreationDate(project.getCreationDate());
        dto.setLastUpdate(project.getLastUpdate());

        //todo ---- O CODIGO COMENTADO NAO E PARA SER APAGADO
//        List<String> requirementAnalysts = teamRepository.findByProjectIdAndUserRole(project.getId(), Role.ANALISTA_DE_REQUISITOS)
//                .stream()
//                .map(Team::getUserName)
//                .collect(Collectors.toList());
//        dto.setRequirementAnalysts(requirementAnalysts);
//
//        List<String> businessAnalysts = teamRepository.findByProjectIdAndUserRole(project.getId(), Role.ANALISTA_DE_NEGOCIO)
//                .stream()
//                .map(Team::getUserName)
//                .collect(Collectors.toList());
//        dto.setBusinessAnalysts(businessAnalysts);
//
//        List<String> commonUsers = teamRepository.findByProjectIdAndUserRole(project.getId(), Role.USUARIO_COMUM)
//                .stream()
//                .map(Team::getUserName)
//                .collect(Collectors.toList());
//        dto.setCommonUsers(commonUsers);

//        dto.setRequirementIds(project.getRequirements() != null ? project.getRequirements().stream()
//                .map(requirement -> requirement.getId())
//                .collect(Collectors.toList()) : new ArrayList<>());

        return dto;
    }

    @Transactional
    public ProjectResponseDTO update(Long id, ProjectRequestDTO requestDTO) throws IOException {
        Optional<Project> existingProjectOpt = projectRepository.findById(id);
        if (existingProjectOpt.isPresent()) {
            Project existingProject = existingProjectOpt.get();
            existingProject.setName(requestDTO.getName());
            existingProject.setManager(requestDTO.getManager());
            existingProject.setStatus(requestDTO.getStatus());
            existingProject.setDescription(requestDTO.getDescription());
            existingProject.setVersion(requestDTO.getVersion());
            existingProject.setLastUpdate(new Date());
            existingProject.setDraft(requestDTO.isDraft());

            // Remover todas as equipes associadas ao projeto antes de adicionar novas
            teamRepository.deleteByProjectId(id);

            // Adicionar novas equipes
            List<Team> newTeams = new ArrayList<>();
            newTeams.addAll(requestDTO.getRequirementAnalysts().stream()
                    .map(userId -> createTeam(userId, existingProject))
                    .collect(Collectors.toList()));
            newTeams.addAll(requestDTO.getBusinessAnalysts().stream()
                    .map(userId -> createTeam(userId, existingProject))
                    .collect(Collectors.toList()));
            newTeams.addAll(requestDTO.getCommonUsers().stream()
                    .map(userId -> createTeam(userId, existingProject))
                    .collect(Collectors.toList()));

            // Adicionar novas equipes à lista existente
            existingProject.setTeams(newTeams);

            Project updatedProject = projectRepository.save(existingProject);

            return convertToDTO(updatedProject);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found with id: " + id);
        }
    }

    public void deleteById(Long id) {
        if (projectRepository.existsById(id)) {
            projectRepository.deleteById(id);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found with id: " + id);
        }
    }

    private Project convertToEntity(ProjectRequestDTO dto) throws IOException {
        Project project = new Project();
        project.setName(dto.getName());
        project.setManager(dto.getManager());
        project.setStatus(dto.getStatus());
        project.setDescription(dto.getDescription());
        project.setVersion(dto.getVersion());
        project.setCreationDate(new Date());
        project.setDraft(dto.isDraft());

        List<Team> teams = dto.getRequirementAnalysts().stream()
                .map(userId -> createTeam(userId, project))
                .collect(Collectors.toList());
        teams.addAll(dto.getBusinessAnalysts().stream()
                .map(userId -> createTeam(userId, project))
                .collect(Collectors.toList()));
        teams.addAll(dto.getCommonUsers().stream()
                .map(userId -> createTeam(userId, project))
                .collect(Collectors.toList()));

        project.setTeams(teams);

        return project;
    }

    private Team createTeam(Long userId, Project project) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id: " + userId));
        Team team = new Team();
        team.setUser(user);
        team.setProject(project);
        team.setUserName(user.getName());
        return team;
    }
}