package org.raflab.studsluzba.controllers.request;

import lombok.Data;

@Data
public class DodajPredispitnePoeneRequest {
    private Long studentIndeksId;
    private Long predispitnaObavezaId;
    private Integer poeni;
}
