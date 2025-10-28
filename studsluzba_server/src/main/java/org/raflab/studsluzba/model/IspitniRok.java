package org.raflab.studsluzba.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

@Entity
@Data
public class IspitniRok {
    @Id
    private Long id;

    private LocalDate datumPocetka;
    private LocalDate datumZavrsetka;

    @OneToMany(mappedBy = "ispitniRok", cascade = CascadeType.ALL)
    private Set<Ispit> ispiti;

    @ManyToOne
    private SkolskaGodina skolskaGodina;
}
