package org.raflab.studsluzba.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
public class Uplata {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate datumUplate;

    private Double iznos;

    private Double srednjiKurs;

    @ManyToOne
    private StudentIndeks studentIndeks;
}
