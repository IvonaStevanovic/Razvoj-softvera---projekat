package org.raflab.studsluzba.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import net.minidev.json.annotate.JsonIgnore;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.*;

@Entity
@Data
public class StudentPodaci {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ime;
    private String prezime;
    private String srednjeIme;
    private String jmbg;
    private LocalDate datumRodjenja;
    private String mestoRodjenja;
    private String drzavaRodjenja;
    private String drzavljanstvo;
    private String nacionalnost;
    private Character pol;
    private String adresa;
    private String brojTelefonaMobilni;
    private String brojTelefonaFiksni;
    private String emailFakultet;
    private String emailPrivatni;
    private String brojLicneKarte;
    private String licnuKartuIzdao;
    private String mestoStanovanja;
    private String adresaStanovanja;

    private Double uspehSrednjaSkola;
    private Double uspehPrijemni;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToOne
    private SrednjaSkola srednjaSkola;  // šifarnik

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToOne
    private VisokoskolskaUstanova ustanovaPrelaska;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(mappedBy = "student",cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<StudentIndeks> indeksi = new HashSet<>();

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(mappedBy = "studentPodaci", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Uplata> uplate;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudentPodaci that = (StudentPodaci) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public String getSrednjeIme() {
        return srednjeIme;
    }

    public void setSrednjeIme(String srednjeIme) {
        this.srednjeIme = srednjeIme;
    }

    public String getJmbg() {
        return jmbg;
    }

    public void setJmbg(String jmbg) {
        this.jmbg = jmbg;
    }

    public LocalDate getDatumRodjenja() {
        return datumRodjenja;
    }

    public void setDatumRodjenja(LocalDate datumRodjenja) {
        this.datumRodjenja = datumRodjenja;
    }

    public String getMestoRodjenja() {
        return mestoRodjenja;
    }

    public void setMestoRodjenja(String mestoRodjenja) {
        this.mestoRodjenja = mestoRodjenja;
    }

    public String getDrzavaRodjenja() {
        return drzavaRodjenja;
    }

    public void setDrzavaRodjenja(String drzavaRodjenja) {
        this.drzavaRodjenja = drzavaRodjenja;
    }

    public String getDrzavljanstvo() {
        return drzavljanstvo;
    }

    public void setDrzavljanstvo(String drzavljanstvo) {
        this.drzavljanstvo = drzavljanstvo;
    }

    public String getNacionalnost() {
        return nacionalnost;
    }

    public void setNacionalnost(String nacionalnost) {
        this.nacionalnost = nacionalnost;
    }

    public Character getPol() {
        return pol;
    }

    public void setPol(Character pol) {
        this.pol = pol;
    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public String getBrojTelefonaMobilni() {
        return brojTelefonaMobilni;
    }

    public void setBrojTelefonaMobilni(String brojTelefonaMobilni) {
        this.brojTelefonaMobilni = brojTelefonaMobilni;
    }

    public String getBrojTelefonaFiksni() {
        return brojTelefonaFiksni;
    }

    public void setBrojTelefonaFiksni(String brojTelefonaFiksni) {
        this.brojTelefonaFiksni = brojTelefonaFiksni;
    }

    public String getEmailFakultet() {
        return emailFakultet;
    }

    public void setEmailFakultet(String emailFakultet) {
        this.emailFakultet = emailFakultet;
    }

    public String getEmailPrivatni() {
        return emailPrivatni;
    }

    public void setEmailPrivatni(String emailPrivatni) {
        this.emailPrivatni = emailPrivatni;
    }

    public String getBrojLicneKarte() {
        return brojLicneKarte;
    }

    public void setBrojLicneKarte(String brojLicneKarte) {
        this.brojLicneKarte = brojLicneKarte;
    }

    public String getLicnuKartuIzdao() {
        return licnuKartuIzdao;
    }

    public void setLicnuKartuIzdao(String licnuKartuIzdao) {
        this.licnuKartuIzdao = licnuKartuIzdao;
    }

    public String getMestoStanovanja() {
        return mestoStanovanja;
    }

    public void setMestoStanovanja(String mestoStanovanja) {
        this.mestoStanovanja = mestoStanovanja;
    }

    public String getAdresaStanovanja() {
        return adresaStanovanja;
    }

    public void setAdresaStanovanja(String adresaStanovanja) {
        this.adresaStanovanja = adresaStanovanja;
    }

    public Double getUspehSrednjaSkola() {
        return uspehSrednjaSkola;
    }

    public void setUspehSrednjaSkola(Double uspehSrednjaSkola) {
        this.uspehSrednjaSkola = uspehSrednjaSkola;
    }

    public Double getUspehPrijemni() {
        return uspehPrijemni;
    }

    public void setUspehPrijemni(Double uspehPrijemni) {
        this.uspehPrijemni = uspehPrijemni;
    }

    public SrednjaSkola getSrednjaSkola() {
        return srednjaSkola;
    }

    public void setSrednjaSkola(SrednjaSkola srednjaSkola) {
        this.srednjaSkola = srednjaSkola;
    }

    public VisokoskolskaUstanova getUstanovaPrelaska() {
        return ustanovaPrelaska;
    }

    public void setUstanovaPrelaska(VisokoskolskaUstanova ustanovaPrelaska) {
        this.ustanovaPrelaska = ustanovaPrelaska;
    }

    public Set<StudentIndeks> getIndeksi() {
        return indeksi;
    }

    public void setIndeksi(Set<StudentIndeks> indeksi) {
        this.indeksi = indeksi;
    }

    public Set<Uplata> getUplate() {
        return uplate;
    }

    public void setUplate(Set<Uplata> uplate) {
        this.uplate = uplate;
    }
}