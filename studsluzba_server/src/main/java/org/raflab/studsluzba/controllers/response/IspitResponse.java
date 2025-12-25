package org.raflab.studsluzba.controllers.response;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class IspitResponse {
    private Long id;

    private Long predmetId;
    private String predmetSifra;
    private String predmetNaziv;

    private Long ispitniRokId;
    private String ispitniRokNaziv;

    private Long skolskaGodinaId;
    private String skolskaGodinaNaziv;

    private Long nastavnikId;
    private String nastavnikIme;
    private String nastavnikPrezime;

    private LocalDate datumOdrzavanja;
    private LocalTime vremePocetka;
    private Boolean zakljucen;
    private String napomena;
    private LocalDate ispitniRokPocetak;
    private LocalDate ispitniRokKraj;

    public Long getIspitniRokId() { return ispitniRokId; }
    public void setIspitniRokId(Long ispitniRokId) { this.ispitniRokId = ispitniRokId; }

    public String getIspitniRokNaziv() { return ispitniRokNaziv; }
    public void setIspitniRokNaziv(String ispitniRokNaziv) { this.ispitniRokNaziv = ispitniRokNaziv; }

    public LocalDate getIspitniRokPocetak() { return ispitniRokPocetak; }
    public void setIspitniRokPocetak(LocalDate ispitniRokPocetak) { this.ispitniRokPocetak = ispitniRokPocetak; }

    public LocalDate getIspitniRokKraj() { return ispitniRokKraj; }
    public void setIspitniRokKraj(LocalDate ispitniRokKraj) { this.ispitniRokKraj = ispitniRokKraj; }

}
