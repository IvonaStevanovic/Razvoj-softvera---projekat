package org.raflab.studsluzbadesktopclient.dtos;

import lombok.Data;

@Data
public class NepolozeniPredmetDTO {   private Long id;                 // ID entiteta SlusaPredmet
    private String sifraPredmeta;
    private String nazivPredmeta;
    private Integer espb;
    private String imeNastavnika;    // opcionalno
    private String prezimeNastavnika; // opcionalno

}
