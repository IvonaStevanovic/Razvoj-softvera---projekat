package org.raflab.studsluzba.controllers.request;

import lombok.Data;

@Data
public class PolozeniPredmetiRequest {
    private Long studentIndeksId;
    private Long predmetId;
    private Integer ocena;
    private boolean priznat;
    private Long izlazakNaIspitId;
}