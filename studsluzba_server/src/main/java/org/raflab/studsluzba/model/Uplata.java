package org.raflab.studsluzba.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "uplata")
public class Uplata {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "uplata_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private StudentPodaci student;  // ← PROMENJENO SA Student NA StudentPodaci

    @Column(name = "datum_uplate", nullable = false)
    private LocalDate datumUplate;

    @Column(name = "iznos_rsd", nullable = false)
    private Double iznosRsd;

    @Column(name = "srednji_kurs", nullable = false)
    private Double srednjiKurs;

    @Column(name = "iznos_eur")
    private Double iznosEur;

    @Column(name = "napomena")
    private String napomena;
    @ManyToOne
    @JoinColumn(name = "student_podaci_id")
    private StudentPodaci studentPodaci;

    // Constructors
    public Uplata() {
    }

    public Uplata(StudentPodaci student, LocalDate datumUplate, Double iznosRsd, Double srednjiKurs) {
        this.student = student;
        this.datumUplate = datumUplate;
        this.iznosRsd = iznosRsd;
        this.srednjiKurs = srednjiKurs;
        this.iznosEur = iznosRsd / srednjiKurs;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StudentPodaci getStudent() {  // ← PROMENJEN TIP
        return student;
    }

    public void setStudent(StudentPodaci student) {  // ← PROMENJEN TIP
        this.student = student;
    }

    public LocalDate getDatumUplate() {
        return datumUplate;
    }

    public void setDatumUplate(LocalDate datumUplate) {
        this.datumUplate = datumUplate;
    }

    public Double getIznosRsd() {
        return iznosRsd;
    }

    public void setIznosRsd(Double iznosRsd) {
        this.iznosRsd = iznosRsd;
        if (this.srednjiKurs != null) {
            this.iznosEur = iznosRsd / this.srednjiKurs;
        }
    }

    public Double getSrednjiKurs() {
        return srednjiKurs;
    }

    public void setSrednjiKurs(Double srednjiKurs) {
        this.srednjiKurs = srednjiKurs;
        if (this.iznosRsd != null) {
            this.iznosEur = this.iznosRsd / srednjiKurs;
        }
    }

    public Double getIznosEur() {
        return iznosEur;
    }

    public void setIznosEur(Double iznosEur) {
        this.iznosEur = iznosEur;
    }


    public Double getIznos() {
        return iznosEur;  // ili iznosRsd, zavisi šta koristiš
    }

    public void setIznos(Double iznos) {
        this.iznosEur = iznos;
    }


    public LocalDate getDatum() {
        return datumUplate;
    }

    public void setDatum(LocalDate datum) {
        this.datumUplate = datum;
    }

    public String getNapomena() {
        return napomena;
    }

    public void setNapomena(String napomena) {
        this.napomena = napomena;
    }

    public void setStudentIndeks(StudentIndeks studentIndeks) {

    }
}
