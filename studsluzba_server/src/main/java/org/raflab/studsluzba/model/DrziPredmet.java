package org.raflab.studsluzba.model;

import javax.persistence.*;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Set;

@Entity
@Data
public class DrziPredmet {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
	@ManyToOne
	private Nastavnik nastavnik;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
	@ManyToOne
	private Predmet predmet;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToOne
    private SkolskaGodina skolskaGodina;
/*
    @OneToMany(mappedBy = "drziPredmet", cascade = CascadeType.ALL)
    private Set<SlusaPredmet> studentiKojiSlusaju;
*/
}
