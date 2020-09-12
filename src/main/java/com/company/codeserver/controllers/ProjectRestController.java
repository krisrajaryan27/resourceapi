package com.company.codeserver.controllers;

import com.company.codeserver.entities.PatchDTO;
import com.company.codeserver.entities.Project;
import com.company.codeserver.services.ProjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(ProjectRestController.ENDPOINT)
@Api(produces = MediaType.APPLICATION_JSON_VALUE, tags = "Project")
public class ProjectRestController {

    public static final String ENDPOINT = "/api/v2/projects";
    public static final String ENDPOINT_ID = "/{id}";
    public static final String PATH_VARIABLE_ID = "id";

    private static final String API_PARAM_ID = "ID";

    @Autowired
    private ProjectService projectService;

    @ApiOperation("Get a Project")
    @GetMapping(ENDPOINT_ID)
    public Project getProject(@ApiParam(name = API_PARAM_ID, required = true)
                              @PathVariable(PATH_VARIABLE_ID) final long projectId) {
        return projectService.getProject(projectId);
    }

    @ApiOperation("Create a Project")
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Project createProject(@Valid @RequestBody Project project) {
        return projectService.createProject(project);
    }

    @ApiOperation("Update a Project")
    @PatchMapping(ENDPOINT_ID)
    public Project updateProject(@ApiParam(name = API_PARAM_ID, required = true)
                                 @PathVariable(PATH_VARIABLE_ID) final long projectId, @Valid
                                 @RequestBody PatchDTO patchDTO) {
        return projectService.updateProject(projectId, patchDTO);
    }

}
