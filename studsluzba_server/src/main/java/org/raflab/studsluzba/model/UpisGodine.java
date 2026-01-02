package org.raflab.studsluzba.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;

@Entity
@Data
@Table(name = "upis_godine")
public class UpisGodine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "student_indeks_id", nullable = false)
    private StudentIndeks studentIndeks;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "skolska_godina_id", nullable = false)
    private SkolskaGodina skolskaGodina;

    @Column(nullable = false)
    private Integer godinaStudija;

    @Column(nullable = false)
    private LocalDate datumUpisa;

    private String napomena;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToMany
    @JoinTable(
            name = "upis_godine_predmet",
            joinColumns = @JoinColumn(name = "upis_godine_id"),
            inverseJoinColumns = @JoinColumn(name = "predmet_id")
    )
    private List<Predmet> predmeti = new ArrayList<>();

    public void addPredmet(Predmet predmet) {
        this.predmeti.add(predmet);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UpisGodine that = (UpisGodine) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public void setDatum(LocalDate datum) {
        this.datumUpisa = datum;
    }

    public LocalDate getDatum() {
        return LocalDate.now();
    }
}
