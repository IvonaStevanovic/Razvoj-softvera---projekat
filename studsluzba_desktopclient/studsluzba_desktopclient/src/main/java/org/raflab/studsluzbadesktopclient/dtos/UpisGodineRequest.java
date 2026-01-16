package org.raflab.studsluzbadesktopclient.dtos;

import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
public class UpisGodineRequest {
    private Integer godinaStudija;
    private LocalDate datum;
    private String napomena;
    private Long studentIndeksId;
    private Set<Long> prenetiPredmetiIds;
}
