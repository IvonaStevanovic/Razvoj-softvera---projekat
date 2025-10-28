package org.raflab.studsluzba.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Data
public class ObnovaGodine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer godinaStudija;
    private LocalDate datum;
    private String napomena;


       @ManyToOne
          private StudentIndeks studentIndeks;
       @ManyToMany
          @JoinTable(
              name = "obnova_predmeti",
              joinColumns = @JoinColumn(name = "obnova_godine_id"),
              inverseJoinColumns = @JoinColumn(name = "predmet_id")
          )
          private Set<Predmet> predmetiKojeUpisuje;

}
