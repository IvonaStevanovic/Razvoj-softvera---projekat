package org.raflab.studsluzbadesktopclient.dtos;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class PredmetRequest {

    @NotNull
    private String sifra;

    @NotNull
    private String naziv;

    private String opis;

    @NotNull
    private Integer espb;

    private Integer fondPredavanja;
    private Integer fondVezbe;

    private Long studijskiProgramId;

    @NotNull
    private Boolean obavezan;
}
