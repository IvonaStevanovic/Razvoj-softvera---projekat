package org.raflab.studsluzba.model;

import lombok.Data;
import lombok.Generated;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
public class PrijavaIspita {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate datumPrijave;
    @ManyToOne
    private Ispit ispit;
    @ManyToOne
    private StudentIndeks studentIndeks;
    @Column(name = "izasao", nullable = false)
    private Boolean izasao = false;
}
