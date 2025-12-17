package org.raflab.studsluzba.controllers.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
@Data
public class IspitRequest {
    @NotNull(message = "Predmet je obavezan")
    private Long predmetId;

    @NotNull(message = "Ispitni rok je obavezan")
    private Long ispitniRokId;

    @NotNull(message = "Nastavnik je obavezan")
    private Long drziPredmetId;  // ID iz DrziPredmet tabele

    @NotNull(message = "Datum održavanja je obavezan")
    private LocalDate datumOdrzavanja;

    private LocalDateTime vremePocetka;

    private String napomena;
}