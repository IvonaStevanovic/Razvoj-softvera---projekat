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

    public PredmetResponse() {
    }

    public PredmetResponse(Long id, String naziv, Integer espb) {
        this.id=id;
        this.naziv=naziv;
        this.espb=espb;
    }
}
