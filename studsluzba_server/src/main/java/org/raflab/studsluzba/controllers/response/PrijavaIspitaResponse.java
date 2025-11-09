package org.raflab.studsluzba.controllers.response;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PrijavaIspitaResponse {
    private Long id;
    private LocalDate datumPrijave;
    private Long ispitId;
    private Long studentIndeksId;
}
