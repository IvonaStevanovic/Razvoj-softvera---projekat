package org.raflab.studsluzba.controllers.request;

import lombok.Data;

@Data
public class PredispitniPoeniRequest {
    private Integer poeni;
    private Long studentIndeksId;
    private Long predispitnaObavezaId;
    private Long slusaPredmetId; // opcionalno
    private Long skolskaGodinaId;
}
