package org.raflab.studsluzba.controllers.response;

import lombok.Data;

@Data
public class PredispitneObavezeResponse {
    private Long id;
    private Long predmetId;
    private String predmetNaziv;
    private Long skolskaGodinaId;
    private String skolskaGodinaNaziv;
    private String vrsta;
    private Integer maksPoeni;
}
