package org.raflab.studsluzbadesktopclient.dtos;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PrijavaIspitaRequest {
    private LocalDate datumPrijave;
    private Long ispitId;
    private Long studentIndeksId;
    private String studentIndeksUnos;
}
