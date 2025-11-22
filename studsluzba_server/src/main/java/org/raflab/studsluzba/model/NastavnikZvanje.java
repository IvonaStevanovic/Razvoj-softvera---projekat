package org.raflab.studsluzba.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.minidev.json.annotate.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class NastavnikZvanje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    private LocalDate datumIzbora;
    private String naucnaOblast;
    private String uzaNaucnaOblast;
    private String zvanje; // ovo ćemo mapirati u DTO kao String
    private boolean aktivno;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    private Nastavnik nastavnik;
}
