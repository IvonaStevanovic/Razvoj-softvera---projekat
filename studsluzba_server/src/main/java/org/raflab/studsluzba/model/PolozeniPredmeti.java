package org.raflab.studsluzba.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class PolozeniPredmeti {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private StudentIndeks studentIndeks;

    @ManyToOne
    private Predmet predmet;

    private Integer ocena;
    private boolean priznat;


      @OneToOne(mappedBy = "polozenPredmet")
          private IzlazakNaIspit izlazakNaIspit;

}
