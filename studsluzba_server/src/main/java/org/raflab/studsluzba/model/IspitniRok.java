package org.raflab.studsluzba.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Data
public class IspitniRok {
    @Id
    private Long id;

    private LocalDate datumPocetka;
    private LocalDate datumZavrsetka;

    @ManyToMany
    private List<Ispit> ispiti;


}
