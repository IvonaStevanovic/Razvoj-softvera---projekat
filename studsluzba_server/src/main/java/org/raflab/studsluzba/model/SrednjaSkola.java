package org.raflab.studsluzba.model;

import lombok.Data;
import net.minidev.json.annotate.JsonIgnore;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
public class SrednjaSkola {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String naziv;
    private String mesto;
    private String vrsta;

    @OneToMany(mappedBy = "srednjaSkola")
    private Set<StudentPodaci> studentPodaci;
    public SrednjaSkola() {
    }

    public SrednjaSkola(String naziv, String mesto, String vrsta) {
        this.naziv = naziv;
        this.mesto = mesto;
        this.vrsta = vrsta;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getMesto() {
        return mesto;
    }

    public void setMesto(String mesto) {
        this.mesto = mesto;
    }

    public String getVrsta() {
        return vrsta;
    }

    public void setVrsta(String vrsta) {
        this.vrsta = vrsta;
    }
}
