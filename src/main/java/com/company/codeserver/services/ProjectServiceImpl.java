package com.company.codeserver.services;

import com.company.codeserver.entities.PatchDTO;
import com.company.codeserver.entities.Project;
import com.company.codeserver.entities.SdlcSystem;
import com.company.codeserver.exceptions.FoundException;
import com.company.codeserver.exceptions.NotFoundException;
import com.company.codeserver.repositories.ProjectRepository;
import com.company.codeserver.repositories.SdlcSystemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private SdlcSystemRepository sdlcSystemRepository;

    public Project getProject(long id) {
        return projectRepository.findById(id).orElseThrow(() -> new NotFoundException(Project.class, id));
    }

    public Project createProject(Project project) {
        boolean status = projectRepository.existsBySdlcSystemIdAndExternalId(project.getSdlcSystem().getId(), project.getExternalId());
        if (status) {
            throw new FoundException(project.getId());
        } else {
            project.setSdlcSystem(sdlcSystemRepository.findById(project.getSdlcSystem().getId()).orElseThrow(() -> new NotFoundException(SdlcSystem.class, project.getSdlcSystem().getId())));
            project.setCreatedDate(Instant.now());
            project.setLastModifiedDate(Instant.now());
            return projectRepository.save(project);
        }
    }

    public Project updateProject(long projectId, @Valid PatchDTO patchDTO) {
        Project status = projectRepository.findById(projectId).orElseThrow(() -> new NotFoundException(Project.class, projectId));

        String tempExternalId = status.getExternalId();
        SdlcSystem sdlcSystem = sdlcSystemRepository.findById(status.getSdlcSystem().getId()).orElseThrow(() -> new NotFoundException(SdlcSystem.class, patchDTO.getSdlcSystem().getId()));

        if (patchDTO.getExternalId() != null) {
            status.setExternalId(patchDTO.getExternalId());
        }

        if (patchDTO.getSdlcSystem() != null) {
            status.setSdlcSystem(sdlcSystemRepository.findById(patchDTO.getSdlcSystem().getId()).orElseThrow(() -> new NotFoundException(SdlcSystem.class, patchDTO.getSdlcSystem().getId())));
        }

        String patchName = patchDTO.getName();
        if (patchName == null || !patchName.equals("invalid_name")) {
            status.setName(patchName);
        }
        status.setLastModifiedDate(Instant.now());

        boolean isExists = projectRepository.existsBySdlcSystemIdAndExternalId(status.getSdlcSystem().getId(), status.getExternalId());
        if (isExists && (!sdlcSystem.equals(sdlcSystemRepository.findById(status.getSdlcSystem().getId()).orElseThrow(() -> new NotFoundException(SdlcSystem.class, patchDTO.getSdlcSystem().getId()))
        ) || !tempExternalId.equals(status.getExternalId()))) {
            throw new FoundException(status.getId());
        } else {
            return projectRepository.save(status);
        }

    }


}
