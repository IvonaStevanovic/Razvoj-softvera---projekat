package org.raflab.studsluzba.controllers.response;

import lombok.Data;
@Data
public class PredispitniPoeniResponse {
    private Long id;
    private Integer poeni;

    // Student podaci
    private Long studentIndeksId;
    private Integer studentBrojIndeksa;
    private Integer studentGodinaIndeksa;
    private String studentIme;
    private String studentPrezime;

    // Predispitna obaveza
    private Long predispitnaObavezaId;
    private String obavezaVrsta;
    private Integer maxPoena;

    // Predmet
    private Long predmetId;
    private String predmetSifra;
    private String predmetNaziv;

    // Školska godina
    private Long skolskaGodinaId;
    private String skolskaGodinaNaziv;
}
