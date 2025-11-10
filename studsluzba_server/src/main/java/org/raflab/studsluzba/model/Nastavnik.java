package org.raflab.studsluzba.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.raflab.studsluzba.model.NastavnikZvanje;
import org.raflab.studsluzba.model.VisokoskolskaUstanova;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Nastavnik {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    private String ime;
    private String prezime;
    private String srednjeIme;
    private String email;
    private String brojTelefona;
    private String adresa;

    @OneToMany(mappedBy = "nastavnik", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<NastavnikZvanje> zvanja;

    private LocalDate datumRodjenja;
    private Character pol;
    private String jmbg;

    @ManyToMany
    @JoinTable(
            name = "nastavnik_obrazovanje",
            joinColumns = @JoinColumn(name = "nastavnik_id"),
            inverseJoinColumns = @JoinColumn(name = "ustanova_id")
    )
    private Set<VisokoskolskaUstanova> obrazovanja;
}
