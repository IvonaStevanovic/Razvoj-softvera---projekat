package org.raflab.studsluzba.controllers.response;

import lombok.Data;

@Data
public class PredmetResponse {
    private Long id;
    private String sifra;
    private String naziv;
    private String opis;
    private Integer espb;
    private String studProgramNaziv;
    private Integer fondVezbe;
    private Integer fondPredavanja;
    private boolean obavezan;
}
