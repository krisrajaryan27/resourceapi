package com.company.codeserver.entities;

import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Data
@Entity
@Table(name = "sdlc_system")
public class SdlcSystem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotEmpty
    @URL
    @Column(name = "base_url", nullable = false)
    private String baseUrl;

    @Column(name = "description")
    private String description;

}
