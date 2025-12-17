package org.raflab.studsluzba.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Data
public class PredispitneObaveze {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String vrsta;          // test, kolokvijum, zadatak...
    private Integer maksPoeni;

    @ManyToOne
    private DrziPredmet drziPredmet;  // predmet koji se drži u toj školskoj godini

    @ManyToOne
    private SkolskaGodina skolskaGodina;

    @OneToMany(mappedBy = "predispitnaObaveza", cascade = CascadeType.ALL)
    private Set<PredispitniPoeni> ostvareniPoeni;  // poeni koje studenti ostvaruju na ovoj obavezi

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null  || getClass() != o.getClass()) return false;
        PredispitneObaveze that = (PredispitneObaveze) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVrsta() {
        return vrsta;
    }

    public void setVrsta(String vrsta) {
        this.vrsta = vrsta;
    }

    public Integer getMaksPoeni() {
        return maksPoeni;
    }

    public void setMaksPoeni(Integer maksPoeni) {
        this.maksPoeni = maksPoeni;
    }

    public DrziPredmet getDrziPredmet() {
        return drziPredmet;
    }

    public void setDrziPredmet(DrziPredmet drziPredmet) {
        this.drziPredmet = drziPredmet;
    }

    public SkolskaGodina getSkolskaGodina() {
        return skolskaGodina;
    }

    public void setSkolskaGodina(SkolskaGodina skolskaGodina) {
        this.skolskaGodina = skolskaGodina;
    }

    public Set<PredispitniPoeni> getOstvareniPoeni() {
        return ostvareniPoeni;
    }

    public void setOstvareniPoeni(Set<PredispitniPoeni> ostvareniPoeni) {
        this.ostvareniPoeni = ostvareniPoeni;
    }

}
