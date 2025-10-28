package org.raflab.studsluzba.model;

import lombok.Data;
import net.minidev.json.annotate.JsonIgnore;

import java.time.LocalDate;
import java.util.Set;

import javax.persistence.*;

@Entity
@Data
public class StudentPodaci {
	
	 @Id
	 @GeneratedValue(strategy=GenerationType.IDENTITY)
	 private Long id;
	 private String ime;
	 private String prezime;
	 private String srednjeIme;
	 private String jmbg;    
	 private LocalDate datumRodjenja;
	 private String mestoRodjenja; 
	 private String mestoPrebivalista;
	 private String drzavaRodjenja;
	 private String drzavljanstvo;
	 private String nacionalnost;   // samoizjasnjavanje, moze bilo sta  
	 private Character pol;
	 private String adresa;
	 private String brojTelefonaMobilni;  
	 private String brojTelefonaFiksni;
	 private String emailFakultet;
	 private String emailPrivatni;
	 private String brojLicneKarte;
	 private String licnuKartuIzdao;
	 private String mestoStanovanja;
	 private String adresaStanovanja;   // u toku studija

    @ManyToOne
    private SrednjaSkola srednjaSkola;  // šifarnik
    @ManyToOne
    private VisokoskolskaUstanova ustanovaPrelaska;

    @JsonIgnore
    @OneToMany(mappedBy = "student")
    private Set<StudentIndeks> indeksi;

}
