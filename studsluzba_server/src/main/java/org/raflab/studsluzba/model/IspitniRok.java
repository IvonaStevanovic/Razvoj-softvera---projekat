package org.raflab.studsluzba.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import net.minidev.json.annotate.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Entity
@Data
public class IspitniRok {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate datumPocetka;
    private LocalDate datumZavrsetka;

    @OneToMany(mappedBy = "ispitniRok", cascade = CascadeType.ALL,orphanRemoval = true,fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    private Set<Ispit> ispiti;

    @ManyToOne
    @ToString.Exclude
    private SkolskaGodina skolskaGodina;
}
