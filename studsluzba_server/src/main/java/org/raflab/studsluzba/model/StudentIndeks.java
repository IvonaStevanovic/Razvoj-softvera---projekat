package org.raflab.studsluzba.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.*;
import lombok.Data;
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

    @ManyToOne
    private StudentPodaci student;

    @ManyToOne
    private StudijskiProgram studijskiProgram;   // na koji studijski program je upisan

    private Integer ostvarenoEspb;

    @OneToMany(mappedBy = "studentIndeks", cascade = CascadeType.ALL)
    private Set<IzlazakNaIspit> izlazakNaIspite = new HashSet<>();

    @OneToMany(mappedBy = "studentIndeks", cascade = CascadeType.ALL)
    private Set<PrijavaIspita> prijaveIspita = new HashSet<>();

    @OneToMany(mappedBy = "studentIndeks", cascade = CascadeType.ALL)
    private Set<UpisGodine> upisiGodina = new HashSet<>();

    @OneToMany(mappedBy = "studentIndeks", cascade = CascadeType.ALL)
    private Set<ObnovaGodine> obnoveGodina = new HashSet<>();

    @OneToMany(mappedBy = "studentIndeks", cascade = CascadeType.ALL)
    private Set<PredispitniPoeni> predispitniPoeni = new HashSet<>();

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
}