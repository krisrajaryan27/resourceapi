package com.company.codeserver.repositories;

import com.company.codeserver.entities.SdlcSystem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SdlcSystemRepository extends JpaRepository<SdlcSystem, Long> {

}
