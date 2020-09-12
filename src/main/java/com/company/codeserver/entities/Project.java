package com.company.codeserver.entities;

import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Table(name = "project")
public class Project extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "external_id", nullable = false)
    @NotBlank
    private String externalId;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "sdlc_system_id")
    @NotNull
    private SdlcSystem sdlcSystem;

}