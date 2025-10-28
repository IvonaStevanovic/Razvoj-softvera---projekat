package org.raflab.studsluzba.model;

import javax.persistence.*;

import lombok.Data;

import java.util.Set;

@Entity
@Data
public class SlusaPredmet {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	@ManyToOne	
	private StudentIndeks studentIndeks;
	
	@ManyToOne
	private DrziPredmet drziPredmet;

    @ManyToOne
    private SkolskaGodina skolskaGodina;


    @OneToMany(mappedBy = "slusaPredmet", cascade = CascadeType.ALL)
          private Set<PredispitniPoeni> predispitniPoeni;

    @OneToMany(mappedBy = "slusaPredmet", cascade = CascadeType.ALL)
          private Set<IzlazakNaIspit> izlazakNaIspite;

}
