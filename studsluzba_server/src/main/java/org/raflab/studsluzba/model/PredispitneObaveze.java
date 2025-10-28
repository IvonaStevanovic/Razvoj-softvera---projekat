package org.raflab.studsluzba.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
public class PredispitneObaveze {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String vrsta;          // test, kolokvijum, zadatak...
    private Integer maksPoeni;

    @ManyToOne
    private DrziPredmet drziPredmet;  // predmet koji se drži u toj školskoj godini

    @ManyToOne
    private SkolskaGodina skolskaGodina;

    @OneToMany(mappedBy = "predispitnaObaveza", cascade = CascadeType.ALL)
    private Set<PredispitniPoeni> ostvareniPoeni;  // poeni koje studenti ostvaruju na ovoj obavezi
}
