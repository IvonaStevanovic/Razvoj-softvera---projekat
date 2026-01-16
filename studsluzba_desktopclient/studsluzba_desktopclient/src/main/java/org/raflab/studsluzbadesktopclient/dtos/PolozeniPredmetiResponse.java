package org.raflab.studsluzbadesktopclient.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PolozeniPredmetiResponse {
    private Long id;

    // Server šalje "predmetNaziv", Jackson to stavlja u varijablu "nazivPredmeta"
    @JsonProperty("predmetNaziv")
    private String nazivPredmeta;

    private Integer ocena;

    // Polja koja server šalje, a koja Jackson mora da prepozna
    private Long studentIndeksId;
    private String studentImePrezime;
    private Long predmetId;
    private boolean priznat;
    private Long izlazakNaIspitId;

    // Polja koja koristiš na klijentu za UI
    private Integer espb ;
    private LocalDate datumPolaganja;
}
