package org.raflab.studsluzba.controllers.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
@Data
public class IspitRequest {
    @NotNull
    private LocalDate datumOdrzavanja;

    @NotNull
    private Long predmetId; // referenca na Predmet

    @NotNull
    private Long nastavnikId; // referenca na Nastavnik

    @NotNull
    private LocalTime vremePocetka;

    private boolean zakljucen;

    @NotNull
    private Long ispitniRokId; // referenca na IspitniRok
}
