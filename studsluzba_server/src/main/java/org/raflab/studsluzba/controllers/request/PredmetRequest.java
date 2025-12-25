package org.raflab.studsluzba.controllers.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PredmetRequest {

    @NotNull(message = "Šifra je obavezna")
    private String sifra;

    @NotNull(message = "Naziv je obavezan")
    private String naziv;

    private String opis;

    @NotNull(message = "ESPB poeni su obavezni")
    @Min(value = 1, message = "Minimalno 1 ESPB")
    @Max(value = 12, message = "Maksimalno 12 ESPB")
    private Integer espb;

    private Integer fondPredavanja;
    private Integer fondVezbe;

    private Long studijskiProgramId;

    @NotNull(message = "Mora se naglasiti da i je obavezan predmet!")
    private Boolean obavezan;
}
