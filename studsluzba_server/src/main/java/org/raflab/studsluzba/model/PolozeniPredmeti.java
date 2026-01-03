package org.raflab.studsluzba.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
public class PolozeniPredmeti {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private StudentIndeks studentIndeks;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToOne
    private Predmet predmet;

    private Integer ocena;
    private boolean priznat;

    @Column(name = "datum_polaganja", nullable = true)
    private LocalDate datumPolaganja;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToOne
    @JoinColumn(name = "izlazak_na_ispit_id")
    private IzlazakNaIspit izlazakNaIspit;

    @Column(name = "napomena")
    private String napomena;

    // Constructors
    public PolozeniPredmeti() {
    }

    public PolozeniPredmeti(StudentIndeks studentIndeks, Predmet predmet, Integer ocena, LocalDate datumPolaganja) {
        this.studentIndeks = studentIndeks;
        this.predmet = predmet;
        this.ocena = ocena;
        this.datumPolaganja = datumPolaganja;
        this.priznat = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StudentIndeks getStudentIndeks() {
        return studentIndeks;
    }

    public void setStudentIndeks(StudentIndeks studentIndeks) {
        this.studentIndeks = studentIndeks;
    }

    public Predmet getPredmet() {
        return predmet;
    }

    public void setPredmet(Predmet predmet) {
        this.predmet = predmet;
    }

    public Integer getOcena() {
        return ocena;
    }

    public void setOcena(Integer ocena) {
        this.ocena = ocena;
    }

    public boolean isPriznat() {
        return priznat;
    }

    public void setPriznat(boolean priznat) {
        this.priznat = priznat;
    }

    public LocalDate getDatumPolaganja() {
        return datumPolaganja;
    }

    public void setDatumPolaganja(LocalDate datumPolaganja) {
        this.datumPolaganja = datumPolaganja;
    }

    public IzlazakNaIspit getIzlazakNaIspit() {
        return izlazakNaIspit;
    }

    public void setIzlazakNaIspit(IzlazakNaIspit izlazakNaIspit) {
        this.izlazakNaIspit = izlazakNaIspit;
    }

    public String getNapomena() {
        return napomena;
    }

    public void setNapomena(String napomena) {
        this.napomena = napomena;
    }





    /* @OneToOne(mappedBy = "polozenPredmet")
          private IzlazakNaIspit izlazakNaIspit;
*/


}
