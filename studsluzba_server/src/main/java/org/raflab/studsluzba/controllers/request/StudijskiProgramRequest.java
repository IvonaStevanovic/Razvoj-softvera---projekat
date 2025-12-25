package org.raflab.studsluzba.controllers.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudijskiProgramRequest {

    @NotNull(message = "Oznaka je obavezna")
    private String oznaka;

    @NotNull(message = "Naziv je obavezan")
    private String naziv;

    @NotNull(message = "Godina akreditacije je obavezna")
    private Integer godinaAkreditacije;

    private String zvanje;

    private Integer trajanjeGodina;

    private Integer trajanjeSemestara;

    private Integer ukupnoEspb;

    private Long vrstaStudijaId;
}
