package com.example.postgresdemo.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "photos_sources")
public class PhotoFileSource extends AuditModel {
    @Id
    @GeneratedValue(generator = "source_id_generator")
    @SequenceGenerator(
            name = "source_id_generator",
            sequenceName = "source_id_sequence"
    )
    private Long id;

    /*
    Absolute path to folder with source photos to monitor and import into library
     */
    @NotBlank
    @Size(max = 255)
    private String path;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
