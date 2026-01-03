package org.raflab.studsluzba.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import net.minidev.json.annotate.JsonIgnore;

import javax.persistence.*;@Entity
@Data
public class IzlazakNaIspit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private PrijavaIspita prijavaIspita;

    @ManyToOne
    private SlusaPredmet slusaPredmet;

    private Integer poeniPredispitne = 0;
    private Integer poeniIspit = 0;
    private Integer ukupnoPoeni = 0;
    private Integer ocena = 5;

    private String napomena;
    private Boolean ponisteno = false;
    private Boolean izasao = false;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToOne(mappedBy = "izlazakNaIspit", cascade = CascadeType.ALL)
    @JsonIgnore
    private PolozeniPredmeti polozeniPredmet;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "student_indeks_id")
    private StudentIndeks studentIndeks;

    public IzlazakNaIspit() {}

    public IzlazakNaIspit(PrijavaIspita prijavaIspita, SlusaPredmet slusaPredmet,
                          Integer poeniPredispitne, Integer poeniIspit) {
        this.prijavaIspita = prijavaIspita;
        this.slusaPredmet = slusaPredmet;
        this.poeniPredispitne = poeniPredispitne != null ? poeniPredispitne : 0;
        this.poeniIspit = poeniIspit != null ? poeniIspit : 0;
        updateUkupnoPoeni();
        this.ponisteno = false;
        this.izasao = true;
    }

    public void setPoeniPredispitne(Integer poeniPredispitne) {
        this.poeniPredispitne = poeniPredispitne != null ? poeniPredispitne : 0;
        updateUkupnoPoeni();
    }

    public void setPoeniIspit(Integer poeniIspit) {
        this.poeniIspit = poeniIspit != null ? poeniIspit : 0;
        updateUkupnoPoeni();
    }

    private void updateUkupnoPoeni() {
        this.ukupnoPoeni = (poeniPredispitne != null ? poeniPredispitne : 0)
                + (poeniIspit != null ? poeniIspit : 0);
        this.ocena = izracunajOcenu(this.ukupnoPoeni);
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
    public boolean isPonistio() {
        return ponisteno != null ? ponisteno : false;
    }

    public boolean isIzasao() {
        return izasao != null ? izasao : false;
    }

    public void setPonistio(boolean ponistio) {
        this.ponisteno = ponistio;
    }

    public void setIzasao(boolean izasao) {
        this.izasao = izasao;
    }
}

