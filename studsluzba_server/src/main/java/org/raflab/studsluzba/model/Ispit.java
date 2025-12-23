package org.raflab.studsluzba.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.raflab.studsluzba.model.dtos.NastavnikDTO;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

@Entity
@Data
public class Ispit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate datumOdrzavanja;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "predmet_id", nullable = false)
    private Predmet predmet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "drzi_predmet_id", nullable = false)
    private DrziPredmet drziPredmet;

    private LocalTime vremePocetka;

    private boolean zakljucen;
    private String napomena;


    public Ispit(LocalDate datumOdrzavanja, Predmet predmet, DrziPredmet drziPredmet, LocalTime vremePocetka, boolean zakljucen, String napomena) {
        this.datumOdrzavanja = datumOdrzavanja;
        this.predmet = predmet;
        this.drziPredmet = drziPredmet;
        this.vremePocetka = vremePocetka;
        this.zakljucen = zakljucen;
        this.napomena = napomena;
    }

    public Ispit() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDatumOdrzavanja() {
        return datumOdrzavanja;
    }

    public void setDatumOdrzavanja(LocalDate datumOdrzavanja) {
        this.datumOdrzavanja = datumOdrzavanja;
    }

    public Predmet getPredmet() {
        return predmet;
    }

    public void setPredmet(Predmet predmet) {
        this.predmet = predmet;
    }

    public DrziPredmet getDrziPredmet() {
        return drziPredmet;
    }

    public void setDrziPredmet(DrziPredmet drziPredmet) {
        this.drziPredmet = drziPredmet;
    }

    public LocalTime getVremePocetka() {
        return vremePocetka;
    }

    public void setVremePocetka(LocalTime vremePocetka) {
        this.vremePocetka = vremePocetka;
    }

    public boolean isZakljucen() {
        return zakljucen;
    }

    public void setZakljucen(boolean zakljucen) {
        this.zakljucen = zakljucen;
    }

    public String getNapomena() {
        return napomena;
    }

    public void setNapomena(String napomena) {
        this.napomena = napomena;
    }



    /*
    @JsonIgnore
    @OneToMany(mappedBy = "ispit", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<PrijavaIspita> prijave;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ispitni_rok_id", nullable = false)
    private IspitniRok ispitniRok;

    @JsonIgnore
    @OneToMany(mappedBy = "ispit", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY
    )
          private Set<IzlazakNaIspit> izlazci;
*/

    /*
    @ManyToOne
    private Nastavnik nastavnik;
*/
}
