package org.raflab.studsluzba.controllers.response;

import lombok.Data;

@Data
public class IzlazakNaIspitResponse {
    private Long id;
    private Integer ostvarenoNaIspitu;
    private String napomena;
    private boolean ponistio;
    private boolean izasao;
    private Long studentIndeksId;
    private String studentImePrezime;
    private Long ispitId;
    private String predmetNaziv;
}
