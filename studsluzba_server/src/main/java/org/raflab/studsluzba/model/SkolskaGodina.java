package org.raflab.studsluzba.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Data
public class SkolskaGodina {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String naziv;
    private boolean aktivna;

    @Column(name = "pocetak_zimskog", nullable = false)
    private LocalDate pocetakZimskog;

    @Column(name = "kraj_zimskog", nullable = false)
    private LocalDate krajZimskog;

    @Column(name = "pocetak_letnjeg", nullable = false)
    private LocalDate pocetakLetnjeg;

    @Column(name = "kraj_letnjeg", nullable = false)
    private LocalDate krajLetnjeg;

    @OneToMany(mappedBy = "skolskaGodina")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<DrziPredmet> drziPredmetList;

    @OneToMany(mappedBy = "skolskaGodina", cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<SlusaPredmet> slusaPredmeti;

    @OneToMany(mappedBy = "skolskaGodina", cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<IspitniRok> ispitniRokovi;

    @OneToMany(mappedBy = "skolskaGodina", cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<PredispitniPoeni> predispitniPoeni;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SkolskaGodina that = (SkolskaGodina) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public Boolean getAktivna() {
        return false;
    }

    public boolean isAktivna() {
        return true;
    }
}
