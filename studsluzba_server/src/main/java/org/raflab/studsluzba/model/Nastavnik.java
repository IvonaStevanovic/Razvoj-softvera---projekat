package org.raflab.studsluzba.model;

import lombok.Data;
import net.minidev.json.annotate.JsonIgnore;

import java.time.LocalDate;
import java.util.Set;

import javax.persistence.*;

@Entity
@Data
public class Nastavnik {
	 
	 @Id
	 @GeneratedValue(strategy=GenerationType.IDENTITY)
	 private Long id;
	 private String ime;
	 private String prezime;
	 private String srednjeIme;
	 private String email;
	 private String brojTelefona;
	 private String adresa;

	 @OneToMany(mappedBy = "nastavnik")
	 private Set<NastavnikZvanje> zvanja;
	 
	 private LocalDate datumRodjenja;
	 private Character pol;
	 private String jmbg;

    @ManyToMany
    @JoinTable(
        name = "nastavnik_obrazovanje",
        joinColumns = @JoinColumn(name = "nastavnik_id"),
        inverseJoinColumns = @JoinColumn(name = "ustanova_id")
    )
    private Set<VisokoskolskaUstanova> obrazovanja;
}
