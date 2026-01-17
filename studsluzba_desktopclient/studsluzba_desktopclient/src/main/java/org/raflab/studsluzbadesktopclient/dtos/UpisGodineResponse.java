package org.raflab.studsluzbadesktopclient.dtos;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class UpisGodineResponse {
    private Long id;
    private int godinaStudija;
    private LocalDate datumUpisa;
    private String napomena;
    private String skolskaGodina;
    private List<PredmetResponse> predmeti; // Dodato jer server ovo šalje
}