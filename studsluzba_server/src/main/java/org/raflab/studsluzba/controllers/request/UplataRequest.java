package org.raflab.studsluzba.controllers.request;

import lombok.Data;
import java.time.LocalDate;

@Data
public class UplataRequest {
    private Long studentId;
    private Double iznosRsd;
    private Double iznosEur;
    private String napomena;
    private LocalDate datumUplate;
}
