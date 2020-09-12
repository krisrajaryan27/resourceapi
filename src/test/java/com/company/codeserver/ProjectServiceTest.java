package com.company.codeserver;

import com.company.codeserver.entities.PatchDTO;
import com.company.codeserver.entities.Project;
import com.company.codeserver.entities.SdlcSystem;
import com.company.codeserver.exceptions.FoundException;
import com.company.codeserver.exceptions.NotFoundException;
import com.company.codeserver.repositories.ProjectRepository;
import com.company.codeserver.repositories.SdlcSystemRepository;
import com.company.codeserver.services.ProjectService;
import com.company.codeserver.services.ProjectServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
public class ProjectServiceTest {

    @TestConfiguration
    static class ProjectServiceTestConfiguration {
        @Bean
        public ProjectService projectService() {
            return new ProjectServiceImpl();
        }
    }

    @Autowired
    ProjectService projectService;


    @MockBean
    private ProjectRepository projectRepository;
    @MockBean
    private SdlcSystemRepository sdlcSystemRepository;

    @Before
    public void setup() {

        SdlcSystem s = new SdlcSystem();
        s.setBaseUrl("baseUrl");
        s.setDescription("description");
        s.setCreatedDate(Instant.now());
        s.setLastModifiedDate(Instant.now());
        s.setId(1);

        SdlcSystem s2 = new SdlcSystem();
        s2.setBaseUrl("baseUrl");
        s2.setDescription("description");
        s2.setCreatedDate(Instant.now());
        s2.setLastModifiedDate(Instant.now());
        s2.setId(2);

        SdlcSystem s3 = new SdlcSystem();
        s3.setBaseUrl("baseUrl");
        s3.setDescription("description");
        s3.setCreatedDate(Instant.now());
        s3.setLastModifiedDate(Instant.now());
        s3.setId(20L);

        Project p3 = new Project();
        p3.setId(3L);
        p3.setExternalId("externalId");
        p3.setCreatedDate(Instant.now());
        p3.setSdlcSystem(s3);
        p3.setLastModifiedDate(Instant.now());


        Project p = new Project();
        p.setId(1);
        p.setExternalId("externalId");
        p.setCreatedDate(Instant.now());
        p.setSdlcSystem(s);
        p.setLastModifiedDate(Instant.now());

        Project p2 = new Project();
        p2.setId(2);
        p2.setExternalId("externalId");
        p2.setCreatedDate(Instant.now());
        p2.setSdlcSystem(s2);
        p2.setLastModifiedDate(Instant.now());


        Mockito.when(sdlcSystemRepository.findById(1L)).thenReturn(Optional.of(s));
        Mockito.when(sdlcSystemRepository.findById(20L)).thenReturn(Optional.of(s3));
        Mockito.when(projectRepository.findById(1L)).thenReturn(Optional.of(p));
        Mockito.when(projectRepository.findById(3L)).thenReturn(Optional.of(p3));
        Mockito.when(projectRepository.existsBySdlcSystemIdAndExternalId(1L, "externalId")).thenReturn(true);
        Mockito.when(projectRepository.existsBySdlcSystemIdAndExternalId(2L, "externalId")).thenReturn(false);
        Mockito.when(projectRepository.existsBySdlcSystemIdAndExternalId(3L, "externalId")).thenReturn(true);
        Mockito.when(projectRepository.existsBySdlcSystemIdAndExternalId(2L, "externalId")).thenReturn(false);
        Mockito.when(projectRepository.existsBySdlcSystemIdAndExternalId(20L, "externalId")).thenReturn(true);
        Mockito.when(projectRepository.save(Mockito.any(Project.class))).thenAnswer(i -> i.getArguments()[0]);


    }

    @Test
    public void ifIdIsCorrectThenProjectShouldBeFound() {
        Project found = projectService.getProject(1L);
        assertEquals("externalId", found.getExternalId());

    }

    @Test(expected = NotFoundException.class)
    public void notFoundIfIdDontExists() {

        Project found = projectService.getProject(32L);

    }

    @Test
    public void createIfAllright() {

        SdlcSystem s2 = new SdlcSystem();
        s2.setBaseUrl("baseUrl");
        s2.setDescription("description");
        s2.setCreatedDate(Instant.now());
        s2.setLastModifiedDate(Instant.now());
        s2.setId(1L);

        Project p2 = new Project();
        p2.setId(2L);
        p2.setExternalId("externalId22");
        p2.setCreatedDate(Instant.now());
        p2.setSdlcSystem(s2);
        p2.setLastModifiedDate(Instant.now());


        Project save = projectService.createProject(p2);
        assertEquals("externalId22", save.getExternalId());
    }

    @Test(expected = FoundException.class)
    public void conflictOnSave() {

        SdlcSystem s = new SdlcSystem();
        s.setBaseUrl("baseUrl");
        s.setDescription("description");
        s.setCreatedDate(Instant.now());
        s.setLastModifiedDate(Instant.now());
        s.setId(1L);

        Project p = new Project();
        p.setId(1L);
        p.setExternalId("externalId");
        p.setCreatedDate(Instant.now());
        p.setSdlcSystem(s);
        p.setLastModifiedDate(Instant.now());

        Project save = projectService.createProject(p);

    }

    @Test(expected = NotFoundException.class)
    public void allreadyHaveEntry() {

        SdlcSystem s = new SdlcSystem();
        s.setBaseUrl("baseUrl");
        s.setDescription("description");
        s.setCreatedDate(Instant.now());
        s.setLastModifiedDate(Instant.now());
        s.setId(8L);

        Project p = new Project();
        p.setId(5L);
        p.setExternalId("externalIdhh");
        p.setCreatedDate(Instant.now());
        p.setSdlcSystem(s);
        p.setLastModifiedDate(Instant.now());

        Project save = projectService.createProject(p);

    }

    @Test
    public void succesfullPatch() {

        PatchDTO pr = new PatchDTO();
        pr.setName("newName");

        Project update = projectService.updateProject(1L, pr);

        assertEquals("newName", update.getName());
    }

    @Test(expected = NotFoundException.class)
    public void cannotFindSystemPatch() {

        PatchDTO pr = new PatchDTO();
        SdlcSystem s = new SdlcSystem();
        s.setBaseUrl("baseUrl");
        s.setDescription("description");
        s.setCreatedDate(Instant.now());
        s.setLastModifiedDate(Instant.now());
        s.setId(8L);

        pr.setName("newName");
        pr.setSdlcSystem(s);

        Project update = projectService.updateProject(1L, pr);

        assertEquals("newName", update.getName());
    }

    @Test(expected = FoundException.class)
    public void conflictingValuesWhilePatching() {

        PatchDTO pr = new PatchDTO();
        SdlcSystem s = new SdlcSystem();
        s.setBaseUrl("baseUrl");
        s.setDescription("description");
        s.setCreatedDate(Instant.now());
        s.setLastModifiedDate(Instant.now());
        s.setId(1L);

        pr.setName("newName");
        pr.setSdlcSystem(s);
        Project update = projectService.updateProject(3L, pr);
    }


}