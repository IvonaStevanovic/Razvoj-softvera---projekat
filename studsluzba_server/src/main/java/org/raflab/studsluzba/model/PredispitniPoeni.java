package org.raflab.studsluzba.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Objects;


@Entity
@Data
public class PredispitniPoeni {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer poeni;  // poeni koje je student ostvario

    @ManyToOne
    private StudentIndeks studentIndeks;          // čiji su poeni

    @ManyToOne
    private PredispitneObaveze predispitnaObaveza; // kojoj predispitnoj obavezi pripadaju

    @ManyToOne
    private SlusaPredmet slusaPredmet;            // opcionalno, vezano za konkretnog studenta i predmet u školskoj godini

    @ManyToOne
    private SkolskaGodina skolskaGodina;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PredispitniPoeni that = (PredispitniPoeni) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}