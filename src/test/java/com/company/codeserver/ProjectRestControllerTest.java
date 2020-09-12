package com.company.codeserver;

import com.company.codeserver.controllers.ProjectRestController;
import com.company.codeserver.entities.PatchDTO;
import com.company.codeserver.entities.Project;
import com.company.codeserver.entities.SdlcSystem;
import com.company.codeserver.exceptions.FoundException;
import com.company.codeserver.exceptions.NotFoundException;
import com.company.codeserver.repositories.ProjectRepository;
import com.company.codeserver.services.ProjectService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(SpringExtension.class)
@AutoConfigureJsonTesters
@WebMvcTest(ProjectRestController.class)
public class ProjectRestControllerTest {

    public static final String ENDPOINT = "/api/v2/projects";
    public static final String ENDPOINT_ID = "/{id}";
    public static final String PATH_VARIABLE_ID = "id";

    @Autowired
    private MockMvc mvc;

    @Autowired
    ProjectService projectService;

    @MockBean
    ProjectRepository projectRepository;

    @MockBean
    ProjectService ProjectService;

    @Autowired
    private JacksonTester<Project> jsonProject;

    @Autowired
    private JacksonTester<PatchDTO> jsonPatchRequest;

    @Test
    public void canRetrieveWhenExists() throws Exception {

        SdlcSystem s = new SdlcSystem();
        s.setId(1L);
        s.setBaseUrl("baseUrl");
        s.setCreatedDate(Instant.now());
        s.setDescription("description");
        s.setLastModifiedDate(Instant.now());

        Project p = new Project();
        p.setId(1L);
        p.setName("name");
        p.setExternalId("externalId");
        p.setCreatedDate(Instant.now());
        p.setLastModifiedDate(Instant.now());
        p.setSdlcSystem(s);

        given(projectService.getProject(1L)).willReturn(p);

        MockHttpServletResponse response = mvc.perform(get("/api/v2/projects/1").accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(jsonProject.write(p).getJson());

    }

    @Test
    public void cantRetrieveWhenNotExists() throws Exception {

        given(projectService.getProject(1234)).willThrow(new NotFoundException(Project.class, 1234));

        MockHttpServletResponse response = mvc.perform(get("/api/v2/projects/1234").accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(response.getContentAsString()).isEmpty();

    }

    @Test
    public void canPostWithFullPayload() throws Exception {

        SdlcSystem s = new SdlcSystem();
        s.setId(1L);
        s.setBaseUrl("baseUrl");
        s.setCreatedDate(Instant.now());
        s.setDescription("description");
        s.setLastModifiedDate(Instant.now());

        Project p = new Project();
        p.setId(1L);
        p.setName("name");
        p.setExternalId("externalId");
        p.setCreatedDate(Instant.now());
        p.setLastModifiedDate(Instant.now());
        p.setSdlcSystem(s);

        given(projectService.createProject(any(Project.class))).willReturn(p);

        MockHttpServletResponse response = mvc.perform(post("/api/v2/projects").contentType(MediaType.APPLICATION_JSON)
                .content(jsonProject.write(p).getJson())).andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getContentAsString()).isEqualTo(jsonProject.write(p).getJson());

    }

    @Test
    public void canPostWithMinimalPayload() throws Exception {

        SdlcSystem s = new SdlcSystem();
        s.setId(1L);
        s.setBaseUrl("baseUrl");
        s.setCreatedDate(Instant.now());
        s.setDescription("description");
        s.setLastModifiedDate(Instant.now());

        Project p = new Project();
        p.setId(1L);
        p.setExternalId("externalId");
        p.setCreatedDate(Instant.now());
        p.setLastModifiedDate(Instant.now());
        p.setSdlcSystem(s);

        given(projectService.createProject(p)).willReturn(p);

        MockHttpServletResponse response = mvc.perform(post("/api/v2/projects").contentType(MediaType.APPLICATION_JSON)
                .content(jsonProject.write(p).getJson())).andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());

    }

    @Test
    public void cantPostWithNonExistingSystem() throws IOException, Exception {

        SdlcSystem s = new SdlcSystem();
        s.setId(1L);
        s.setBaseUrl("baseUrl");
        s.setCreatedDate(Instant.now());
        s.setDescription("description");
        s.setLastModifiedDate(Instant.now());

        Project p = new Project();
        p.setId(1L);
        p.setName("name");
        p.setExternalId("externalId");
        p.setCreatedDate(Instant.now());
        p.setLastModifiedDate(Instant.now());
        p.setSdlcSystem(s);

        given(projectService.createProject(any(Project.class))).willThrow(new NotFoundException(SdlcSystem.class, 1234));

        MockHttpServletResponse response = mvc.perform(post("/api/v2/projects").contentType(MediaType.APPLICATION_JSON).content(
                jsonProject.write(p).getJson()
        )).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());

    }


    @Test
    public void cantPostUnderConflict() throws IOException, Exception {

        SdlcSystem s = new SdlcSystem();
        s.setId(1L);
        s.setBaseUrl("baseUrl");
        s.setCreatedDate(Instant.now());
        s.setDescription("description");
        s.setLastModifiedDate(Instant.now());

        Project p = new Project();
        p.setId(1L);
        p.setName("name");
        p.setExternalId("externalId");
        p.setCreatedDate(Instant.now());
        p.setLastModifiedDate(Instant.now());
        p.setSdlcSystem(s);

        given(projectService.createProject(any(Project.class))).willThrow(new FoundException(1234));

        MockHttpServletResponse response = mvc.perform(post("/api/v2/projects").contentType(MediaType.APPLICATION_JSON).content(
                jsonProject.write(p).getJson()
        )).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CONFLICT.value());

    }

    @Test
    public void canPatch() throws Exception {

        SdlcSystem s = new SdlcSystem();
        s.setId(1L);
        s.setBaseUrl("baseUrl");
        s.setCreatedDate(Instant.now());
        s.setDescription("description");
        s.setLastModifiedDate(Instant.now());

        Project p = new Project();
        p.setId(1L);
        p.setName("name");
        p.setExternalId("externalId");
        p.setCreatedDate(Instant.now());
        p.setLastModifiedDate(Instant.now());
        p.setSdlcSystem(s);

        PatchDTO pr = new PatchDTO();
        pr.setName("newname");

        given(projectService.updateProject(eq(1), any(PatchDTO.class))).willReturn(p);

        MockHttpServletResponse response = mvc.perform(patch("/api/v2/projects/1").contentType(MediaType.APPLICATION_JSON).content(
                jsonPatchRequest.write(pr).getJson()
        )).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

    }

    @Test
    public void cantPatchIfNonExistingSystem() throws Exception {

        SdlcSystem s = new SdlcSystem();
        s.setId(1L);
        s.setBaseUrl("baseUrl");
        s.setCreatedDate(Instant.now());
        s.setDescription("description");
        s.setLastModifiedDate(Instant.now());

        SdlcSystem s2 = new SdlcSystem();
        s.setId(123L);
        s.setBaseUrl("baseUrl");
        s.setCreatedDate(Instant.now());
        s.setDescription("description");
        s.setLastModifiedDate(Instant.now());

        Project p = new Project();
        p.setId(1L);
        p.setName("name");
        p.setExternalId("externalId");
        p.setCreatedDate(Instant.now());
        p.setLastModifiedDate(Instant.now());
        p.setSdlcSystem(s);

        PatchDTO pr = new PatchDTO();
        pr.setSdlcSystem(s2);


        given(projectService.updateProject(eq(1L), any(PatchDTO.class))).willThrow(new NotFoundException(SdlcSystem.class, 1L));


        MockHttpServletResponse response = mvc.perform(patch("/api/v2/projects/1").contentType(MediaType.APPLICATION_JSON).content(
                jsonPatchRequest.write(pr).getJson()
        )).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());

    }


    @Test
    public void cantTestIfConflictExists() throws Exception {

        SdlcSystem s = new SdlcSystem();
        s.setId(1L);
        s.setBaseUrl("baseUrl");
        s.setCreatedDate(Instant.now());
        s.setDescription("description");
        s.setLastModifiedDate(Instant.now());

        Project p = new Project();
        p.setId(1L);
        p.setName("name");
        p.setExternalId("externalId");
        p.setCreatedDate(Instant.now());
        p.setLastModifiedDate(Instant.now());
        p.setSdlcSystem(s);

        PatchDTO pr = new PatchDTO();
        pr.setName("newname");

        given(projectService.updateProject(eq(1L), any(PatchDTO.class))).willThrow(new FoundException(1L));

        MockHttpServletResponse response = mvc.perform(patch("/api/v2/projects/1").contentType(MediaType.APPLICATION_JSON).content(
                jsonPatchRequest.write(pr).getJson()
        )).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CONFLICT.value());

    }


}