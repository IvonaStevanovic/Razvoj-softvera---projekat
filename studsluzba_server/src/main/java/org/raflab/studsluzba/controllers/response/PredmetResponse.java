package org.raflab.studsluzba.controllers.response;

import lombok.Data;

@Data
public class PredmetResponse {
    private Long id;
    private String sifra;
    private String naziv;
    private String opis;
    private Integer espb;
    private Long studijskiProgramId;
    private String studijskiProgramNaziv;
    private Boolean obavezan;


}
