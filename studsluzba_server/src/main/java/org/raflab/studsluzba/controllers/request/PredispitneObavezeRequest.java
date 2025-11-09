package org.raflab.studsluzba.controllers.request;

import lombok.Data;

@Data
public class PredispitneObavezeRequest {
    private String vrsta;
    private Integer maksPoeni;
    private Long drziPredmetId;
    private Long skolskaGodinaId;
}
