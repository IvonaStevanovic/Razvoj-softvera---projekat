package org.raflab.studsluzba.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Data
public class ObnovaGodine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer godinaStudija;
    private LocalDate datum;
    private String napomena;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "skolska_godina_id", nullable = false)
    private SkolskaGodina skolskaGodina;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    private StudentIndeks studentIndeks;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToMany(fetch = FetchType.LAZY)
          @JoinTable(
              name = "obnova_predmeti",
              joinColumns = @JoinColumn(name = "obnova_godine_id"),
              inverseJoinColumns = @JoinColumn(name = "predmet_id")
          )
    private List<Predmet> predmetiKojeUpisuje;
    public void addPredmet(Predmet predmet) {
        this.predmetiKojeUpisuje.add(predmet);
    }
    public ObnovaGodine() {
    }

    public ObnovaGodine(StudentIndeks studentIndeks, SkolskaGodina skolskaGodina, Integer godinaStudija, LocalDate datumObnove) {
        this.studentIndeks = studentIndeks;
        this.skolskaGodina = skolskaGodina;
        this.godinaStudija = godinaStudija;
        this.datum = datumObnove;
    }

    // Getters and Setters
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

    public SkolskaGodina getSkolskaGodina() {
        return skolskaGodina;
    }

    public void setSkolskaGodina(SkolskaGodina skolskaGodina) {
        this.skolskaGodina = skolskaGodina;
    }

    public Integer getGodinaStudija() {
        return godinaStudija;
    }

    public void setGodinaStudija(Integer godinaStudija) {
        this.godinaStudija = godinaStudija;
    }

    public String getNapomena() {
        return napomena;
    }

    public void setNapomena(String napomena) {
        this.napomena = napomena;
    }

    public List<Predmet> getPredmeti() {
        return predmetiKojeUpisuje;
    }

    public void setPredmeti(List<Predmet> predmeti) {
        this.predmetiKojeUpisuje = predmeti;
    }

    public void setDatumObnove(LocalDate of) {
    }
    public LocalDate getDatum() {
        return datum;
    }
}
