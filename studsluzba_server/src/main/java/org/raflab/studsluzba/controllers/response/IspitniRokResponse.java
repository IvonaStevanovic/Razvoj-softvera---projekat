package org.raflab.studsluzba.controllers.response;

import lombok.Data;

import java.time.LocalDate;

@Data
public class IspitniRokResponse {
    private Long id;
    private String naziv;
    private Long skolskaGodinaId;
    private String skolskaGodinaNaziv;
    private LocalDate pocetak;
    private LocalDate kraj;
    private Boolean aktivan;
}
