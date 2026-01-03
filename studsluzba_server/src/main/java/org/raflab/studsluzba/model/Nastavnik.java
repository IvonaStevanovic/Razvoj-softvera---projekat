package org.raflab.studsluzba.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.raflab.studsluzba.model.NastavnikZvanje;
import org.raflab.studsluzba.model.VisokoskolskaUstanova;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;
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

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(mappedBy = "nastavnik", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<NastavnikZvanje> zvanja;

    private LocalDate datumRodjenja;
    private Character pol;
    private String jmbg;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToMany
    @JoinTable(
            name = "nastavnik_obrazovanje",
            joinColumns = @JoinColumn(name = "nastavnik_id"),
            inverseJoinColumns = @JoinColumn(name = "ustanova_id")
    )
    private Set<VisokoskolskaUstanova> obrazovanja;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Nastavnik nastavnik = (Nastavnik) o;
        return Objects.equals(id, nastavnik.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
