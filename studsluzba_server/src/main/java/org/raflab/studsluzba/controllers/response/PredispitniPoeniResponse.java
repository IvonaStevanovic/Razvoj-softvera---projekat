package org.raflab.studsluzba.controllers.response;

import lombok.Data;

@Data
public class PredispitniPoeniResponse {
    private Long id;
    private Integer poeni;
    private Long studentIndeksId;
    private Long predispitnaObavezaId;
    private Long slusaPredmetId;
    private Long skolskaGodinaId;
}
