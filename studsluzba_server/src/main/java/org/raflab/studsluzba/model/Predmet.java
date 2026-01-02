package org.raflab.studsluzba.model;

import javax.persistence.*;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Set;

@Entity
@Data
@ToString(exclude = {"studProgram"})
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "sifra"))
public class Predmet {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	private String sifra;
	private String naziv;
	private String opis;
	private Integer espb;

    @EqualsAndHashCode.Exclude
	@ManyToOne
	private StudijskiProgram studProgram;
    private Integer fondVezbe;
    private Integer fondPredavanja;
	private boolean obavezan;

    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "drziPredmet", cascade = CascadeType.ALL)
          private Set<SlusaPredmet> slusaPredmetSet;

    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "predmet", cascade = CascadeType.ALL)
          private Set<DrziPredmet> drziPredmetSet;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((sifra == null) ? 0 : sifra.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Predmet other = (Predmet) obj;
		if (sifra == null) {
			if (other.sifra != null)
				return false;
		} else if (!sifra.equals(other.sifra))
			return false;
		return true;
	}


     /* @OneToMany(mappedBy = "predmet", cascade = CascadeType.ALL)
    private Set<PredispitniPoeni> predispitniPoeni; */

}
