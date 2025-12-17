package org.raflab.studsluzba.controllers.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class IspitniRokRequest {
    @NotNull(message = "Naziv je obavezan")
    private String naziv;

    @NotNull(message = "Školska godina je obavezna")
    private Long skolskaGodinaId;

    @NotNull(message = "Datum početka je obavezan")
    private LocalDate pocetak;

    @NotNull(message = "Datum kraja je obavezan")
    private LocalDate kraj;

    private Boolean aktivan;
}
