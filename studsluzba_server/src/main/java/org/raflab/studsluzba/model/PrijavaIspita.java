package org.raflab.studsluzba.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Generated;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
public class PrijavaIspita {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate datumPrijave;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToOne
    private Ispit ispit;
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToOne
    private StudentIndeks studentIndeks;
    @Column(name = "izasao", nullable = false)
    private Boolean izasao = false;
    @OneToMany(mappedBy = "prijavaIspita", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<IzlazakNaIspit> izlasci;

}
