package org.raflab.studsluzba.controllers.request;

import lombok.Data;

@Data
public class SlusaPredmetRequest {
    private Long studentIndeksId;
    private Long drziPredmetId;
    private Long skolskaGodinaId;
}
