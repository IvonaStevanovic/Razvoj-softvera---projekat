package org.raflab.studsluzba.controllers.response;

import lombok.Data;

@Data
public class PredispitneObavezeResponse {
    private Long id;
    private String vrsta;
    private Integer maksPoeni;
    private Long drziPredmetId;
    private Long skolskaGodinaId;
}
