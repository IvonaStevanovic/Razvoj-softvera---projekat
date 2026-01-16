package org.raflab.studsluzba.controllers.response;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PolozeniPredmetiResponse {
    private Long id;
    private Long studentIndeksId;
    private String studentImePrezime;
    private Long predmetId;
    private String predmetNaziv;
    private Integer ocena;
    private boolean priznat;
    private Long izlazakNaIspitId;
    private Integer espb;
    private LocalDate datumPolaganja;
}
