package org.raflab.studsluzba.controllers.response;

import lombok.Data;

import java.time.LocalDate;
@Data
public class UplataResponse {
    private Long id;
    private LocalDate datumUplate;
    private Double iznos;
    private Double srednjiKurs;
}
