package org.raflab.studsluzbadesktopclient.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor // Zamenjuje tvoj prazan konstruktor
public class StudentPodaciResponse {
    private Long id;
    private String ime;
    private String prezime;
    private String srednjeIme;
    private String jmbg;
    private LocalDate datumRodjenja;
    private String mestoRodjenja;
    private String mestoPrebivalista;
    private String drzavaRodjenja;
    private String drzavljanstvo;
    private String nacionalnost;
    private String pol;
    private String adresa;
    private String brojTelefonaMobilni;
    private String brojTelefonaFiksni;
    private String emailFakultet;
    private String emailPrivatni;
    private String brojLicneKarte;
    private String licnuKartuIzdao;
    private String mestoStanovanja;
    private String adresaStanovanja;
    private Integer godinaUpisa;
    private Integer brojIndeksa; // Ključno za specifikaciju
    private String srednjaSkola; // Naziv škole iz šifarnika
    public StudentPodaciResponse(Long id, String ime, String prezime, Integer brojIndeksa,Integer godinaUpisa) {
        this.id = id;
        this.ime = ime;
        this.prezime = prezime;
        this.brojIndeksa = brojIndeksa;
    }

    public void setPol(String pol) {
        if (pol != null && (pol.equalsIgnoreCase("M") || pol.equalsIgnoreCase("Ž"))) {
            this.pol = pol.toUpperCase();
        }
    }

    public int getGodinaUpisa() {
        return godinaUpisa;
    }

    public void setGodinaUpisa(int godinaUpisa) {
        this.godinaUpisa = godinaUpisa;
    }

    public int getBrojIndeksa() {
        return brojIndeksa;
    }

    public void setBrojIndeksa(int brojIndeksa) {
        this.brojIndeksa = brojIndeksa;
    }
}