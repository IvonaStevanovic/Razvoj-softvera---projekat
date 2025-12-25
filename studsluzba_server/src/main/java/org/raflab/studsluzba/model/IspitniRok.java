package org.raflab.studsluzba.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import net.minidev.json.annotate.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Data
public class IspitniRok {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String naziv;

    private LocalDate datumPocetka;
    private LocalDate datumZavrsetka;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "skolska_godina_id", nullable = false)
    private SkolskaGodina skolskaGodina;

    @Column(nullable = false)
    private Boolean aktivan = false;
    @OneToMany(mappedBy = "ispitniRok", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ispit> ispiti = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public LocalDate getDatumPocetka() {
        return datumPocetka;
    }

    public void setDatumPocetka(LocalDate datumPocetka) {
        this.datumPocetka = datumPocetka;
    }

    public LocalDate getDatumZavrsetka() {
        return datumZavrsetka;
    }

    public void setDatumZavrsetka(LocalDate datumZavrsetka) {
        this.datumZavrsetka = datumZavrsetka;
    }

    public SkolskaGodina getSkolskaGodina() {
        return skolskaGodina;
    }

    public void setSkolskaGodina(SkolskaGodina skolskaGodina) {
        this.skolskaGodina = skolskaGodina;
    }

    public Boolean getAktivan() {
        return aktivan;
    }

    public void setAktivan(Boolean aktivan) {
        this.aktivan = aktivan;
    }

    public void addIspit(Ispit ispit) {
        ispiti.add(ispit);
        ispit.setIspitniRok(this);
    }

    public void removeIspit(Ispit ispit) {
        ispiti.remove(ispit);
        ispit.setIspitniRok(null);
    }

    /* @OneToMany(mappedBy = "ispitniRok", cascade = CascadeType.ALL,orphanRemoval = true,fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    private Set<Ispit> ispiti;

     */
}
