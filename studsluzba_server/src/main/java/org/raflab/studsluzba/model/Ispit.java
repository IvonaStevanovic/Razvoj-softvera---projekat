package org.raflab.studsluzba.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
public class Ispit {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private LocalDate datum;
    @ManyToOne
    @JoinColumn(name = "predmet_id")
    private Predmet predmet;
    @ManyToOne
    @JoinColumn(name = "nastavnik_id")
    private Nastavnik nastavnik;
    private LocalDateTime pocetak;
    private boolean zakljucen;
    @ManyToOne
    @JoinColumn(name = "ispitni_rok_id")
    private IspitniRok ispitniRok;
}
