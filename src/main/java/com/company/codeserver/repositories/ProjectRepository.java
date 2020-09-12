package com.company.codeserver.repositories;

import com.company.codeserver.entities.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    Optional<Project> findBySdlcSystemIdAndId(long sdlcSystemId, long projectId);

    boolean existsBySdlcSystemIdAndExternalId(long sdlcSystemId, String externalId);

}
