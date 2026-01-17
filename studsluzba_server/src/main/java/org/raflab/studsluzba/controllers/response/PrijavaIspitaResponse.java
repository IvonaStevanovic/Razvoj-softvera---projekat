package org.raflab.studsluzba.controllers.response;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PrijavaIspitaResponse {
    private Long id;
    private LocalDate datumPrijave;
    private Long ispitId;
    private Long studentIndeksId;
    private String studentIndeksUnos;


    public PrijavaIspitaResponse(Long id, Long id1, Long id2, LocalDate datumPrijave) {
        this.id=id;
        this.ispitId=id1;
        this.studentIndeksId=id2;
        this.datumPrijave=datumPrijave;
    }
}
