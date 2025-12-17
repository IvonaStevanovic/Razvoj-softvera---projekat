package org.raflab.studsluzba.model;

import lombok.Data;

import javax.persistence.*;
@Entity
@Data
public class IzlazakNaIspit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private PrijavaIspita prijavaIspita; // veza ka prijavi ispita (sadrži student + predmet + rok)

    @ManyToOne
    private SlusaPredmet slusaPredmet; // za evidenciju po školskoj godini i nastavniku

    private Integer poeniPredispitne; // poeni sa predispitnih
    private Integer poeniIspit;       // poeni sa ispita
    private Integer ukupnoPoeni;      // zbir predispitnih + ispita
    private Integer ocena;            // 5-10, automatski se računa

    private String napomena;
    private Boolean ponisteno = false;
    private Boolean izasao = false;   // da li je student izašao

    @OneToOne(mappedBy = "izlazakNaIspit", cascade = CascadeType.ALL)
    private PolozeniPredmeti polozeniPredmet;
    @ManyToOne
    @JoinColumn(name = "student_indeks_id")
    private StudentIndeks studentIndeks;


    public IzlazakNaIspit() {}

    public IzlazakNaIspit(PrijavaIspita prijavaIspita, SlusaPredmet slusaPredmet,
                          Integer poeniPredispitne, Integer poeniIspit) {
        this.prijavaIspita = prijavaIspita;
        this.slusaPredmet = slusaPredmet;
        this.poeniPredispitne = poeniPredispitne;
        this.poeniIspit = poeniIspit;
        this.ukupnoPoeni = poeniPredispitne + poeniIspit;
        this.ocena = izracunajOcenu(this.ukupnoPoeni);
        this.ponisteno = false;
        this.izasao = true;
    }

    private Integer izracunajOcenu(Integer ukupnoPoeni) {
        if (ukupnoPoeni < 51) return 5;
        if (ukupnoPoeni < 61) return 6;
        if (ukupnoPoeni < 71) return 7;
        if (ukupnoPoeni < 81) return 8;
        if (ukupnoPoeni < 91) return 9;
        return 10;
    }

    public boolean jePolozio() {
        return !ponisteno && ocena != null && ocena >= 6;
    }

    public void setPoeniPredispitne(Integer poeniPredispitne) {
        this.poeniPredispitne = poeniPredispitne;
        updateUkupnoPoeni();
    }

    public void setPoeniIspit(Integer poeniIspit) {
        this.poeniIspit = poeniIspit;
        updateUkupnoPoeni();
    }

    private void updateUkupnoPoeni() {
        if (poeniPredispitne != null && poeniIspit != null) {
            this.ukupnoPoeni = poeniPredispitne + poeniIspit;
            this.ocena = izracunajOcenu(this.ukupnoPoeni);
        }
    }

    public void setStudentIndeks(StudentIndeks studentIndeks) {
    }

    public void setIspit(Ispit ispit) {
    }

    public void setOstvarenoNaIspitu(Integer ostvarenoNaIspitu) {
    }

    public void setPonistio(boolean ponistio) {
    }

    public Integer getOstvarenoNaIspitu() {
        return poeniIspit;
    }

    public boolean isPonistio() {
        return false;
    }

    public boolean isIzasao() {
        return true;
    }

    public Uplata getStudentIndeks() {
        return null;
    }

    public Thread getIspit() {
        return null;
    }
}
