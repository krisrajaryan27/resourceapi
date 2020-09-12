package com.company.codeserver.services;

import com.company.codeserver.entities.PatchDTO;
import com.company.codeserver.entities.Project;

import javax.validation.Valid;

public interface ProjectService {

    Project getProject(long id);

    Project createProject(Project project);

    Project updateProject(long projectId, @Valid PatchDTO patchDTO);
}
