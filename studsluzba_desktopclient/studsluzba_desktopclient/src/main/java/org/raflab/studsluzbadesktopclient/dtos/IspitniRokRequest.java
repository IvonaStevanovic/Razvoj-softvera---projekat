package org.raflab.studsluzbadesktopclient.dtos;

import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;

@Data
public class IspitniRokRequest {
    @NotNull
    private String naziv;

    @NotNull
    private Long skolskaGodinaId;

    @NotNull
    private LocalDate pocetak;

    @NotNull
    private LocalDate kraj;

    private Boolean aktivan;

    public IspitniRokRequest() {

    }
}
