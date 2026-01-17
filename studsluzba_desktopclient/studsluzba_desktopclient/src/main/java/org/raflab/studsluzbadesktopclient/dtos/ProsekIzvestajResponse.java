package org.raflab.studsluzbadesktopclient.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProsekIzvestajResponse {
    private String predmetNaziv;
    private String period;
    private Double prosek;
    private String studentIndeks; // Dodato
    private String studentImePrezime; // Dodato
    private Integer ocena; // Dodato


}