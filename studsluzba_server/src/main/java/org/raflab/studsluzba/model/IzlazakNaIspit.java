package org.raflab.studsluzba.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class IzlazakNaIspit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer ostvarenoNaIspitu;
    private String napomena;
    private boolean ponistio;
    private boolean izasao;

    //JsonIgnore
    @ManyToOne
    private StudentIndeks studentIndeks;
    //JsonIgnore
    @ManyToOne
    private Ispit ispit;

    @ManyToOne
    private SlusaPredmet slusaPredmet;

    /* @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "polozen_predmet_id")
    private PolozeniPredmeti polozenPredmet;
    */

}
