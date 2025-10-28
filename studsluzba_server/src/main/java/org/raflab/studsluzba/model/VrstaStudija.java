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
}
