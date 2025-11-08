package org.raflab.studsluzba.controllers.request;

import lombok.Data;

@Data
public class IzlazakNaIspitRequest {
    private Long studentIndeksId;
    private Long ispitId;
    private Long slusaPredmet;
    private Integer ostvarenoNaIspitu;
    private String napomena;
    private boolean ponistio;
    private boolean izasao;
}
