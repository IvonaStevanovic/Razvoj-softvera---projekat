package org.raflab.studsluzba.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
public class NastavnikZvanje {
	
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private LocalDate datumIzbora;  // ili reizbora
	private String naucnaOblast;  // sifarnik na klijentu - tabela u bazi bez veze
	private String uzaNaucnaOblast;  // sifarnik na klijentu - tabela u bazi bez veze
	private String zvanje;   // sifarnik na klijentu - tabela u bazi bez veze
	private boolean aktivno;

	@ManyToOne
	private Nastavnik nastavnik;
}
