package org.raflab.studsluzba.controllers.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class NastavnikZvanjeRequest {
    private Long id;
    @NotNull
    private LocalDate datumIzbora;
    private String naucnaOblast;
    private String uzaNaucnaOblast;
    private String zvanje;
    private boolean aktivno;
    @NotNull
    private Long nastavnikId;
}
