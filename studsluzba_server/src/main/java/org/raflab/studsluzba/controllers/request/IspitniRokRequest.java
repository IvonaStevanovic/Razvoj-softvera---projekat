package org.raflab.studsluzba.controllers.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class IspitniRokRequest {
    private LocalDate datumPocetka;
    private LocalDate datumZavrsetka;
    private Long skolskaGodinaId;
}
