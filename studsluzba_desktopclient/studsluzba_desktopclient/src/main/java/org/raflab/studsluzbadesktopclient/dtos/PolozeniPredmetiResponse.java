package org.raflab.studsluzbadesktopclient.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PolozeniPredmetiResponse {
    private Long id;

    @JsonProperty("predmetNaziv") // Mapira serversko polje na tvoje
    private String nazivPredmeta;

    private Integer ocena;

    // Server ne šalje ESPB u ovom DTO-u, pa ćemo ga za sad
    // tretirati kao 0 ili ga moraš dodati na serveru.
    private Integer espb = 0;

    // Ako server šalje datum kroz izlazakNaIspit ili slično, dodaj ovde
    private String datumPolaganja = "-";
}
