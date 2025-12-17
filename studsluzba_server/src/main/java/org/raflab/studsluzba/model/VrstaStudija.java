package org.raflab.studsluzba.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
public class VrstaStudija {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String oznaka;    // "OAS"
    private String punNaziv;  // "Osnovne akademske studije"
    @OneToMany(mappedBy = "vrstaStudija")
    private Set<StudijskiProgram> programi;
    public VrstaStudija() {
    }

    public VrstaStudija(String oznaka, String punNaziv) {
        this.oznaka = oznaka;
        this.punNaziv = punNaziv;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
