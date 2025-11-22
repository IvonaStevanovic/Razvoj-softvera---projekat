package org.raflab.studsluzba.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

@Entity
@Data
public class Ispit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate datumOdrzavanja;

    @ManyToOne
    @JoinColumn(name = "predmet_id")
    private Predmet predmet;

    @ManyToOne
    private Nastavnik nastavnik;

    private LocalTime vremePocetka;

    private boolean zakljucen;

    //JsonIgnore
    @OneToMany(mappedBy = "ispit", cascade = CascadeType.ALL)
    private Set<PrijavaIspita> prijave;

    @ManyToOne
    private IspitniRok ispitniRok;

    //JsonIgnore
    @OneToMany(mappedBy = "ispit", cascade = CascadeType.ALL)
          private Set<IzlazakNaIspit> izlazci;

}
