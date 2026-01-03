package org.raflab.studsluzba.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

import javax.persistence.*;

@Entity
@Data
public class Grupa {
	
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
	@ManyToOne
	private StudijskiProgram studijskiProgram;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
	@ManyToMany(fetch = FetchType.EAGER)
	private List<Predmet> predmeti;

}
