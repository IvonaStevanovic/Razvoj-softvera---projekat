package org.raflab.studsluzba.controllers.request;

import lombok.Data;

@Data
public class PredmetRequest {

    private String sifra;
    private String naziv;
    private String opis;
    private Integer espb;
    private Long studProgramId;
    private Integer fondVezbe;
    private Integer fondPredavanja;
    private boolean obavezan;

}