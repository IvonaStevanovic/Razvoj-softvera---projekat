package org.raflab.studsluzba.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Data
public class UpisGodine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer godinaStudija;
    /// SLUSA predmeti
    private LocalDate datum;
    private String napomena;
    @ManyToOne
    private StudentIndeks studentIndeks;

    @ManyToMany
    @JoinTable(
            name = "upis_preneti_predmeti",
            joinColumns = @JoinColumn(name = "upis_godine_id"),
            inverseJoinColumns = @JoinColumn(name = "predmet_id")
    )
    private Set<Predmet> prenetiPredmeti;

    /*
       ako upisuje prvu godinu
       private Double uspehSrednja;
    private Double uspehPrijemni;*/
}
