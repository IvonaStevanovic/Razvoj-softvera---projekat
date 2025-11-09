package org.raflab.studsluzba.controllers.response;

import lombok.Data;

@Data
public class SlusaPredmetResponse {
    private Long id;
    private Long studentIndeksId;
    private String studentImePrezime;
    private Long drziPredmetId;
    private String predmetNaziv;
    private String nastavnikImePrezime;
    private Long skolskaGodinaId;
    private String skolskaGodinaNaziv; // opcionalno
}
