package com.example.postgresdemo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.geo.Point;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name = "photos")
public class PhotoFile extends AuditModel {

    @Id
    @GeneratedValue(generator = "photo_id_generator")
    @SequenceGenerator(
            name = "photo_id_generator",
            sequenceName = "photo_id_sequence",
            initialValue = 100000
    )
    private Long id;

    /*
    Path relative to source root without leading slash
     */
    @NotBlank
    @Size(max = 255)
    private String relativePath;

    @NotBlank
    @Size(max = 255)
    private byte[] md5;

    @Column(columnDefinition = "jsonb")
    private String meta;

    @NotBlank
    @Column
    private Date date;

    @Column
    private Point geoLocation;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "source_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private PhotoFileSource source;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRelativePath() {
        return relativePath;
    }

    public void setRelativePath(String relativePath) {
        this.relativePath = relativePath;
    }

    public byte[] getMd5() {
        return md5;
    }

    public void setMd5(byte[] md5) {
        this.md5 = md5;
    }

    public String getMeta() {
        return meta;
    }

    public void setMeta(String meta) {
        this.meta = meta;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Point getGeoLocation() {
        return geoLocation;
    }

    public void setGeoLocation(Point geoLocation) {
        this.geoLocation = geoLocation;
    }

    public PhotoFileSource getSource() {
        return source;
    }

    public void setSource(PhotoFileSource source) {
        this.source = source;
    }
}