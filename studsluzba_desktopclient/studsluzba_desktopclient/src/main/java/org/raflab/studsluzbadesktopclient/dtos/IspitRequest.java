package org.raflab.studsluzbadesktopclient.dtos;

import lombok.Data;
import org.jetbrains.annotations.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class IspitRequest {

    private Long predmetId;


    private Long ispitniRokId;

    private LocalDate ispitniRokKraj;

    private Long drziPredmetId;  // ID iz DrziPredmet tabele


    private LocalDate datumOdrzavanja;

    private LocalTime vremePocetka;

    private String napomena;


    public IspitRequest() {

    }
}