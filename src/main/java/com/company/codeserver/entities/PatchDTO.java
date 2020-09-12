package com.company.codeserver.entities;

import lombok.Data;

import java.time.Instant;

@Data
public class PatchDTO {
    private long id;

    private String externalId;

    private String name = "invalid_name";

    private SdlcSystem sdlcSystem;

    private Instant createdDate;

    private Instant lastModifiedDate;

}