package org.raflab.studsluzba.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class StudijskiProgram {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private String oznaka;
    private String naziv;
    private String godina;
    private String zvanje;
    private int semestri;
    @ManyToOne
    @JoinColumn(name = "vrsta_id")
    private Vrsta vrsta;
    @ManyToOne
    @JoinColumn(name = "predmet_id")
    private Predmet predmet;
}
