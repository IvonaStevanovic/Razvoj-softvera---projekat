package org.raflab.studsluzba.model;

import java.util.List;
import java.util.Set;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Data
public class StudijskiProgram {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String oznaka;  // RN, RM
	private String naziv;   
	private Integer godinaAkreditacije;
	private String zvanje;
	private Integer trajanjeGodina;
	private Integer trajanjeSemestara;
	///private String vrstaStudija; // OAS - osnovne akademske studje, OSS - osnovne strukovne, 	MAS - master akademske studije
	private Integer ukupnoEspb;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    @OneToMany(mappedBy = "studProgram")
    private List<Predmet> predmeti;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
   @ManyToOne
    private VrstaStudija vrstaStudija;

    public StudijskiProgram() {
    }

    public StudijskiProgram(String oznaka, String naziv, Integer godinaAkreditacije, Integer trajanjeSemestara) {
        this.oznaka = oznaka;
        this.naziv = naziv;
        this.godinaAkreditacije = godinaAkreditacije;
        this.trajanjeSemestara = trajanjeSemestara;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOznaka() {
        return oznaka;
    }

    public void setOznaka(String oznaka) {
        this.oznaka = oznaka;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public Integer getGodinaAkreditacije() {
        return godinaAkreditacije;
    }

    public void setGodinaAkreditacije(Integer godinaAkreditacije) {
        this.godinaAkreditacije = godinaAkreditacije;
    }

    public Integer getTrajanjeSemestara() {
        return trajanjeSemestara;
    }

    public void setTrajanjeSemestara(Integer trajanjeSemestara) {
        this.trajanjeSemestara = trajanjeSemestara;
    }

    public VrstaStudija getVrstaStudija() {
        return vrstaStudija;
    }

    public void setVrstaStudija(VrstaStudija vrstaStudija) {
        this.vrstaStudija = vrstaStudija;
    }

    public List<Predmet> getPredmeti() {
        return predmeti;
    }

    public void setPredmeti(List<Predmet> predmeti) {
        this.predmeti = predmeti;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudijskiProgram that = (StudijskiProgram) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }



/*
    @OneToMany(mappedBy = "studijskiProgram")
    private Set<StudentIndeks> studenti;
*/
}
