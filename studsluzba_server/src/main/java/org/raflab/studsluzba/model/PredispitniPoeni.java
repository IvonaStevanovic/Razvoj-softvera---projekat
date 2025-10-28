package org.raflab.studsluzba.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class PredispitniPoeni {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer poeni;
    @ManyToOne
    private StudentIndeks studentIndeks;   // čiji su poeni

    @ManyToOne
    private Predmet predmet;               // predmet na kojem su poeni ostvareni

    @ManyToOne
    private SkolskaGodina skolskaGodina;   // školska godina po kojoj se poeni računaju

    @ManyToOne
    private SlusaPredmet slusaPredmet;
}
