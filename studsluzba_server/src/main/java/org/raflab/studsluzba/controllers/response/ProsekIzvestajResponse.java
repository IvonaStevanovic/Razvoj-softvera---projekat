package org.raflab.studsluzba.controllers.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProsekIzvestajResponse {
    private String predmetNaziv;
    private String period;
    private Double prosek;
    private String studentIndeks; // Dodato
    private String studentImePrezime; // Dodato
    private Integer ocena; // Dodato

}