package org.raflab.studsluzba.controllers.response;

import lombok.Data;
import java.time.LocalDate;
import java.util.Set;

@Data
public class UpisGodineResponse {
    private Long id;
    private Integer godinaStudija;
    private LocalDate datum;
    private String napomena;
    private Long studentIndeksId;
    private Set<String> prenetiPredmetiNazivi;
}
