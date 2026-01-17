package org.raflab.studsluzba.controllers.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NepolozeniPredmetResponse {
    private Long id;            // ID iz tabele SlusaPredmet
    private Long predmetId;     // ID iz tabele Predmet (OVO NAM JE TREBALO)
    private String sifraPredmeta;
    private String nazivPredmeta;
    private int espb;
}
