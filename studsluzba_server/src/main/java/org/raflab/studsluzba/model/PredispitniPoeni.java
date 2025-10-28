package org.raflab.studsluzba.model;

import lombok.Data;

import javax.persistence.*;



@Entity
@Data
public class PredispitniPoeni {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer poeni;  // poeni koje je student ostvario

    @ManyToOne
    private StudentIndeks studentIndeks;          // čiji su poeni

    @ManyToOne
    private PredispitneObaveze predispitnaObaveza; // kojoj predispitnoj obavezi pripadaju

    @ManyToOne
    private SlusaPredmet slusaPredmet;            // opcionalno, vezano za konkretnog studenta i predmet u školskoj godini

    @ManyToOne
    private SkolskaGodina skolskaGodina;

}