package org.raflab.studsluzba.controllers.request;

import lombok.Data;

import java.time.LocalDate;
@Data
public class UplataRequest {
    private Double iznos;
    private LocalDate datumUplate;
}
