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
    private LocalDateTime vremePocetka;
    private Boolean zakljucen;
    private String napomena;

}
