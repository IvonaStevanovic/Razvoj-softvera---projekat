package org.raflab.studsluzba.controllers.response;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class IspitResponse {
    private Long id;
    private LocalDate datumOdrzavanja;
    private Long predmetId;
    private String predmetNaziv;
    private Long nastavnikId;
    private String nastavnikImePrezime;
    private LocalTime vremePocetka;
    private boolean zakljucen;
    private Long ispitniRokId;
}
