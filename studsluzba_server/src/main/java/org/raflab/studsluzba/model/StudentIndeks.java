package org.raflab.studsluzba.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import net.minidev.json.annotate.JsonIgnore;

@Entity
@Data
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"broj", "godina", "studProgramOznaka"}))
public class StudentIndeks {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int broj;
    private int godina;
    private String studProgramOznaka;
    private String nacinFinansiranja;
    private boolean aktivan;
    private LocalDate vaziOd;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToOne
    private StudentPodaci student;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToOne
    private StudijskiProgram studijskiProgram;   // na koji studijski program je upisan

    private Integer ostvarenoEspb;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(mappedBy = "studentIndeks", cascade = CascadeType.ALL)
    private Set<IzlazakNaIspit> izlazakNaIspite = new HashSet<>();

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(mappedBy = "studentIndeks", cascade = CascadeType.ALL)
    private Set<PrijavaIspita> prijaveIspita = new HashSet<>();

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(mappedBy = "studentIndeks", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<UpisGodine> upisiGodina = new HashSet<>();

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(mappedBy = "studentIndeks", cascade = CascadeType.ALL)
    private Set<ObnovaGodine> obnoveGodina = new HashSet<>();

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(mappedBy = "studentIndeks", cascade = CascadeType.ALL)
    private Set<PredispitniPoeni> predispitniPoeni = new HashSet<>();

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(mappedBy = "studentIndeks", cascade = CascadeType.ALL)
    private Set<PolozeniPredmeti> polozeniPredmeti = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudentIndeks that = (StudentIndeks) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public void dodajPolozeniPredmet(Predmet predmet, int ukupnoPoena, IzlazakNaIspit izlazak) {
        if (predmet == null || izlazak == null) {
            throw new IllegalArgumentException("Predmet i izlazak na ispit ne mogu biti null");
        }

        PolozeniPredmeti pp = new PolozeniPredmeti();
        pp.setPredmet(predmet);
        pp.setOcena(ukupnoPoena); // čuvamo ukupno poena (predispitne + ispit)
        pp.setPriznat(ukupnoPoena >= 51); // priznato ako ukupno >= 51
        pp.setDatumPolaganja(LocalDate.now());
        pp.setIzlazakNaIspit(izlazak);

        // Dodavanje u listu položenih predmeta
        if (this.polozeniPredmeti == null) {
            this.polozeniPredmeti = new HashSet<>();
        }
        this.polozeniPredmeti.add(pp);
    }


}