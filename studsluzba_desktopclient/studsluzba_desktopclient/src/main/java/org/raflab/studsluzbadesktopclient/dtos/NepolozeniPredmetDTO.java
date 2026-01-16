package org.raflab.studsluzbadesktopclient.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class NepolozeniPredmetDTO {   private Long id;                 // ID entiteta SlusaPredmet
    private String sifraPredmeta;
    private String nazivPredmeta;
    private Integer espb;
    private String imeNastavnika;    // opcionalno
    private String prezimeNastavnika; // opcionalno

}
