package org.raflab.studsluzba.controllers.response;

import lombok.Data;

@Data
public class StudentIspitRezultatiResponse {
    private Long studentIndeksId;
    private String ime;
    private String prezime;
    private String studijskiProgram;
    private int godinaUpisa;
    private String brojIndeksa;
    private int ukupnoPoena;
}

