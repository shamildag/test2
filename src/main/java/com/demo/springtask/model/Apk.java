package com.demo.springtask.model;

import lombok.Data;
import org.springframework.data.repository.NoRepositoryBean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "APK")
@NoRepositoryBean
public class Apk {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "NAME", nullable = false)
    String name;

    @Column(name = "FILENAME", nullable = false)
    String filename;

    @Column(name = "DESC", nullable = false)
    String description;

    @Column(name = "CATEGORY", nullable = false)
    String category;

    @Lob
    @Column(name = "IMG128", nullable = true)
    byte[] image128;

    @Lob
    @Column(name = "IMG512", nullable = true)
    byte[] image512;

    @Column(name = "DEV", nullable = false)
    Long dev_id;

    @Column(name = "DOWNLOADS", nullable = false)
    Integer downloads;

    Float rating;

    LocalDate upload_date;

    public Apk() {
    }

    public Apk(String name, String description, String category, Long dev_id) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.dev_id = dev_id;
    }

    public Apk(String name, String description, String category, Long dev_id, String filename) {
        this(name, description, category, dev_id);
        this.filename = filename;
    }
}
