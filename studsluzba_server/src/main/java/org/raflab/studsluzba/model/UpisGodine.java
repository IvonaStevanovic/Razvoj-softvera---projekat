package org.raflab.studsluzba.model;

import lombok.Data;

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

    @ManyToOne
    @JoinColumn(name = "student_indeks_id", nullable = false)
    private StudentIndeks studentIndeks;

    @ManyToOne
    @JoinColumn(name = "skolska_godina_id", nullable = false)
    private SkolskaGodina skolskaGodina;

    @Column(nullable = false)
    private Integer godinaStudija;

    @Column(nullable = false)
    private LocalDate datumUpisa;

    private String napomena;

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
    }

    public void setPrenetiPredmeti(Set<?> objects) {
    }

    public LocalDate getDatum() {
        return LocalDate.now();
    }


    public double[] getPrenetiPredmeti() {
        return null;
    }
}
