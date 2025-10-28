package org.raflab.studsluzba.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Data
public class SkolskaGodina {
    @Id
    private Long id;
    private String naziv;
    private boolean aktivna;

    @OneToMany(mappedBy = "skolskaGodina")
    private List<DrziPredmet> drziPredmetList;


    @OneToMany(mappedBy = "skolskaGodina", cascade = CascadeType.ALL)
    private Set<SlusaPredmet> slusaPredmeti;

    @OneToMany(mappedBy = "skolskaGodina", cascade = CascadeType.ALL)
    private Set<IspitniRok> ispitniRokovi;

    @OneToMany(mappedBy = "skolskaGodina", cascade = CascadeType.ALL)
    private Set<PredispitniPoeni> predispitniPoeni;
}
