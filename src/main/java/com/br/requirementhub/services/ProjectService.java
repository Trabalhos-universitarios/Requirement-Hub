package com.br.requirementhub.services;

import com.br.requirementhub.dtos.project.ProjectRequestDTO;
import com.br.requirementhub.dtos.project.ProjectResponseDTO;
import com.br.requirementhub.entity.Project;
import com.br.requirementhub.entity.Team;
import com.br.requirementhub.entity.User;
import com.br.requirementhub.enums.Role;
import com.br.requirementhub.exceptions.ProjectAlreadyExistException;
import com.br.requirementhub.repository.ProjectRepository;
import com.br.requirementhub.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final TeamService teamService;
    private final UserService userService;
    private final ProjectArtifactService projectArtifactService;


    public ProjectResponseDTO create(ProjectRequestDTO requestDTO) throws IOException {
        verifyAlreadyExistsProject(requestDTO);
        Project project = convertToEntity(requestDTO);
        Project savedProject = projectRepository.save(project);

        savedProject.getTeams().forEach(team -> team.setProject(savedProject));
        teamService.saveAllTeams(savedProject.getTeams());

        ProjectResponseDTO responseDTO = new ProjectResponseDTO();
        responseDTO.setId(savedProject.getId());
        responseDTO.setName(savedProject.getName());
        return responseDTO;
    }


    private void verifyAlreadyExistsProject(ProjectRequestDTO request) {
        Optional<Project> findProject = projectRepository
                .findByNameAndManager(request.getName(), request.getManager());

        if (findProject.isPresent()) {
            throw new ProjectAlreadyExistException("This project already exists!");
        }
    }

    public List<ProjectResponseDTO> listProjectsByUserId(Long userId) {
        Role userRole = userService.findUserRoleById(userId);

        List<Project> projects;
        if (userRole == Role.ADMIN) {
            projects = projectRepository.findAll();
        } else if (userRole == Role.GERENTE_DE_PROJETOS) {
            projects = projectRepository.findByManager(userId.toString());
        }
        else {
            List<Long> projectIds = teamService.findProjectIdsByUserId(userId);
            projects = projectRepository.findAllById(projectIds);
        }

        return projects.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ProjectResponseDTO> listProjectsByManagerId(Long managerId) {
        Role userRole = userService.findUserRoleById(managerId);
        String managerIdStr = String.valueOf(managerId);

        List<Project> projects;
        if (userRole == Role.GERENTE_DE_PROJETOS) {
            projects = projectRepository.findByManager(managerIdStr);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not Manager Role! ID USER: " + managerId);
        }
        if (projects.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No Projects Found for Manager ID: " + managerIdStr);
        }

        return projects.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
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

            teamService.deleteByProjectId(id);

            List<Team> newTeams = new ArrayList<>();
            newTeams.addAll(requestDTO.getRequirementAnalysts().stream()
                    .map(userId -> createTeam(userId, existingProject))
                    .toList());
            newTeams.addAll(requestDTO.getBusinessAnalysts().stream()
                    .map(userId -> createTeam(userId, existingProject))
                    .toList());
            newTeams.addAll(requestDTO.getCommonUsers().stream()
                    .map(userId -> createTeam(userId, existingProject))
                    .toList());

            existingProject.setTeams(newTeams);

            Project updatedProject = projectRepository.save(existingProject);

            return convertToDTO(updatedProject);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found with id: " + id);
        }
    }

    @Transactional
    public void deleteById(Long id) {
        if (projectRepository.existsById(id)) {
            projectArtifactService.deleteArtifactsByProjectId(id);
            projectRepository.deleteById(id);
            if (projectRepository.existsById(id)) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete project with id: " + id);
            }
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

        List<Team> teams = new ArrayList<>();
        addTeams(dto, project, teams);
        project.setTeams(teams);

        return project;
    }

    private void addTeams(ProjectRequestDTO dto, Project project, List<Team> teams) {
        if (dto.getRequirementAnalysts() != null) {
            for (Long userId : dto.getRequirementAnalysts()) {
                teams.add(createTeam(userId, project));
            }
        }

        if (dto.getBusinessAnalysts() != null) {
            for (Long userId : dto.getBusinessAnalysts()) {
                teams.add(createTeam(userId, project));
            }
        }

        if (dto.getCommonUsers() != null) {
            for (Long userId : dto.getCommonUsers()) {
                teams.add(createTeam(userId, project));
            }
        }
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
