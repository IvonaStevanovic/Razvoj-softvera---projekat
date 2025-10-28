package org.raflab.studsluzba.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Data
public class SkolskaGodina {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String naziv;
    private boolean aktivna;

    @OneToMany(mappedBy = "skolskaGodina")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<DrziPredmet> drziPredmetList;

    @OneToMany(mappedBy = "skolskaGodina", cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<SlusaPredmet> slusaPredmeti;

    @OneToMany(mappedBy = "skolskaGodina", cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<IspitniRok> ispitniRokovi;

    @OneToMany(mappedBy = "skolskaGodina", cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<PredispitniPoeni> predispitniPoeni;
}
