package org.raflab.studsluzbadesktopclient.dtos;

import lombok.Data;
import java.time.LocalDate;
import java.util.Set;

@Data
public class ObnovaGodineResponse {
    private Long id;
    private int godinaStudija;
    private LocalDate datum;
    private String napomena;
    private Long studentIndeksId;
    private String studentImePrezime;
    private Set<String> predmetiNazivi; // Lista predmeta kao stringovi
}
