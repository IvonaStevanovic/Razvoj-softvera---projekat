package org.raflab.studsluzba.controllers.response;

import lombok.Data;

@Data
public class DrziPredmetResponse {
    private Long id;
    private Long predmetId;
    private String predmetNaziv;
    private Long nastavnikId;
    private String nastavnikImePrezime;
}
